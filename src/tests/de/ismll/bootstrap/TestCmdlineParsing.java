package de.ismll.bootstrap;

import java.net.URI;
import java.util.Arrays;


import org.junit.Assert;
import org.junit.Test;

public class TestCmdlineParsing {

	@Test
	public void testStatic() {
		URI turi = URI.create("test://uri");
		int t1=1234;
		int[] tArray = {12,1342,23423,412,31,23};
		
		Container c = new Container();
		
		CommandLineParser.parseCommandLine(new String[] {
				"tint=" + t1,
				"turi=" + turi,
				"tintarray=" + Arrays.toString(tArray),
				
		}, c);

		Assert.assertEquals(turi, c.gettUri());
		Assert.assertEquals(t1, c.gettInt());
		Assert.assertArrayEquals(tArray, c.gettArray());
	}
	
	
}
