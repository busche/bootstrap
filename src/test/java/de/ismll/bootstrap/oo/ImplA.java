package de.ismll.bootstrap.oo;

import de.ismll.bootstrap.Parameter;

public class ImplA extends AbstractA {

	@Parameter(cmdline="aimpl")
	private int aimpl;

	public int getAimpl() {
		return aimpl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + aimpl;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImplA other = (ImplA) obj;
		if (aimpl != other.aimpl)
			return false;
		return true;
	}

	public void setAimpl(int aimpl) {
		this.aimpl = aimpl;
	}
}
