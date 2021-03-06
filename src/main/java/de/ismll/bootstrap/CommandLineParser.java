package de.ismll.bootstrap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * @author Martin Ortmann (initial version), Andre Busche (various extensions)
 *
 */
public class CommandLineParser{

	private static final char ARRAY_END = ']';
	private static final char ARRAY_START = '[';
	private static final String ARRAY_END_STRING = ARRAY_END + "";
	private static final String ARRAY_START_STRING = ARRAY_START + "";
	public static final String ARRAY_DELIMITER;
	public static final char ARRAY_DELIMITER_CHAR;
	private static String SVN_VERSION="";
	private static String KEY_SVN_VERSION="svn.version";
	private static String MODULE_VERSION="";
	private static String KEY_MODULE_VERSION="module.version";
	
	private static Logger logger = LogManager.getLogger(CommandLineParser.class);

	static {
		String t = System.getProperty(CommandLineParser.class.toString() + ".arrayDelimiter");
		ARRAY_DELIMITER=(t==null?",":t);
		ARRAY_DELIMITER_CHAR=ARRAY_DELIMITER.charAt(0);
		
		InputStream resource = CommandLineParser.class.getResourceAsStream("/application.properties");
		
		if (null != resource) {			
			Properties p = new Properties();
			try {
				p.load(resource);
				Object valueSvnVersion = p.get(KEY_SVN_VERSION);
				if (null != valueSvnVersion)
					SVN_VERSION=valueSvnVersion.toString();
				Object valueModuleVersion = p.get(KEY_MODULE_VERSION);
				if (null != valueSvnVersion)
					MODULE_VERSION=valueModuleVersion.toString();
			} catch (IOException e1) {
				logger.warn("Failed to read from /application.properties file to load Bootstrap metadata.");
				e1.printStackTrace();
			} finally {
				try {
					resource.close();
				} catch (IOException e) {
					//noop
				}
			}
		}
		


//		logger.debug("debug");
//		logger.info("info");
//		logger.warn("warn");
//		logger.error("error");
//		
//		try {
//			resource = CommandLineParser.class.getResourceAsStream("/log4j.properties");
//			if (null != resource) {
//				System.out.println("log4j properties found.");
//				Properties log4jDefaultProperties = new Properties();
//				log4jDefaultProperties.load(resource);
//				PropertyConfigurator.configure(log4jDefaultProperties);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (null != resource)
//				try {
//					resource.close();
//				} catch (IOException e) {
//					//noop
//				}
//		}
//
//		logger.debug("debug");
//		logger.info("info");
//		logger.warn("warn");
//		logger.error("error");
	}


//	public static boolean debug;
	private static boolean help;

	/**
	 * TODO: some time implement a new style like 
	 * 
	 * -parameter value 
	 * 
	 * with possibly multiple values. Then, implement this by setting this to true.
	 */
	private static boolean oldStyle=true;

	public static void parseCommandLine(String[] args,Object obj){
		// determine whether to print debug information
		
		logger.debug("This version was compiled on " + MODULE_VERSION + " (Version " + SVN_VERSION + ")");
		
		boolean printRestartCmdlineString = false;
		
		boolean usedMask[] = new boolean[args.length];
		Arrays.fill(usedMask, false);
				
		for (int i = 0; i < args.length; i++) {
			String string =args[i];
			if(string.toLowerCase().equals("-debug")){
				logger.setLevel(Level.DEBUG);
				logger.debug("Overriding log4j configuration and forcibly setting this loggers level to DEBUG.");
				usedMask[i]=true;
				continue;
			}
			if(string.toLowerCase().equals("-help")){
				help=true;
				usedMask[i]=true;
			}
			if(string.toLowerCase().equals("-restart")){
				printRestartCmdlineString=true;
				usedMask[i]=true;
			}
		}
		
		if (help) {
			printCommandLineHelp(obj, true);
		}
		
		// contains the actual parameters specified by the user.
		HashMap<String, Object> cmdArgs = new HashMap<String, Object>();
		
		Pattern compile = Pattern.compile("=");

		boolean varargHackEnabled=false;
		String param_name = null;
		List<String> varargs = null;
		
		for (int i = 0; i < args.length; i++) {
			if(usedMask[i]) continue;
			if (oldStyle) {
				String arg = args[i];

				if (arg.trim().length()==0)
					continue; // skip multiple blanks.
				
				try{
					String[] split = compile.split(arg, 2);
					if (split.length == 1 ) {
						if (arg.startsWith("-")) {
						// preliminary hack to allow for vararg variant like
						
						// -option value1 value2 value3 value4
						
							param_name = arg.substring(1);
							varargs = new ArrayList<String>();
							cmdArgs.put(param_name, varargs);
							varargHackEnabled=true;
						} else if (varargs != null && varargHackEnabled) { 
							// next value for vararg found
							varargs.add(arg);
						} else {
							printCommandLineHelp(obj);
							throw new RuntimeException("Vararg parsing failed.");
						}
					} else if (split.length == 2) {
						param_name = split[0];
						String param_value = split[1];
						
						cmdArgs.put(param_name, param_value);
					} else {
						printCommandLineHelp(obj);
						throw new RuntimeException("Command line contains 0 or more than 2 components. Cannot use it.");
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					logger.fatal("Error parsing parameters. Use '<parameter>=<value>' to set program parameters!\nError occurred here: " + arg + "\nExiting due to error...");
					System.exit(1);
				}
				
			} else {
				throw new RuntimeException("New style not available! Update library?");
			}
		}
		
		applyArguments(obj, cmdArgs);
		
		if (printRestartCmdlineString) {
			logger.info("Command line for re-starting: " + parameters2Cmdline(cmdArgs, param_name, varargs));
		}
	}

	private static String parameters2Cmdline(HashMap<String, Object> keyValueMap,
			String varargParametername, List<String> varargs) {
		// note: varargParametername is only meaningful if varargs != null. It is only the last parameter key given.
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Object> e : keyValueMap.entrySet()) {
			String key= e.getKey().trim();
			sb.append(key);
			sb.append('=');
			
			Object value = e.getValue();
			sb.append(value.toString());
			if (!(value instanceof String)) {
				logger.warn("Non-String value encountered for key " + key + ". You will probably see the hashCode object representation rather than a valid command line String.");
			}
			sb.append(' ');
		}

		if (varargs != null) {
			sb.append(" -");
			sb.append(varargParametername);
			for (String s : varargs) {
				sb.append(' ');
				sb.append(s);
			}
		}
		return sb.toString();
	}

	public static void printCommandLineHelp(Object obj) {
		printCommandLineHelp(obj, false);
	}
	
	public static void printCommandLineHelp(Class<?> obj) {
		printCommandLineHelp(obj, false);
	}
	
	public static void printCommandLineHelp(Class<?> obj, PrintStream out) {
		printCommandLineHelp(obj, out, false);
	}
	
	private static void printCommandLineHelp(Object obj, boolean terminateVm) {
		printCommandLineHelp(obj.getClass(), System.out, terminateVm);
	}
	
	private static void printCommandLineHelp(Class<?> clazz, PrintStream out, boolean terminateVm) {
			
		class DT{
			Parameter p;
			Field f;
		}
		SortedMap<String, DT> parameters = new TreeMap<String, DT>();
		
		int maxLengthCmdLine = -1;
		int maxLengthFieldType = -1;
		
		Class<? extends Object> targetObjectClazz = clazz;
		while (!targetObjectClazz.equals(Object.class)) {

			Field[] fields = targetObjectClazz.getDeclaredFields();
			for(Field field : fields){
				Parameter annotation = field.getAnnotation(Parameter.class);
			
				if (annotation == null)
					continue;
	
				DT d = new DT();
				d.p=annotation;
				d.f=field;
				parameters.put(annotation.cmdline(), d);
				maxLengthCmdLine = Math.max(maxLengthCmdLine, annotation.cmdline().length());
				
				String fieldClassName = getHumanFieldName(field);
				
				maxLengthFieldType = Math.max(maxLengthFieldType, fieldClassName.length());
			}
			targetObjectClazz=targetObjectClazz.getSuperclass();
		}
		
		maxLengthCmdLine = Math.max("Parameter".length(), maxLengthCmdLine);
		maxLengthFieldType = Math.max("Java-Type".length(), maxLengthFieldType);
		maxLengthCmdLine+=2;
		maxLengthFieldType+=2;
		
		out.println();
		out.println("Bootstrap version from " + MODULE_VERSION + " (Revision " + SVN_VERSION + ")");
		out.println();
		out.println("Listing command line parameters for class " + clazz.getCanonicalName() + ". They are evaluated by using Parameter=<some-value>.");
		out.println();
		out.println(String.format("%1$" + maxLengthCmdLine + "s | %2$" + maxLengthFieldType + "s | Description", "Parameter", "Java-Type"));
		out.println();
		for (Entry<String, DT> current : parameters.entrySet()){
			out.println(String.format("%1$" + maxLengthCmdLine + "s | %2$" + maxLengthFieldType + "s |  %3$s", current.getValue().p.cmdline(), getHumanFieldName(current.getValue().f), current.getValue().p.description()));
		}

		out.println();
		out.println("End of command line parameter listing.");
		
		if (terminateVm) {
			out.println("Bye!");
			System.exit(0);
		}
	}

	public static String getHumanFieldName(Field field) {
		String fieldClassName= field.getType().toString().replace("class ", "");
		if (field.getType().isArray()) {
			Class<?> componentType = field.getType().getComponentType();
			
			if (componentType == Boolean.TYPE) {
				fieldClassName="boolean[]";	
			}
			else
			if (componentType == Integer.TYPE) {
				fieldClassName="int[]";	
			}
			else
			if (componentType == Double.TYPE) {
				fieldClassName="double[]";	
			}
			else
			if (componentType == Float.TYPE) {
				fieldClassName="float[]";	
			}
			else
			if (componentType == Short.TYPE) {
				fieldClassName="short[]";	
			}
			else
			if (componentType == Byte.TYPE) {
				fieldClassName="byte[]";	
			}
			else
			if (componentType == Long.TYPE) {
				fieldClassName="long[]";	
			}
			else
			if (componentType == Character.TYPE) {
				fieldClassName="char[]";	
			}			
			else
				fieldClassName=componentType.toString().replace("class ", "") + "[]";				
		}
		return fieldClassName;
	}
	
	static void applyArguments0(Object obj, HashMap<String, String> parameters) {
		applyArguments(obj, wrap(parameters));
	}
	
	private static Map<String, Object> wrap(final HashMap<String, String> i) {
		
		return new Map<String, Object>(){

			@Override
			public int size() { return i.size();}

			@Override
			public boolean isEmpty() {return i.isEmpty();}

			@Override
			public boolean containsKey(Object key) {return i.containsKey(key);}

			@Override
			public boolean containsValue(Object value) {return i.containsValue(value);}

			@Override
			public Object get(Object key) {return i.get(key);}

			@Override
			public Object put(String key, Object value) {return i.put(key, value.toString());}

			@Override
			public Object remove(Object key) {return i.remove(key);}

			@Override
			public void putAll(Map<? extends String, ? extends Object> m) {
				// noop
			}

			@Override
			public void clear() {
				// noop				
			}

			@Override
			public Set<String> keySet() {return i.keySet();}

			@Override
			public Collection<Object> values() {
				Vector<Object> ret = new Vector<Object>();
				for (String s : i.values())
					ret.add(s);
				return ret;
				
			}

			@Override
			public Set<Entry<String, Object>> entrySet() {
				
				return new _WrappedSet(i.entrySet());
			}
			
		};
	}

	public static void applyArguments(Object obj, Map<String, Object> parameters) {
		HashMap<String, Method> setters = new HashMap<String, Method>();
		
		Method[] methods = obj.getClass().getMethods();
		for(Method m : methods) {
			String mName = m.getName();
			if(mName.startsWith("set") && mName.length()>3) {
				String fieldName = mName.substring(3,4).toLowerCase() + mName.substring(4);
				
				logger.debug(fieldName + " --> " + m);
				
				setters.put(fieldName, m);
			}
		}
		
		boolean param_found;			
		
		Class<? extends Object> targetObjectClazz = obj.getClass();
		
		// iterate over type hierarchy
		while (!targetObjectClazz.equals(Object.class)) {
			Field[] fields = targetObjectClazz.getDeclaredFields();
			for(Field field : fields){
				Parameter annotation = field.getAnnotation(Parameter.class);
			
				if (annotation == null)
					continue;
	
				String cmd = annotation.cmdline();
				boolean isVararg = annotation.vararg();
				Method m = setters.get(field.getName());
				Object param_value = parameters.get(cmd);
				parameters.remove(cmd);
				
				if(m == null) {
					logger.warn("Field name " + field.getName() + " is annotated by a @Parameter annotation (key: \"" + cmd + "\") but does not provide a Bean-compatible setter method. This is probably not what you want ...");
					continue;
				}
				if(param_value == null) {
					logger.debug("Value for " + cmd + " not set. Using implicit default.");
					continue;
				}
				
				param_found=true;
				
				try{
					Class<?> type = field.getType();
					
					logger.debug("Calling " + m.getName());
					
					if (isVararg) {
						if (!(param_value instanceof ArrayList<?>)) {
							throw new RuntimeException("Field " + cmd + " is a vararg type, but the given parameter value type is no array list. Please only pass ArrayList<?> objects as sources for vararg types.");
						}
						if (!(type.isArray())){
							throw new RuntimeException("Field " + cmd + " is defined as being vararg, but the type itself is no array. Please define it as being an array.");
						}
						Class<?> componentType = type.getComponentType();
						ArrayList<?> sourceValues = (ArrayList<?>) param_value;
						Object targetArray = Array.newInstance(componentType, sourceValues.size());
						
						for (int i = 0; i < sourceValues.size(); i++) {
							Object currentSource = sourceValues.get(i);
							Object convertedValue = convert(currentSource, componentType);
							Array.set(targetArray, i, convertedValue);
						}
						
						Object returnvalue = m.invoke(obj,targetArray);
						if (returnvalue != null) {
							reportReturnValue(m, obj, returnvalue);
						}
					} else { 
						Object convertedValue = convert(param_value,type);
						if (convertedValue!=null) {
							Object returnvalue = m.invoke(obj,convertedValue);
							if (returnvalue != null) {
								reportReturnValue(m, obj, returnvalue);
							}
						}
					}
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				if(!param_found){
					printCapabilities(obj);
					System.exit(0);
				}
			}
			targetObjectClazz = targetObjectClazz.getSuperclass();
		}
		
		// check if there are any parameters we did not use
		if(!parameters.isEmpty()){
			String[] keys = new String[1];
			keys = parameters.keySet().toArray(keys);

			for(String param : keys){
				logger.warn("Unknown parameter: " + param);
			}
		}
	}
	
	
	private static void reportReturnValue(Method m, Object obj,
			Object returnvalue) {
		logger.info("Ignoring return value " + returnvalue.toString() + " of method " + m.toString() + (obj!=null ? " of object " + obj.toString() : ""));
	}

	private static void printCapabilities(Object obj){
		Field[] fields = obj.getClass().getDeclaredFields();
		
		boolean infoEnabled = logger.isEnabledFor(Level.INFO);
		
		for(Field field : fields){
			Parameter p = field.getAnnotation(Parameter.class);
			String msg = p.cmdline() + " : " + p.description();
			if (infoEnabled)
				logger.info(msg);
			else
				System.out.println(msg); 
		}
	}
	
	private static final void fail(Object from, Class<?> object, Class<?> primitive) {
		logger.warn("Failed to convert \"" + from + "\" to type " + object + (primitive!=null?" or " + primitive:"") + " ... trying further possible converters ...");
	}
	
	public static Object convert(Object from, Class<?> targetClassType) {
		if (logger.isDebugEnabled())
			logger.debug("Converting from " + from.getClass() + " to " + targetClassType);
		// TODO: Trace the impossibility of conversion (e.g., "asdlkf" cannot be parsed as an int)
		boolean failed = false;
		
		Class<? extends Object> sourceClazz = from.getClass();
		if (targetClassType.isAssignableFrom(sourceClazz)/* || targetClassType.equals(sourceClazz)*/) {
			return from;
		}
		
		if(targetClassType == Short.class || targetClassType == short.class) {
			try {
				return Short.valueOf(from.toString());
			} catch (Exception e) {
				fail(from, Short.class, short.class);
				failed=true;
			}
			try {
				return Short.valueOf(new Double(from.toString()).shortValue());
			} catch (Exception e) {
				fail(from, Short.class, short.class);	
				failed=true;
			}
		}
		if(targetClassType == Integer.class || targetClassType == int.class) {
			try {
				return Integer.valueOf(from.toString());
			} catch (Exception e) {
				fail(from, Integer.class, int.class);	
				failed=true;
			}
			try {
				return Integer.valueOf(new Double(from.toString()).intValue());
			} catch (Exception e) {
				fail(from, Integer.class, int.class);	
				failed=true;
			}
		}
		if(targetClassType == Long.class || targetClassType == long.class) {
			try {
				return Long.valueOf(from.toString());
			} catch (Exception e) {
				fail(from, Long.class, long.class);	
				failed=true;
			}
			try {
				return Long.valueOf(new Double(from.toString()).longValue());
			} catch (Exception e) {
				fail(from, Long.class, long.class);	
				failed=true;
			}
		}
		if (targetClassType == Character.class || targetClassType == char.class) {
			return Character.valueOf(from.toString().charAt(0));
		}
		if (targetClassType == Double.class || targetClassType == double.class) {
			return new Double(from.toString());			
		}
		if (targetClassType == Float.class || targetClassType == float.class) {
			return new Float(from.toString());
		}
		if (targetClassType == URI.class) {
			return URI.create(from.toString());
		}
		if (targetClassType == String.class) {
			return from.toString();
		}
		if (targetClassType == Boolean.class || targetClassType == boolean.class) {
			return Boolean.valueOf(from.toString());
		}
		if (targetClassType == File.class) {
			File f = new File(from.toString());
			if (!f.isAbsolute())
				f = new File(System.getProperty("user.dir"), from.toString());
			return f;
		}
		
		if (targetClassType == char[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			char result[] = new char[strings.length];
			for (int i = 0; i < result.length; i++) {
				String trim = strings[i].trim();
				if (trim.length()==0)
					throw new BootstrapException("char at position " + i + " is empty. Cannot convert 'blank' chars; this is unsupported.");
				result[i] = trim.charAt(0);
			}
			return result;
		}
		
		if (targetClassType == String[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			String result[] = new String[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = strings[i].trim();
			}
			return result;
		}
		if (targetClassType == URI[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			URI result[] = new URI[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = URI.create(strings[i].trim());
			}
			return result;
		}
		

		if (targetClassType == boolean[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			boolean result[] = new boolean[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = Boolean.parseBoolean(strings[i].trim());
			}
			return result;
		}
		
		if (targetClassType == float[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			float result[] = new float[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = Float.parseFloat(strings[i].trim());
			}
			return result;
		}
		
		if (targetClassType == double[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			double result[] = new double[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = Double.parseDouble(strings[i].trim());
			}
			return result;
		}
		
		if (targetClassType == short[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			short result[] = new short[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = Short.parseShort(strings[i].trim());
			}
			return result;
		}

		if (targetClassType == int[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			int result[] = new int[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = Integer.parseInt(strings[i].trim());
			}
			return result;
		}
		if (targetClassType == long[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			long result[] = new long[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = Long.parseLong(strings[i].trim());
			}
			return result;
		}
		
		if (targetClassType == File[].class) {
			String[] strings = from.toString().replace(ARRAY_START_STRING, "").replace(ARRAY_END_STRING, "").split(ARRAY_DELIMITER);
			File result[] = new File[strings.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = new File(strings[i].trim());
				if (!result[i].isAbsolute())
					result[i] = new File(System.getProperty("user.dir"), from.toString());
			}
			return result;
		}
		
		try{
			if (targetClassType.isArray()) {
				Class<?> componentType = targetClassType.getComponentType();
				
				// non-supported, generic object ...
				logger.info("Trying to convert a non-supported, generic Object into an array type...");
				
				int length = Array.getLength(from);
				Object result = Array.newInstance(componentType, length);
//				new Object[Array.getLength(from)];
				for (int i = 0; i < length; i++) {
					Array.set(result, i, convert(Array.get(from, i), componentType));
				}
				
				return result;
			}
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		if (failed && targetClassType.isPrimitive()) {
			logger.error("Could (finally) not convert \"" + from + "\" to primitive type " + targetClassType + ". Giving up (returning null)...");
			return null;
		}
		
		if (failed) {
			logger.error("Failed at least one built-in conversion. This will probably cause an exception in a few milliseconds ...");
		}
		

		// TODO: add either possible external converters (preferred), or support for DefaultMatrix, DefaultVector, and other RTM-classes
		logger.info("No explicit converter found - looking for public static convert() method in target class...");
		
		try {
			Method convertMethod = targetClassType.getMethod("convert", Object.class);
			logger.debug("convert-Method reference is " + convertMethod + ". Invoking ...");
			if (convertMethod != null) {
				Object invoke = convertMethod.invoke(null, from);
				String invokeClassString = invoke==null?"null":invoke.getClass().toString();
				logger.debug("Returned object is of type " + invokeClassString);
				
				if (invoke != null) {
					if (targetClassType.isAssignableFrom(invoke.getClass())) {
						logger.info("Success: Object converted!");
						return invoke;
					}
				} 
				logger.info("static method found according to specification. However: either the reference returned is null, or the returned type (" + invokeClassString + ") is not the expected one (" +targetClassType + ").");
				
			}
		} catch (InvalidConversionFormatException e) {
			e.setSourceType(sourceClazz);
			e.setTargetType(targetClassType);
			throw e;
		} catch (InvocationTargetException e) {
			throw new BootstrapException(e);
		} catch (BootstrapException e) {
			throw e;
		} catch (Exception e) {
			logger.info("An error occurred while looking for a static convert(Object): " + targetClassType.getCanonicalName() + " Method in class " + targetClassType.getCanonicalName() + " (Error was: " + e.getMessage() + " while trying to convert from type " + sourceClazz.getCanonicalName() + "). Consider creating it!", e);
		}
		
		// TODO: more intelligent error message here. Case not caught: convert() throwed an error (causes wrong error message)
		logger.fatal("Invalid conversion: " + from.getClass().toString() + " to " + targetClassType.toString() + ": probably not implemented ....");
		System.exit(1);
		return new IllegalArgumentException("target class type not supported!");
	}
	
	/**
	 * Copies field values from an object to an object, conforming to the @Parameter annotations.
	 * 
	 * Fields may have different names, but same cmdline-values. Then, the value is copied.
	 * 
	 * Currently only supports copy actions, if the data types are equal! 
	 * 
	 * @param from the source object to copy members from 
	 * @param to the target object to copy members to 
	 */
	public static void copyProperties(Object from, Object to){
		Field[] fromFields = from.getClass().getDeclaredFields();
		Field[] toFields = to.getClass().getDeclaredFields();
		Method[] fromMethods = from.getClass().getMethods();
		Method[] toMethods = to.getClass().getMethods();
		HashMap<String, Object> cmdValues = new HashMap<String, Object>();
		HashMap<String, Method> setters = new HashMap<String, Method>();

		
		for (Method m : fromMethods){
			if((m.getName().startsWith("get") && Modifier.isPublic(m.getModifiers()))
					// allow "isBoolean()" beans constructs for boolean return types
					|| (m.getName().startsWith("is") && (m.getReturnType().equals(Boolean.class) || m.getReturnType().equals(boolean.class)) && Modifier.isPublic(m.getModifiers()))
					) {
				String fieldName = m.getName().substring(3,4).toLowerCase() + m.getName().substring(4);
				
				Field found = null;
				Parameter annotation = null;
				for (Field f : fromFields){
					if (!f.getName().equals(fieldName)){
						continue;
					}
					annotation = f.getAnnotation(Parameter.class);
					if (annotation==null)
						continue;
					found=f;
					break;					
				}
				if (found == null){
					logger.debug("Field \"" + fieldName + "\" not found, or it has no @Parameter-annotation. This might not be a problem!");
					continue;
				}
				
				
				logger.debug(fieldName + " --> " + m);
				
				assert(annotation!=null);
				
				try {
					cmdValues.put(annotation.cmdline(), m.invoke(from, (Object[])null));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		////////    Values extracted, now copying ...................
		
		for(Method m : toMethods) {
			if(m.getName().startsWith("set")) {
				String fieldName = m.getName().substring(3,4).toLowerCase() + m.getName().substring(4);
				
				Field found = null;
				Parameter annotation = null;
				for (Field f : toFields){
					if (!f.getName().equals(fieldName)){
						continue;
					}
					annotation = f.getAnnotation(Parameter.class);
					if (annotation==null)
						continue;
					found=f;
					break;					
				}
				if (found == null){
					logger.debug("Field \"" + fieldName + "\" has no @Parameter-annotation. This might not be a problem!");
					continue;
				}
				
				assert (annotation!=null);
				
				logger.debug(annotation.cmdline() + " --> " + m);
				
				setters.put(annotation.cmdline(), m);
			}
		}
		
		
		
		for(Field field : toFields){
			Parameter annotation = field.getAnnotation(Parameter.class);
		
			if (annotation == null)
				continue;

			String cmd = annotation.cmdline();
			Object value = cmdValues.get(cmd);
			Method m = setters.get(cmd);
			
			if(m == null) {
				logger.debug("Could not parse " + cmd);
				continue;
			}
			
			try {
				m.invoke(to,value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
	}
	

	/**
	 * calls parseMap(String, ',','=')
	 * 
	 * @param in a string with syntax key1=value1,key2=value2,key3=value3
	 * @return a map {key1=value1,key2=value2,key3=value3}
	 */
	public static Map<String, Object> parseMap(String in){
		return parseMap(in, ',', '=');
	}

	/**
	 * parses the given String in according to the following syntax:
	 * 
	 * <pre>"key1"assignment"value1"tokenDelimiter"key2"assignment"value2"tokenDelimiter"key3"assignment"value3"</pre>
	 * 
	 * @param in input string, confirming to the above syntax
	 * @param tokenDelimiter token delimiter (char within 'in'), separating individual tokens within the input string
	 * @param assignment assignment character, splitting key from value.
	 * @return a map containing the parsed information which is contained in the string
	 */
	public static Map<String, Object> parseMap(String in, char tokenDelimiter, char assignment) {
//		String[] split = in.substring(0, in.length()).split("" + tokenDelimiter);
		Map<String, Object> ret = new TreeMap<String, Object>();
		
//		List<String> split2 = new ArrayList<String>();
		
		StringBuffer sb = new StringBuffer();
		String key=null;
		
		int idx=0;
		int length = in.length();
		boolean skip=false;
		while (idx < length) {
			char currentChar = in.charAt(idx);
			
			if (currentChar == ARRAY_START) {
				skip = true;
				sb.append(currentChar);
			} else
			if (currentChar == ARRAY_END) {
				skip = false;
				sb.append(currentChar);
			} else
			if (currentChar == tokenDelimiter && !skip) {
				ret.put(key, sb.toString());
				sb.setLength(0);
				key=null;
			} else
			if (currentChar == assignment && !skip) {
				key = sb.toString();
				sb.setLength(0);
			} else {
				sb.append(currentChar);
			}
			idx++;
		}
		if (sb.length()>0 && key != null)
			ret.put(key, sb.toString());
//		for (String s : split) {
//			String[] chunk = s.split("" + assignment);
//			
//			ret.put(chunk[0], chunk[1]);
//		}
		
		return ret;
	}

//	/**
//	 * the Parameter annotation for the object bootstrap_enabled_object whose field value is val.
//	 * 
//	 * @param bootstrap_enabled_object
//	 * @param reference
//	 * @return
//	 */
//	static Parameter getAnnotation(Object bootstrap_enabled_object, Object val) {
//		
//		Class<? extends Object> targetObjectClazz = bootstrap_enabled_object.getClass();
//		while (!targetObjectClazz.equals(Object.class)) {
//
//			Field[] fields = targetObjectClazz.getDeclaredFields();
//			for(Field field : fields){
//				Parameter annotation = field.getAnnotation(Parameter.class);
//			
//				if (annotation == null) continue;
//
//				Object fieldValue;
//				try {
//					fieldValue = field.get(bootstrap_enabled_object);
//				} catch (IllegalArgumentException e) {
//					logger.debug(e);
//					continue;
//				} catch (IllegalAccessException e) {
//					logger.debug(e);
//					continue;
//				}
//				
//				if (fieldValue  == val) {
//					return annotation;
//				}
//				
//				
//			}
//			targetObjectClazz=targetObjectClazz.getSuperclass();
//		}
//		
//		return null;
//	}
}
