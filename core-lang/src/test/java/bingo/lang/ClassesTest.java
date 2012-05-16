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

import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassesTest {

	@Test
	public void testScan(){
		Set<Class<?>> classes = Classes.scan("bingo.lang","*Test");
		
		assertNotNull(classes);
		assertFalse(classes.isEmpty());
		
		for(Class<?> clazz : classes){
			assertTrue(clazz.getName().endsWith("Test"));
			assertTrue(clazz.getPackage().getName().equals("bingo.lang"));
		}
		
		Set<Class<?>> classes1 = Classes.scan("bingo.lang", "*");
		
		assertTrue(classes1.containsAll(classes));
		assertTrue(classes1.size() > classes.size());
	}
	
	@Test
	public void testGetFileName(){
		assertEquals("String.class", Classes.getFileName(String.class));
		assertEquals("String.class", Classes.getFileName(java.lang.String.class));
		assertNotSame("Classes", Classes.getFileName(Classes.class));
		assertNotSame("Classes", Classes.getFileName(bingo.lang.Classes.class));
	}
	
}
