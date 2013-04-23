package de.ismll.bootstrap.oo;

import de.ismll.bootstrap.Parameter;

public class ImplA extends AbstractA {

	@Parameter(cmdline="aimpl")
	private int aimpl;

	public int getAimpl() {
		return aimpl;
	}

	public void setAimpl(int aimpl) {
		this.aimpl = aimpl;
	}
}
