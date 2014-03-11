package de.ismll.bootstrap;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import de.ismll.console.Generic;

public class TestRestartCommand {

	public static class Z implements Runnable{

		@Override
		public void run() {
			// all fine.
		}
		
	}
	
	@Test
	public void testHelpScreen() {

		try {
			Generic.main(new String[] {
					"de.ismll.bootstrap.TestRestartCommand$Z", 
					"a=blub",
					"b=\"12.4\"",
					"c=test@value",
					"d=sdf=1",
					"e=\"\"",
					"\"f=1.000\"",
					"-restart"
			});
		} catch (ClassNotFoundException e) {
			Assert.fail();
		} catch (SecurityException e) {
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.fail();
		} catch (IllegalAccessException e) {
			Assert.fail();
		} catch (InvocationTargetException e) {
			Assert.fail();
		}
	}
}
