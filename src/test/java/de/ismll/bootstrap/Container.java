package de.ismll.bootstrap;

import java.net.URI;

public class Container {

	@Parameter(cmdline="tint")
	private int tInt;
	
	@Parameter(cmdline="turi")
	private URI tUri;
	
	@Parameter(cmdline="tintarray")
	private int[] tArray;

	public int gettInt() {
		return tInt;
	}

	public void settInt(int tInt) {
		this.tInt = tInt;
	}

	public URI gettUri() {
		return tUri;
	}

	public void settUri(URI tUri) {
		this.tUri = tUri;
	}

	public int[] gettArray() {
		return tArray;
	}

	public void settArray(int[] tArray) {
		this.tArray = tArray;
	}
}
