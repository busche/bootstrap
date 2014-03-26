package de.ismll.bootstrap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotates a named parameter on the command line. Parameters on the command
 * line are passed as <code>cmdline()=value</code>, such that the cmdline
 * parameter denotes the key on the command line. The value is subject for
 * conversion by means of the convert() method.
 * 
 * 
 * @author André Busche
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
	/**
	 * denotes the key of the parameter on the command line
	 * 
	 * @return the key String for the parameter
	 */
	String cmdline();

	/**
	 * optional description for the parameter. may be empty. "" by default
	 * 
	 * @return the description of a parameter
	 */
	String description() default "";

	/**
	 * only one field, which needs to be an array, may have this annotation set
	 * to true. If to, each following command line arg is parsed as the fields
	 * type
	 */
	boolean vararg() default false;

}
