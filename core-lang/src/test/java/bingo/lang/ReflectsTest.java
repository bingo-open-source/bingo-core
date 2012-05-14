/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import bingo.lang.testbed.beans.TestBean;
import bingo.lang.testing.Perf;

@SuppressWarnings({"unchecked","unused"})
public class ReflectsTest {

	@Test
	public void testFindField() {
		Field field = Reflects.findField(TestBeanSubclassWithPublicField.class, "publicField", String.class);
		assertNotNull(field);
		assertEquals("publicField", field.getName());
		assertEquals(String.class, field.getType());
		assertTrue("Field should be public.", Modifier.isPublic(field.getModifiers()));

		field = Reflects.findField(TestBeanSubclassWithNewField.class, "prot", String.class);
		assertNotNull(field);
		assertEquals("prot", field.getName());
		assertEquals(String.class, field.getType());
		assertTrue("Field should be protected.", Modifier.isProtected(field.getModifiers()));

		field = Reflects.findField(TestBeanSubclassWithNewField.class, "name", String.class);
		assertNotNull(field);
		assertEquals("name", field.getName());
		assertEquals(String.class, field.getType());
		assertTrue("Field should be private.", Modifier.isPrivate(field.getModifiers()));
	}
	
	@Test
	public void testFindMethod() throws Exception {
		assertNotNull(Reflects.findMethod(B.class, "bar", String.class));
		assertNotNull(Reflects.findMethod(B.class, "foo", Integer.class));
		assertNotNull(Reflects.findMethod(B.class, "getClass"));
	}

	@Test
	public void testGetTypeArgument() throws Exception{
		 assertEquals(Object.class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list0").getGenericType()));
		 assertEquals(String.class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list1").getGenericType()));
		 assertEquals(List.class, 	 Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list2").getGenericType()));
		 assertEquals(List.class, 	 Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list3").getGenericType()));
		 assertEquals(Map.class, 	 Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list4").getGenericType()));
		 assertEquals(Object.class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list5").getGenericType()));
		 assertEquals(Object.class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list6").getGenericType()));
		 assertEquals(StringChild.class, Reflects.getTypeArgument(GenericContainer1.class.getDeclaredField("list5").getGenericType()));
		 assertEquals(StringChild.class, Reflects.getTypeArgument(GenericContainer1.class.getDeclaredField("list6").getGenericType()));
		 assertEquals(Object.class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list7").getGenericType()));
		 assertEquals(String[].class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("list8").getGenericType()));
		 assertEquals(Object.class,Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("field0").getGenericType()));
		 assertEquals(Object.class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("child1").getGenericType()));
		 assertEquals(String.class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("child2").getGenericType()));
		 assertEquals(StringChild.class, Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("child3").getGenericType()));
		 assertEquals(Object.class,Reflects.getTypeArgument(GenericContainer.class.getDeclaredField("child4").getGenericType()));
	}
	
	@Test
	public void testGetTypeArgumentPerformance() throws Exception {
		 final Type genericType = GenericContainer.class.getDeclaredField("child4").getGenericType();
		 
		 Perf.run("Types.getTypeArgument", new Runnable() {
			public void run() {
				Reflects.getTypeArgument(genericType);
			}
		},1000000);
	}
	
	private static class GenericContainer<T> {
		
		private List<?> list0;
		
		private List<String> list1;
		
		private List<List<?>> list2;
		
		private List<List<String>> list3;
		
		private List<Map<?, ?>> list4;
		
		private List<T> list5;
		
		private List<? extends T> list6;
		
		private List list7;
		
		private List<String[]> list8;
		
		private T field0;
		
		private String field1;
		
		private StringChild child0;
		
		private GenericParent<?> child1;
		
		private GenericParent<String> child2;
		
		private GenericContainer1 child3;
		
		private StringChildI child4;
		
	}
	
	private static class GenericContainer1<T extends StringChild> {
		
		private List<T> list5;
		
		private List<? extends T> list6;		
		
		private T field0;
	}	
	
	private static interface IGenericParent<T> {
		
		
	}

	private static class GenericParent<T> {
		
		
	}
	
	private static class StringChild extends GenericParent<String> {
		
	}
	
	private static class StringChildI implements IGenericParent<String> {
		
	}	

	private static class TestBeanSubclass extends TestBean {

		@Override
		public void absquatulate() {
			throw new UnsupportedOperationException();
		}
	}

	private static class TestBeanSubclassWithPublicField extends TestBean {

		public String publicField = "foo";
	}

	private static class TestBeanSubclassWithNewField extends TestBean {

		private int magic;

		protected String prot = "foo";
	}

	private static class TestBeanSubclassWithFinalField extends TestBean {

		private final String foo = "will break naive copy that doesn't exclude statics";
	}

	private static class A {

		private void foo(Integer i) throws RemoteException {
		}
	}

	private static class B extends A {

		void bar(String s) throws IllegalArgumentException {
		}

		int add(int... args) {
			int sum = 0;
			for (int i = 0; i < args.length; i++) {
				sum += args[i];
			}
			return sum;
		}
	}
}
