package de.ismll.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LaunchConfig {

	private static Logger logger = LogManager.getLogger(LaunchConfig.class);

	public static String PREFIX;
	
	public static final String CLASS_NAME = "launch.class";
	
	static {
//		if (PREFIX == null || PREFIX.equals("")) {
//			CLASS_NAME="class";
//		} else {
//			CLASS_NAME = PREFIX + ".class";
//		}
		PREFIX=System.getProperty(LaunchConfig.class.getName() + ".prefix", "launch");
		if (PREFIX == null) {
			PREFIX="";
		} 
	}
	
	public static void main(String[] args) {
		if (args.length<1) {
			System.err.println("Need at least one parameter: The configuration file!");
			System.exit(0);
		}
		File input = new File(args[0]);
		Properties config= new Properties();
		InputStream is = null;
		if (input.exists()) {
			try {
				is = new FileInputStream(input);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		} else {
			is = LaunchConfig.class.getClassLoader().getResourceAsStream(args[0]);
			if (is==null) {
				String error = args[0] + " was found neither by the classloader, nor as a file!";
				logger.fatal(error);
				throw new RuntimeException(error);
			}
		}
		try {
			config.load(is);
		} catch (IOException e) {
			logger.fatal(e.getMessage(), e);
			System.exit(2);
		} 
		
		String implementationClass = config.getProperty(CLASS_NAME);
		if (implementationClass==null)
			throw new RuntimeException("Property " + CLASS_NAME + " missing on property file " + args[0] + "!");
		Class<?> forName;
		try {
			forName = Class.forName(implementationClass);
		} catch (ClassNotFoundException e) {
			logger.fatal("Class not found: " + implementationClass, e);
			System.exit(1);
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Class<? extends Runnable> asSubclass;
		try {
			asSubclass = forName.asSubclass(Runnable.class);			
		} catch (ClassCastException e) {
			logger.fatal(implementationClass + " is no subclass of " + Runnable.class , e);
			System.exit(1);
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Runnable execClass;
		try {
			execClass = asSubclass.newInstance();
		} catch (InstantiationException e) {
			logger.fatal(e.getMessage(), e);
			System.exit(1);
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.fatal(e.getMessage(), e);
			System.exit(1);
			throw new RuntimeException(e.getMessage(), e);
		}
		HashMap<String, String> configArgs = parseOptions0(config, PREFIX);
		
		logger.info("Calling " + implementationClass + " with arguments " + configArgs.toString());
		
		CommandLineParser.applyArguments0(execClass, configArgs);
		execClass.run();
	}

	
	public static HashMap<String, String> parseOptions(Properties config) {
		return parseOptions0(config, "");
	}
	
	private static HashMap<String, String> parseOptions0(Properties config, String prefix) {
		HashMap<String, String> configArgs = new HashMap<String, String>();
		for (Object keyO : config.keySet()) {
			String key = (String)keyO;
			if (!key.startsWith(prefix)) {
				logger.debug("Skipping property " + key);
				continue;				
			}
			if (key.equals(CLASS_NAME)) {
				logger.debug("Skipping property " + key);
				continue;
			}
			
			int startIdx = 0;
			if (prefix.length()>0)
				startIdx = prefix.length()+1;
			configArgs.put(key.substring(startIdx), config.getProperty(key));
		}
		return configArgs;
	}


	public static void configureFromProperties(Object obj,
			Properties config) {
		HashMap<String, String> parseOptions = parseOptions(config);
		CommandLineParser.applyArguments0(obj, parseOptions);		
	}
}
