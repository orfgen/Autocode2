package experiments.boofcv.autocode2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class GeneratorBase {
	
	private ArrayList<File> templateFiles = new ArrayList<>();
	
	private File outputFile = null;
	
	/**
	 * Adds a template file to the generator.
	 * The template files are processed in the same order they were added, the output appended to the output file.
	 * @param filePath
	 */
	protected void addTemplate(String filePath) {
		templateFiles.add(new File(filePath));
	}
	
	protected void setOutput(String filePath) {
		outputFile = new File(filePath);
	}

	/**
	 * Generates the output file based on added template files.
	 * values contains %PLACEHOLDERS% as keys and their replacements as values.
	 * @param values
	 */
	public void generate(HashMap<String, String> values) {
		if(outputFile==null) {
			throw new NullPointerException("Output file not set.");
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			
			for(int i=0;i<templateFiles.size();i++) {
				File file = templateFiles.get(i);
				// Would be handled by appendTemplate as well, this only ensures that strings in the
				// text file that accidently have %PLACEHOLDER% form are not replaced.
				if(file.getName().endsWith(".txt")) {
					appendText(bw, file);
				} else if(file.getName().endsWith(".template")) {
					appendTemplate(bw, file, values);
				} else {
					throw new IllegalArgumentException("Unknown file extension: "+file.getName());
				}
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private void appendTemplate(BufferedWriter bw, File file, HashMap<String, String> values) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line = br.readLine();
		while(line!=null) {			
			if(line.contains("BEGIN_BLOCK")) {
				handleBlock(br, bw, values);
			} else {
				for(String key : values.keySet()) {
					line = line.replace(key, values.get(key));
				}
				bw.write(line);
				bw.newLine();
			}
			
			line = br.readLine();
		}

		br.close();
	}

	private void appendText(BufferedWriter bw, File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line = br.readLine();
		while(line!=null) {
			bw.write(line);
			bw.newLine();
			
			line = br.readLine();
		}

		br.close();
	}

	public void handleBlock(BufferedReader br, BufferedWriter bw, HashMap<String, String> values) throws IOException {
		ArrayList<String> block = readBlock(br);
		
		String[] familyArr = {"Gray", "Interleaved"};
		String[] signedArr = {"S", "U"};
		String[] numBitsArr = {"8", "16"};
		
		// Could be done in a more general form by generating all permutations of a list of arrays,
		// but for demonstrational purpose this is easier to read.
		for(String family : familyArr) {
			for(String signed : signedArr) {
				for(String numBits : numBitsArr) {

					// Create a copy to implement poor man's variable scope. 
					HashMap<String, String> valuesCopy = new HashMap<>();
					valuesCopy.putAll(values);
			
					valuesCopy.put(Constants.FAMILY, family);
					valuesCopy.put(Constants.SIGNED, signed);
					valuesCopy.put(Constants.NUM_BITS, numBits);
					addDerivedValues(valuesCopy);

					generateBlock(block, bw, valuesCopy);
				}
			}
		}
	}

	private void generateBlock(ArrayList<String> block, BufferedWriter bw, HashMap<String, String> values) throws IOException {
		for(int i=0;i<block.size();i++) {
			String line = block.get(i);
			
			for(String key : values.keySet()) {
				line = line.replace(key, values.get(key));
			}

			bw.write(line);
			bw.newLine();
		}
	}

	private ArrayList<String> readBlock(BufferedReader br) throws IOException {
		ArrayList<String> block = new ArrayList<>();
		
		String line = br.readLine();
		while(!line.contains("END_BLOCK")) {
			block.add(line);

			line = br.readLine();
		}

		return block;
	}

	/**
	 * Used to set values derived from the current ones in the map.
	 * Handles derived values defined in Constants.java.
	 * @param values
	 */
	private void addDerivedValues(HashMap<String, String> values) {
		if("Interleaved".equals(values.get(Constants.FAMILY))) {
			values.put(Constants.NUM_BANDS, "*input.numBands");
		} else {
			values.put(Constants.NUM_BANDS, "");
		}
		
		if("U".equals(values.get(Constants.SIGNED))) {
			values.put(Constants.UNSIGNED_SUFFIX, "U");
		} else {
			values.put(Constants.UNSIGNED_SUFFIX, "");	
		}
		
		String imageType = values.get(Constants.FAMILY) + values.get(Constants.SIGNED) + values.get(Constants.NUM_BITS);
		values.put(Constants.IMAGE_TYPE, imageType);
		
		addCustomDerivedValues(values);
	}

	/**
	 * Used to add custom derivations in concrete generator instances.
	 * @param values
	 */
	protected abstract void addCustomDerivedValues(HashMap<String, String> values);
}
