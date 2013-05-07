package de.ismll.bootstrap;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import de.ismll.console.Generic;

public class TestInstantiations {
	
	@Test
	public void testClassCastException() {
		try {
			Generic.main(new String[] {
				NoRunnable.class.getCanonicalName()
				
			});
			return;
		} catch (ClassNotFoundException e) {
			Assert.fail(e.getMessage() + " unexpected");
		} catch (SecurityException e) {
			Assert.fail(e.getMessage() + " unexpected");
		} catch (IllegalArgumentException e) {
			Assert.fail(e.getMessage() + " unexpected");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage() + " unexpected");
		} catch (InvocationTargetException e) {
			Assert.fail(e.getMessage() + " unexpected");
		}
		Assert.fail("Expected sucessful return");

	}
	
	@Test(expected=ClassCastException.class)
	public void testClassCastException2() {
		try {
			Generic.main(new String[] {
				MyRunnable.class.getCanonicalName()				
			});
			return;
		} catch (ClassNotFoundException e) {
			Assert.fail(e.getMessage() + " unexpected");
		} catch (SecurityException e) {
			Assert.fail(e.getMessage() + " unexpected");
		} catch (IllegalArgumentException e) {
			Assert.fail(e.getMessage() + " unexpected");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage() + " unexpected");
		} catch (InvocationTargetException e) {
			Assert.fail(e.getMessage() + " unexpected");
		}
		Assert.fail("Expected ClassCastException");

	}
}
