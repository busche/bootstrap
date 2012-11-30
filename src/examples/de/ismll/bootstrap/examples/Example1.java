package de.ismll.bootstrap.examples;

import de.ismll.bootstrap.CommandLineParser;

public class Example1 {

	public static void main(String[] args) {
		PojoA object = new PojoA();
		CommandLineParser.parseCommandLine(args, object);
		System.out.println("Age is:  " + object.getAge());
		System.out.println("Name is: " + object.getName());
		
	}
}
