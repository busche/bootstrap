package de.ismll.bootstrap;

public class MyRunnable implements Runnable{

	@Override
	public void run() {
		NoRunnable nr = new NoRunnable();
		
		// Expect ClassCastException here.
		((Runnable) nr).run();
		
	}

}
