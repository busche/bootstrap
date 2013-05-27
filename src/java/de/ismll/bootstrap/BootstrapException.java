package de.ismll.bootstrap;

public class BootstrapException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8215318662956331221L;

	public BootstrapException() {
		super();
	}

	public BootstrapException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public BootstrapException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public BootstrapException(String message) {
		super(message);
		
	}

	@Override
	public String getMessage() {
		if (bootstrapEnabledObject==null) return super.getMessage();
		
		return "Bootstrap Exception while working on an object of type " + bootstrapEnabledObject.getClass().getCanonicalName() + ": " + super.getMessage();
	}
	
	public BootstrapException(Throwable cause) {
		super(cause);
		
	}

	protected Object bootstrapEnabledObject;
	
	public BootstrapException(Object bootstrapEnabledObject) {
		super();
		this.bootstrapEnabledObject = bootstrapEnabledObject;
		
	}

	public Object getBootstrapEnabledObject() {
		return bootstrapEnabledObject;
	}

	public void setBootstrapEnabledObject(Object bootstrapEnabledObject) {
		this.bootstrapEnabledObject = bootstrapEnabledObject;
	}
}
