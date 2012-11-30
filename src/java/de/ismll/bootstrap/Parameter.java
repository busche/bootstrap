package de.ismll.bootstrap;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
	String cmdline();
	String description() default "";
	
	/**
	 * only one field, which needs to be an array, may have this annotation set to true. If to, each following command line arg is parsed as the fields type  
	 */
	boolean vararg() default false;
	
}
