package de.ismll.bootstrap;

/**
 * An Exception (superclass) thrown by all Bootstrap-related methods is an unhandled situation occurs.
 * 
 * This Exception class inherits all superclass constructors, but should preferably be instantiated using {@link BootstrapException#BootstrapException(Object)} as this is caught and nicely printed by the {@link de.ismll.console.Generic} launcher class.  
 * 
 * 
 * @author André Busche
 *
 */
public class BootstrapException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8215318662956331221L;

	protected Object bootstrapEnabledObject;

	public BootstrapException() {
		super();
	}

	public BootstrapException(Object bootstrapEnabledObject) {
		super();
		this.bootstrapEnabledObject = bootstrapEnabledObject;
		
	}

	public BootstrapException(String message) {
		super(message);
		
	}

	public BootstrapException(String message, Throwable cause) {
		super(message, cause);
		
	}
	
	public BootstrapException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public BootstrapException(Throwable cause) {
		super(cause);
		
	}
	
	public Object getBootstrapEnabledObject() {
		return bootstrapEnabledObject;
	}

	@Override
	public String getMessage() {
		if (bootstrapEnabledObject==null) return super.getMessage();
		
		return "Bootstrap Exception while working on an object of type " + bootstrapEnabledObject.getClass().getCanonicalName() + ": " + super.getMessage();
	}

	public void setBootstrapEnabledObject(Object bootstrapEnabledObject) {
		this.bootstrapEnabledObject = bootstrapEnabledObject;
	}
}
