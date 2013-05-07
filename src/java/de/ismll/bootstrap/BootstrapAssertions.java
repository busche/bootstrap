package de.ismll.bootstrap;

/**
 * Simple Assertions, typically used at the beginning of an executive block
 * (e.g., a run()-method to ensure that all parameters were correctly injected).
 * 
 * 
 * @author Andre Busche
 * 
 */
public class BootstrapAssertions {

	// public final static void notNull(Object bootstrap_enabled_object, Object
	// reference) throws BootstrapException{
	// if (reference==null) {
	// if (bootstrap_enabled_object!=null) {
	// CommandLineParser.printCommandLineHelp(bootstrap_enabled_object);
	// }
	// BootstrapException throwThis;
	// Parameter p = CommandLineParser.getAnnotation(bootstrap_enabled_object,
	// reference);
	//
	// throwThis = new BootstrapException("The variable annotated with " +
	// p.cmdline() + " should not have had a null value!");
	// throwThis.setBootstrapEnabledObject(bootstrap_enabled_object);
	// throw throwThis;
	// }
	// }

	/**
	 * Throws an appropriate BootstrapException if reference is null. Otherwise,
	 * does nothing. If bootstrap_enabled_object is not null, and the code is
	 * launched through the de.ismll.console.Generic launcher, the help dialog
	 * is shown on error. The variablename may not be null and should give a
	 * named hint, which field / variable should be not null the next time.
	 * 
	 * 
	 * @param bootstrap_enabled_object
	 *            may be null
	 * @param reference
	 *            may be null
	 * @param variablename
	 *            May not be null. a variable name, depending on the context
	 *            either the cmdline of a parameter, or the plain field name.
	 * @throws BootstrapException
	 *             if reference is null (Desired behaviour)
	 */
	public final static void notNull(Object bootstrap_enabled_object,
			Object reference, String variablename) throws BootstrapException {
		if (reference == null) {
			if (bootstrap_enabled_object != null) {
				CommandLineParser
						.printCommandLineHelp(bootstrap_enabled_object);
			}
			BootstrapException throwThis = new BootstrapException(
					"The variable " + variablename
							+ " should not have had a null value!");
			throwThis.setBootstrapEnabledObject(bootstrap_enabled_object);
			throw throwThis;
		}
	}

	/**
	 * calls notNull(null, reference, variable)
	 * 
	 * @param reference
	 * @param variable
	 * @throws BootstrapException
	 */
	public final static void notNull(Object reference, String variable)
			throws BootstrapException {
		notNull(null, reference, variable);
	}

	/**
	 * calls notNull(null, reference, variable) while giving an explanatory text
	 * for the variable name.
	 * 
	 * @param reference
	 * @throws BootstrapException
	 */
	public final static void notNull(Object reference)
			throws BootstrapException {
		notNull(null, reference, "an object of class "
				+ reference.getClass().getCanonicalName());
	}

	/**
	 * calls assertTrue(condition, errorMessage, null) without an explanatory
	 * text.
	 * 
	 * @param condition
	 * @param errorMessage
	 * @throws BootstrapException
	 */
	public final static void assertTrue(boolean condition, String errorMessage)
			throws BootstrapException {
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
	 * </ul>
	 * 
	 * @param condition
	 * @param errorMessage
	 * @param actual
	 * @throws BootstrapException
	 */
	public static void assertTrue(boolean condition, String errorMessage,
			Object actual) throws BootstrapException {
		if (!condition)
			throw new BootstrapException("Assertion failed. Assertion "
					+ errorMessage + " evaluated to false "
					+ (actual != null ? "(is: " + actual.toString() + ")" : "")
					+ "!");
	}

}
