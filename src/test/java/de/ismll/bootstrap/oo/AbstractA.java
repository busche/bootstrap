package de.ismll.bootstrap.oo;

import de.ismll.bootstrap.Parameter;

public abstract class AbstractA {

	@Parameter(cmdline="abstracta")
	private int abstracta;

	public int getAbstracta() {
		return abstracta;
	}

	public void setAbstracta(int abstracta) {
		this.abstracta = abstracta;
	}
	
	public static AbstractA convert(Object in) {
		if (in.equals("aimpl")) return new ImplA();
		
		return null; // uggh!
	}
}
