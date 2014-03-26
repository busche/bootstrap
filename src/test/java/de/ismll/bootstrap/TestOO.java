package de.ismll.bootstrap;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import de.ismll.bootstrap.oo.AbstractA;
import de.ismll.bootstrap.oo.ImplA;
import de.ismll.bootstrap.oo.UseA;

public class TestOO {

	@Test
	public void testClassHierarchyTargetClass() {
		AbstractA aa = new ImplA();
		
		Random r = new Random();
		
		int aimpl=r.nextInt(10000);
		int abstracta=r.nextInt(10000);
		
		CommandLineParser.parseCommandLine(new String[] {
				"aimpl=" + aimpl,
				"abstracta=" + abstracta,
		}, aa);
		
		Assert.assertEquals(aa.getAbstracta(), abstracta);
		Assert.assertEquals(((ImplA)aa).getAimpl(), aimpl);

	}
	
	@Test
	public void testClassHierarchyFields() {
		Random r = new Random();
		int aimpl=r.nextInt(10000);
		int abstracta=r.nextInt(10000);
		
		
		UseA ua = new UseA();	
		
		CommandLineParser.parseCommandLine(new String[] {
				"aa=aimpl"
		}, ua);
		
		AbstractA aaobj = ua.getAa();		
		
		CommandLineParser.parseCommandLine(new String[] {
				"aimpl=" + aimpl,
				"abstracta=" + abstracta,
		}, aaobj);
		
		Assert.assertEquals(aaobj.getAbstracta(), abstracta);
		Assert.assertEquals(((ImplA)aaobj).getAimpl(), aimpl);

	}

	
}
