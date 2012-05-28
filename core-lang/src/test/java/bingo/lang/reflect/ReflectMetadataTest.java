package bingo.lang.reflect;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

@SuppressWarnings("unused")
public class ReflectMetadataTest {
	
	@Test
	public void testGetParameterNamesConstructor() {
		ReflectClass<ConstructorParameterNames> rc = ReflectClass.get(ConstructorParameterNames.class);
		
		ReflectConstructor<ConstructorParameterNames> c1 = rc.getConstructor();
		
		assertNotNull(c1);
		assertNotNull(c1.getParameters());
		assertEquals(0,c1.getParameters().length);
		
		ReflectConstructor<ConstructorParameterNames> c2 = rc.getConstructor(int.class,String.class,List.class);
		assertNotNull(c2);
		assertEquals(3,c2.getParameters().length);
		
		ReflectParameter param1 = c2.getParameters()[0];
		ReflectParameter param2 = c2.getParameters()[1];
		ReflectParameter param3 = c2.getParameters()[2];
		
		assertEquals(1,    param1.getIndex());
		assertEquals("i1", param1.getName());
		
		assertEquals(2,    param2.getIndex());
		assertEquals("s2", param2.getName());
		
		assertEquals(3,    param3.getIndex());
		assertEquals("l99", param3.getName());
	}
	
	private static final class ConstructorParameterNames {
		
		public ConstructorParameterNames(){
			
		}
		
		public ConstructorParameterNames(int i1,String s2,List<String> l99){
			
		}
		
	}
}
