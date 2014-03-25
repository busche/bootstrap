package de.ismll.bootstrap;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import de.ismll.bootstrap.oo.AbstractA;

/**
 * TODO: Add tests for custom conversions
 * 
 * @author busche
 *
 */
public class TestConversions {


	@Test
	public void testChar(){
		char value = (char) (Math.random()*254);
		String valueAsString = "" + value;
		Character conversion = (Character) CommandLineParser.convert(valueAsString, Character.class);
		Assert.assertEquals(value, conversion.charValue());
	}
	
	@Test
	// FIXME: There is a bug in this test. However - it could not be identified on 2014-03-25 when fixing Math.random() to the extremes.
	public void testCharArray(){
		// offset 33: avoid ASCII-space (32)
		char[] value = {
				(char) (33 +Math.random()*222),
				(char) (33 +Math.random()*222),
				(char) (33 +Math.random()*222),
		};
		String valueAsString = Arrays.toString(value);
		char[] conversion = (char[]) CommandLineParser.convert(valueAsString, char[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i]);
		}
	}

	@Test
	public void testString(){
		String valueAsString = "QQ4675Q4H50Q8FH3";
		String conversion = (String) CommandLineParser.convert(valueAsString, String.class);
		Assert.assertEquals(valueAsString, conversion);
	}
	
	@Test
	public void testStringArray(){
		String[] value = {
				"Q87J0D4J034J50FQ",
				"98W4Q05JF9045F",
				"0Q8FH7085",
		};
		String valueAsString = Arrays.toString(value);
		String[] conversion = (String[]) CommandLineParser.convert(valueAsString, String[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i]);
		}
	}
	

	@Test
	public void testURI(){
		URI value = URI.create("test://www.tester.org?q=which#header");
		String valueAsString = "" + value.toASCIIString();
		URI conversion = (URI) CommandLineParser.convert(valueAsString, URI.class);
		Assert.assertEquals(value, conversion);
	}
	
	@Test
	public void testURIArray(){
		URI[] value = {
				URI.create("test://www.tester.org?q=which#header"),
				URI.create("opaque:uri"),
		};
		String valueAsString = Arrays.toString(value);
		URI[] conversion = (URI[]) CommandLineParser.convert(valueAsString, URI[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i]);
		}
	}
	
	@Test
	public void testBoolean(){
		Boolean value = Boolean.valueOf(new Random().nextBoolean());
		String valueAsString = value.toString();
		Boolean conversion = (Boolean) CommandLineParser.convert(valueAsString, Boolean.class);
		Assert.assertEquals(value, conversion);
	}
	
	@Test
	public void testBooleanArray(){
		boolean[] value = {
				(new Random().nextBoolean()),
				(new Random().nextBoolean()),
				(new Random().nextBoolean()),
				(new Random().nextBoolean())
		};
		String valueAsString = Arrays.toString(value);
		boolean[] conversion = (boolean[]) CommandLineParser.convert(valueAsString, boolean[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i]);
		}
	}
	

	@Test
	public void testFloat(){
		float value = (float) (Math.random()*10000);
		String valueAsString = "" + value;
		Float conversion = (Float) CommandLineParser.convert(valueAsString, Float.class);
		Assert.assertEquals(value, conversion.floatValue(), 0.00001);
	}
	
	@Test
	public void testFloatArray(){
		float[] value = {
				(float) (Math.random()*254),
				(float) (Math.random()*254),
				(float) (Math.random()*254),
		};
		String valueAsString = Arrays.toString(value);
		float[] conversion = (float[]) CommandLineParser.convert(valueAsString, float[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i], 0.00001);
		}
	}


	@Test
	public void testDouble(){
		double value = Math.random()*10000;
		String valueAsString = "" + value;
		Double conversion = (Double) CommandLineParser.convert(valueAsString, Double.class);
		Assert.assertEquals(value, conversion.doubleValue(), 0.00001);
	}
	
	@Test
	public void testDoubleArray(){
		double[] value = {
				Math.random()*254,
				Math.random()*254,
				Math.random()*254,
		};
		String valueAsString = Arrays.toString(value);
		double[] conversion = (double[]) CommandLineParser.convert(valueAsString, double[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i], 0.00001);
		}
	}


	@Test
	public void testShort(){
		short value = (short) (Math.random()*100000);
		String valueAsString = "" + value;
		Short conversion = (Short) CommandLineParser.convert(valueAsString, Short.class);
		Assert.assertEquals(value, conversion.shortValue());
	}
	
	@Test
	public void testShortArray(){
		short[] value = {
				(short) (Math.random()*10000000),
				(short) (Math.random()*10000000),
				(short) (Math.random()*10000000),
		};
		String valueAsString = Arrays.toString(value);
		short[] conversion = (short[]) CommandLineParser.convert(valueAsString, short[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i]);
		}
	}
	

	@Test
	public void testInt(){
		int value = (int) (Math.random()*10000000);
		String valueAsString = "" + value;
		Integer conversion = (Integer) CommandLineParser.convert(valueAsString, Integer.class);
		Assert.assertEquals(value, conversion.intValue());
	}
	
	@Test
	public void testIntArray(){
		int[] value = {
				(int) (Math.random()*10000000),
				(int) (Math.random()*10000000),
				(int) (Math.random()*10000000),
				(int) (Math.random()*10000000),
				(int) (Math.random()*10000000),
				(int) (Math.random()*10000000),
				(int) (Math.random()*10000000),
		};
		String valueAsString = Arrays.toString(value);
		int[] conversion = (int[]) CommandLineParser.convert(valueAsString, int[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i]);
		}
	}
	

	@Test
	public void testLong(){
		long value = (long) (Math.random()*10000000);
		String valueAsString = "" + value;
		Long conversion = (Long) CommandLineParser.convert(valueAsString, Long.class);
		Assert.assertEquals(value, conversion.longValue());
	}
	
	@Test
	public void testLongArray(){
		long[] value = {
				(long) (Math.random()*10000000),
				(long) (Math.random()*10000000),
		
		};
		String valueAsString = Arrays.toString(value);
		long[] conversion = (long[]) CommandLineParser.convert(valueAsString, long[].class);
		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(value[i], conversion[i]);
		}
	}

	@Test
	public void testGenericObject(){
		// only works, as URIs are not supported built-in
		AbstractA[] target = {
				AbstractA.convert("aimpl"),
				AbstractA.convert("aimpl"),		
		};
		String[] from = {
				"aimpl",
				"aimpl",		
		};
		
		AbstractA[] conversion = (AbstractA[]) CommandLineParser.convert(from, AbstractA[].class);
//		System.out.println(Arrays.);

		for (int i = 0; i < conversion.length; i++) {
			Assert.assertEquals(target[i].getAbstracta(), conversion[i].getAbstracta());
			Assert.assertEquals(target[i].getClass(), conversion[i].getClass());
			Assert.assertTrue(target[i].equals(conversion[i]));
		}
	}

//	@Test
//	public void testFile(){
//		File value = new File("/tmp");
//		String valueAsString = "" + value.toString();
//		File conversion = (File) CommandLineParser.convert(valueAsString, File.class);
//		Assert.assertEquals(value, conversion);
//	}
	
//	@Test
//	public void testFileArray(){
//		File[] value = {
//				new File("/tmp"),
//				new File("/non-existing"),
//		};
//		String valueAsString = Arrays.toString(value);
//		File[] conversion = (File[]) CommandLineParser.convert(valueAsString, File[].class);
//		Assert.assertArrayEquals(value, conversion);
//		for (int i = 0; i < conversion.length; i++) {
//			Assert.assertEquals(value[i], conversion[i]);
//		}
//	}
	
}
