package de.ismll.bootstrap.examples;

import de.ismll.bootstrap.Parameter;

public class HdfsWorker implements Runnable{

	@Parameter(cmdline="resource")
	private HDFSResource resource;
	
	@Override
	public void run() {
//		System.out.println("Resource is " + resource.getmHdfsPath());
	}

	public HDFSResource getResource() {
		return resource;
	}

	public void setResource(HDFSResource resource) {
		this.resource = resource;
	}

}
