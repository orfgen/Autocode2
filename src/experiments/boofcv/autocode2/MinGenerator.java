package experiments.boofcv.autocode2;

import java.util.HashMap;

public class MinGenerator extends GeneratorBase {

	public static void main(String[] args) {
		new MinGenerator();
	}

	public MinGenerator() {
		addTemplate("template/copyright.txt");
		addTemplate("template/min.template");

		setOutput("generated/ImageStatistics.java");
		
		HashMap<String, String> values = new HashMap<>();
		values.put(Constants.GENERATOR, "MinGenerator");
		
		generate(values);
	}

	@Override
	protected void addCustomDerivedValues(HashMap<String, String> values) {
		// Do nothing in this case.
	}
	
}
