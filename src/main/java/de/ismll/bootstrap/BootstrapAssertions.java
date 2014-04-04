package de.ismll.bootstrap;

/**
 * Simple Assertions, typically used at the beginning of an executive block
 * (e.g., a run()-method to ensure that all parameters were correctly injected).
 * 
 * 
 * @author Andre Busche
 * 
 */
public final class BootstrapAssertions {

	/**
	 * no instantiation. This provides just helper methods.
	 */
	private BootstrapAssertions() {
		
	}
	
	/**
	 * Throws an appropriate BootstrapException if reference is null. Otherwise,
	 * does nothing. If bootstrap_enabled_object is not null, and the code is
	 * launched through the de.ismll.console.Generic launcher, the help dialog
	 * is shown on error. The variablename may not be null and should give a
	 * named hint, which field / variable should be not null the next time.
	 * 
	 * 
	 * @param bootstrapEnabledObject
	 *            may be null
	 * @param reference
	 *            may be null
	 * @param variablename
	 *            May not be null. a variable name, depending on the context
	 *            either the cmdline of a parameter, or the plain field name.
	 * @throws BootstrapException
	 *             if reference is null (Desired behaviour)
	 */
	public static void notNull(final Object bootstrapEnabledObject,
			final Object reference, final String variablename)
			throws BootstrapException {
		if (reference == null) {
			if (bootstrapEnabledObject != null) {
				CommandLineParser.printCommandLineHelp(bootstrapEnabledObject);
			}
			BootstrapException throwThis = new BootstrapException(
					"The variable " + variablename
							+ " should not have had a null value!");
			throwThis.setBootstrapEnabledObject(bootstrapEnabledObject);
			throw throwThis;
		}
	}

	/**
	 * Checks for the object reference to be not null.
	 * 
	 * calls notNull(null, reference, variable)
	 * 
	 * @param reference
	 *            the object reference
	 * @param variable
	 *            an arbitrary variable name
	 * @throws BootstrapException
	 */
	public static void notNull(final Object reference,
			final String variable) throws BootstrapException {
		notNull(null, reference, variable);
	}

	/**
	 * calls notNull(null, reference, variable) while giving an explanatory text
	 * for the variable name.
	 * 
	 * @param reference
	 *            an arbitrary object reference
	 * @throws BootstrapException
	 */
	public static void notNull(final Object reference)
			throws BootstrapException {
		notNull(null, reference, "an object of class "
				+ reference.getClass().getCanonicalName());
	}

	/**
	 * calls assertTrue(condition, errorMessage, null) without an explanatory
	 * text.
	 * 
	 * @param condition
	 *            the condition to check
	 * @param errorMessage
	 *            the error message (logged if the check fails)
	 * @throws BootstrapException
	 */
	public static void assertTrue(final boolean condition,
			final String errorMessage) throws BootstrapException {
		assertTrue(condition, errorMessage, null);
	}

	/**
	 * Throws a BootstrapException if condition is false. The error message
	 * should be the plain text variant of the boolean test, e.g. "a < 4" or
	 * "a*b<0". The actual object may be used to additionally state the expected
	 * value. Samples, when BootstrapExceptions are thrown:
	 * <ul>
	 * <li>assertTrue(a<4,"a<4",a) throws an BootstrapException for any a >=4,
	 * e.g., a=8, stating that the condition "a<4" evaluated to false for a
	 * value of 8.
	 * <li>assertTrue(obj!=null,"obj!=null",obj) throws an BootstrapException if
	 * obj is null,stating that the condition "obj!=null" evaluated to false for
	 * a value of null.
	 * 
	 * @param condition
	 *            the condition to check
	 * @param errorMessage
	 *            the free-text assertion message (should help the user to
	 *            understand the problem).
	 * @param actual
	 *            the actual value of the object being checked.
	 * @throws BootstrapException
	 */
	public static void assertTrue(final boolean condition,
			final String errorMessage, final Object actual)
			throws BootstrapException {
		if (!condition) {
			throw new BootstrapException("Assertion failed. Assertion "
					+ errorMessage + " evaluated to false "
					+ (actual != null ? "(is: " + actual.toString() + ")" : "")
					+ "!");
		}
	}

}
