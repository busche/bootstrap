package de.ismll.bootstrap;

import de.ismll.console.Generic;

/**
 * An exception denoting an invalid conversion trial.
 * 
 * This exception is and should be thrown of a datatype conversion using the
 * bootstrap framework fails. It is implicitly caught by {@link Generic} and
 * prints a nicely formatted error message to the console when thrown.
 * 
 * 
 * @author André Busche
 */
public class InvalidConversionFormatException extends BootstrapException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -192056817488785045L;
	private Class<?> sourceType;
	private Class<?> targetType;
	private Object sourceString;
	private Object expectedPattern;

	public InvalidConversionFormatException(Object bootstrapEnabledObject,
			Object sourceString, Object expectedPattern) {
		super(bootstrapEnabledObject);
		this.sourceString = sourceString;
		this.expectedPattern = expectedPattern;
	}

	public InvalidConversionFormatException(Object bootstrapEnabledObject,
			Object sourceString, Object expectedPattern, Class<?> sourceType,
			Class<?> targetType) {
		this(bootstrapEnabledObject, sourceString, expectedPattern);
		this.sourceType = sourceType;
		this.targetType = targetType;
	}
	
	private String msg;
	
	@Override
	public String getMessage() {
		if (msg != null) return msg;
		
		StringBuilder sb = new StringBuilder();
		if (super.bootstrapEnabledObject!=null) {
			sb.append("Bootstrap Exception while working in object class " + super.bootstrapEnabledObject.getClass().getCanonicalName() + ": ");
		}
		if (sourceType !=null && targetType!=null) {
			sb.append("Invalid conversion from " + sourceType + " to " + targetType + ": ");
		}
		sb.append("Value " + sourceString + " could not be matched to pattern " + expectedPattern);
		msg = sb.toString();
		return msg;
	}

	public Class<?> getSourceType() {
		return sourceType;
	}

	public void setSourceType(Class<?> sourceType) {
		this.sourceType = sourceType;
	}

	public Class<?> getTargetType() {
		return targetType;
	}

	public void setTargetType(Class<?> targetType) {
		this.targetType = targetType;
	}

}
