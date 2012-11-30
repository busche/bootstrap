package de.ismll.bootstrap;

public class Run {
	public static void main(String[] args){
		if (args.length<1) {
			System.err.println("Need at least one parameter, a class which implements Runnable!");
			System.exit(1);
		}
		try {
			Object obj = Class.forName(args[0]).newInstance();
			
			if (!(obj instanceof Runnable)) {
				System.err.println(args[0] + " does not implement Runnable ... cannot use it!");
				System.exit(2);
			}
			
			String[] temp = new String[args.length-1];
			
			for(int i=1 ; i<args.length ; i++){
				temp[i-1] = args[i];
			}
			
			
			CommandLineParser.parseCommandLine(temp,obj);

			((Runnable)obj).run();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
