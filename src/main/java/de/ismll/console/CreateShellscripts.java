package de.ismll.console;

import java.io.Closeable;
import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;  
import java.util.ArrayList;  
import java.util.List;  

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ismll.bootstrap.Parameter;
  
/**
 * 
 * Creates run-scripts for classes implementing the Runnable interface.
 * 
 * Inspired by http://www.coderanch.com/t/485985/vc/Iterating-classes-package-Java-project
 * 
 * @author bcsanbu
 *
 */
public class CreateShellscripts implements Runnable {  
  
    public static List<Class<? extends Runnable>> getClassesInPackage(String pckgname, Logger logger) {
    	List<Class<? extends Runnable>> l = new ArrayList<Class<? extends Runnable>>();
    	
        File directory = getPackageDirectory(pckgname);  
        if (!directory.exists()) {  
            throw new IllegalArgumentException("Could not get directory resource for package " + pckgname + ".");  
        }  
  
        getClassesInPackage(l, logger, pckgname, directory);
        
        return l;
    }  
  
    private static void getClassesInPackage(List<Class<? extends Runnable>> list, Logger logger, String pckgname, File directory) {  
        for (String filename : directory.list()) {  
            if (filename.endsWith(".class")) {  
                String classname = buildClassname(pckgname, filename);  
                try {  
                	Class<?> forName = Class.forName(classname);
//                	if (forName.isAssignableFrom(Runnable.class)) {
                	if (Runnable.class.isAssignableFrom(forName)) {                        		
                		logger.debug("Found class " + forName.toString());
                		list.add(forName.asSubclass(Runnable.class));                  		
                	}
                } catch (ClassNotFoundException e) {  
                    logger.error("Error creating class " + classname);  
                }  
            }  
        }  
    }  
  
    private static String buildClassname(String pckgname, String filename) {  
        return pckgname + '.' + filename.replace(".class", "");  
    }  
  
    private static File getPackageDirectory(String pckgname) {  
        ClassLoader cld = Thread.currentThread().getContextClassLoader();  
        if (cld == null) {  
            throw new IllegalStateException("Can't get class loader.");  
        }  
  
        URL resource = cld.getResource(pckgname.replace('.', '/'));  
        if (resource == null) {  
            throw new RuntimeException("Package " + pckgname + " not found on classpath.");  
        }  
  
        return new File(resource.getFile());  
    }  

    @Parameter(cmdline = "package", 
    		description = "Package name to scan (non-recursive)")
    private String packagename;
    
    @Parameter(cmdline="scriptfolder", description="Folder to put generated scripts into.")
    private File targetScriptFolder;
    
    @Parameter(cmdline="runsh",description="pointer to run.sh shell script (default: ./run.sh)")    		
    private File runsh;

	private Logger log = LogManager.getLogger(Generic.class);
	
	@Override
	public void run() {
		log.warn("The creation of Linux shell-scripts is an untested feature and not supported!");
		
		if (targetScriptFolder== null)targetScriptFolder = new File(".");
		if (runsh == null) runsh = new File("run.sh");
		if (!runsh.isFile()) {
			log.error("run.sh searched at " + runsh.getAbsolutePath() + " does not point to a valid file. Please specify a valid file location using the runsh=<> command line parameter");
			return;
		}
		if (!runsh.canRead()) {
			log.error("run.sh located at " + runsh + " cannot be read. Please check file permissions.");
			return;
		}
		log.info("About to scan package " + packagename + " for classes implementing Runnable...");
		List<Class<? extends Runnable>> classes = getClassesInPackage(packagename, log);
		
		for (Class<? extends Runnable> c : classes) { 
			String scriptname = c.getCanonicalName().replaceAll("\\.", "_") + ".sh";
			log.info("Creating script for class " + c.getCanonicalName() + " in as " + scriptname + " in directory " + targetScriptFolder.getAbsolutePath());
			
			copy(log, runsh, new File(targetScriptFolder, scriptname), c.getCanonicalName());
//			System.out.println("Found: " + c.getCanonicalName());  
		}
	}

	private static void copy(Logger logger, File runsh, File file, String canonicalName) {
		String content = null;
		
		FileReader reader;
		try {
			reader = new FileReader(runsh);
		} catch (FileNotFoundException e) {
			logger.warn(runsh + " does not point to an existing file.", e);
			return;
		}
		FileWriter fw;
		try {
			fw = new FileWriter(file, false);
		} catch (IOException e1) {
			logger.warn(file + " could not be opened for writing.", e1);
			closeSilent(reader);
			return;
		}
		
		char[] chars = new char[(int) runsh.length()];
		try {
			reader.read(chars);
			
			content = new String(chars);
			content = content.replace("de.ismll.console.Generic", "de.ismll.console.Generic de.ismll.console.CreateShellscripts ");
			
			fw.write(content);
			
		} catch (IOException e) {
			logger.warn("IO Exception occurred while copying contents from " + runsh + " to " + file, e);
		} finally {
			closeSilent(reader);
			closeSilent(fw);
		}
		
	}

	/**
	 * closes the closable object while rejecting the exception.
	 * 
	 * @param reader some closable object
	 */
	private static void closeSilent(final Closeable reader) {
		try {
			reader.close();
		} catch (IOException e) {
			// noop
		}
		
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public File getTargetScriptFolder() {
		return targetScriptFolder;
	}

	public void setTargetScriptFolder(File targetScriptFolder) {
		this.targetScriptFolder = targetScriptFolder;
	}

	public File getRunsh() {
		return runsh;
	}

	public void setRunsh(File runsh) {
		this.runsh = runsh;
	}  
  
}  