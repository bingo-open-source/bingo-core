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
import bingo.lang.testing.junit.ConcurrentTestCase;

@SuppressWarnings({"unused"})
public class ReflectsTest extends ConcurrentTestCase {

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
