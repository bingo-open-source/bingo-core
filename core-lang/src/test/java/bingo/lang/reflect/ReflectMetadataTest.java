package bingo.lang.reflect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReflectMetadataTest {
	
	@Test
	public void testGetParameterNamesMethod() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParameterNamesConstructor() {
		ReflectMetadata rm = new ReflectMetadata(String.class);
		String[] names = rm.getParameterNames(String.class.getConstructors()[1]);
		System.out.println(names);
	}

}
