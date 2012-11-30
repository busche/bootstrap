package de.ismll.bootstrap.examples;

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
	}

	public String[] getHobbies() { return hobbies; }

	public void setHobbies(String[] hobbies) { this.hobbies = hobbies; }
	
}
