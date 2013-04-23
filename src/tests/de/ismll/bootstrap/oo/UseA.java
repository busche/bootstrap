package de.ismll.bootstrap.oo;

import de.ismll.bootstrap.Parameter;

public class UseA {

	@Parameter(cmdline="aa")
	private AbstractA aa;

	public AbstractA getAa() {
		return aa;
	}

	public void setAa(AbstractA aa) {
		this.aa = aa;
	}
	
}
