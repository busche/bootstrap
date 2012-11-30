package de.ismll.bootstrap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Start {
	public static void main(String[] args){
		try {
			Object obj = Class.forName(args[0]).newInstance();
			
			String[] temp = new String[args.length-1];
			
			for(int i=1 ; i<args.length ; i++){
				temp[i-1] = args[i];
			}
			
			
			CommandLineParser.parseCommandLine(temp,obj);
			
			
			for(Method method : obj.getClass().getMethods()){
				if(method.getName().contains("get")){
					try {
						System.out.println(method.getName() + ":" + method.invoke(obj));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
			
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
