package de.ismll.bootstrap.examples;

import java.io.File;

public class PojoB {

	private PojoA reference;
	
	private File[] paths;

	public PojoA getReference() { return reference; }
	public void setReference(PojoA reference) { this.reference = reference; }

	public File[] getPaths() { return paths; }
	public void setPaths(File[] paths) { this.paths = paths; }
	
}
