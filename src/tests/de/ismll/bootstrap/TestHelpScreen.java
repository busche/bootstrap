package de.ismll.bootstrap;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import de.ismll.console.Generic;

public class TestHelpScreen {

	public static final class Z implements Runnable{
		@Parameter(cmdline="y", description="some desc")
		private
		Y y;

		public Y getY() {
			return y;
		}

		public void setY(Y y) {
			this.y = y;
		}

		@Override
		public void run() {
			throw new BootstrapException("May not end up here...");
		}
	}

	public static final class Y {
		
		public static Y convert(Object in) {
			throw new RuntimeException("Some serious thing here ... to show functionality of automatic help screen display.");
		}
	}
	
	@Test
	public void testHelpScreen() {

		try {
			Generic.main(new String[] {
					"de.ismll.bootstrap.TestHelpScreen$Z", 
					"y=blub"	
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
