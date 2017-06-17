package experiments.boofcv.autocode2;

public class Constants {
	public static final String GENERATOR = "%GENERATOR%";
	
	public static final String FAMILY = "%FAMILY%";
	
	/**
	 * Special case for the handling of interleaved image types. Adds "*input.numBands" if family is "Interleaved".
	 */
	public static final String NUM_BANDS = "%NUM_BANDS%";
	
	public static final String SIGNED = "%SIGNED%";
	
	/**
	 * Special case for method names. Is replaced with "U" if unsigned, with the empty string if signed.
	 */
	public static final String UNSIGNED_SUFFIX = "%UNSIGNED_SUFFIX%";
	
	public static final String NUM_BITS = "%NUM_BITS%";
	
	/**
	 * IMAGE_TYPE is the concatenation of FAMILY, SIGNED and NUM_BITS.
	 */
	public static final String IMAGE_TYPE = "%IMAGE_TYPE%";
	
	public static final String[] ALL_CONSTANTS = {GENERATOR, FAMILY, SIGNED, NUM_BITS, IMAGE_TYPE};
}
