package de.ismll.bootstrap.examples;

import java.util.Arrays;
import java.util.Map;

import de.ismll.bootstrap.CommandLineParser;
import de.ismll.bootstrap.Parameter;

public class PojoA implements Runnable {

	@Parameter(cmdline="hobbies", vararg=true)
	private String[] hobbies;
	
	@Parameter(cmdline="age")
	private int age;
	
	@Parameter(cmdline="fullname")
	private String name;

	public int getAge() { return age; }

	public void setAge(int age) { this.age = age; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	@Override
	public void run() {
		System.out.println("Age is:  " + getAge());
		System.out.println("Name is: " + getName());
		System.out.println("Hobbies: " + Arrays.toString(hobbies));
	}

	public String[] getHobbies() { return hobbies; }

	public void setHobbies(String[] hobbies) { this.hobbies = hobbies; }
	
	public static PojoA convert(Object in) {
		String s = (String) CommandLineParser.convert(in, String.class);
		
		// split string
		Map<String, Object> map = CommandLineParser.parseMap(s, ',', '=');
		PojoA ret = new PojoA();
		
		// set properties
		CommandLineParser.applyArguments(ret, map);
		
		return ret;
	}

	
}
