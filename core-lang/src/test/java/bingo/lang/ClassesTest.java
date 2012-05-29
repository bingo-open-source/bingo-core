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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import bingo.lang.testing.junit.Concurrent;
import bingo.lang.testing.junit.ConcurrentTestCase;

@Concurrent(threads=5)
public class ClassesTest extends ConcurrentTestCase{

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
	
	@Test
    public void test_isAssignable_DefaultUnboxing_Widening() throws Exception {
        boolean autoboxing = true;

        // test byte conversions
        assertFalse("byte -> char", Classes.isAssignable(Byte.class, Character.TYPE));
        assertEquals("byte -> byte", autoboxing, Classes.isAssignable(Byte.class, Byte.TYPE));
        assertEquals("byte -> short", autoboxing, Classes.isAssignable(Byte.class, Short.TYPE));
        assertEquals("byte -> int", autoboxing, Classes.isAssignable(Byte.class, Integer.TYPE));
        assertEquals("byte -> long", autoboxing, Classes.isAssignable(Byte.class, Long.TYPE));
        assertEquals("byte -> float", autoboxing, Classes.isAssignable(Byte.class, Float.TYPE));
        assertEquals("byte -> double", autoboxing, Classes.isAssignable(Byte.class, Double.TYPE));
        assertFalse("byte -> boolean", Classes.isAssignable(Byte.class, Boolean.TYPE));

        // test short conversions
        assertFalse("short -> char", Classes.isAssignable(Short.class, Character.TYPE));
        assertFalse("short -> byte", Classes.isAssignable(Short.class, Byte.TYPE));
        assertEquals("short -> short", autoboxing, Classes.isAssignable(Short.class, Short.TYPE));
        assertEquals("short -> int", autoboxing, Classes.isAssignable(Short.class, Integer.TYPE));
        assertEquals("short -> long", autoboxing, Classes.isAssignable(Short.class, Long.TYPE));
        assertEquals("short -> float", autoboxing, Classes.isAssignable(Short.class, Float.TYPE));
        assertEquals("short -> double", autoboxing, Classes.isAssignable(Short.class, Double.TYPE));
        assertFalse("short -> boolean", Classes.isAssignable(Short.class, Boolean.TYPE));

        // test char conversions
        assertEquals("char -> char", autoboxing, Classes.isAssignable(Character.class, Character.TYPE));
        assertFalse("char -> byte", Classes.isAssignable(Character.class, Byte.TYPE));
        assertFalse("char -> short", Classes.isAssignable(Character.class, Short.TYPE));
        assertEquals("char -> int", autoboxing, Classes.isAssignable(Character.class, Integer.TYPE));
        assertEquals("char -> long", autoboxing, Classes.isAssignable(Character.class, Long.TYPE));
        assertEquals("char -> float", autoboxing, Classes.isAssignable(Character.class, Float.TYPE));
        assertEquals("char -> double", autoboxing, Classes.isAssignable(Character.class, Double.TYPE));
        assertFalse("char -> boolean", Classes.isAssignable(Character.class, Boolean.TYPE));

        // test int conversions
        assertFalse("int -> char", Classes.isAssignable(Integer.class, Character.TYPE));
        assertFalse("int -> byte", Classes.isAssignable(Integer.class, Byte.TYPE));
        assertFalse("int -> short", Classes.isAssignable(Integer.class, Short.TYPE));
        assertEquals("int -> int", autoboxing, Classes.isAssignable(Integer.class, Integer.TYPE));
        assertEquals("int -> long", autoboxing, Classes.isAssignable(Integer.class, Long.TYPE));
        assertEquals("int -> float", autoboxing, Classes.isAssignable(Integer.class, Float.TYPE));
        assertEquals("int -> double", autoboxing, Classes.isAssignable(Integer.class, Double.TYPE));
        assertFalse("int -> boolean", Classes.isAssignable(Integer.class, Boolean.TYPE));

        // test long conversions
        assertFalse("long -> char", Classes.isAssignable(Long.class, Character.TYPE));
        assertFalse("long -> byte", Classes.isAssignable(Long.class, Byte.TYPE));
        assertFalse("long -> short", Classes.isAssignable(Long.class, Short.TYPE));
        assertFalse("long -> int", Classes.isAssignable(Long.class, Integer.TYPE));
        assertEquals("long -> long", autoboxing, Classes.isAssignable(Long.class, Long.TYPE));
        assertEquals("long -> float", autoboxing, Classes.isAssignable(Long.class, Float.TYPE));
        assertEquals("long -> double", autoboxing, Classes.isAssignable(Long.class, Double.TYPE));
        assertFalse("long -> boolean", Classes.isAssignable(Long.class, Boolean.TYPE));

        // test float conversions
        assertFalse("float -> char", Classes.isAssignable(Float.class, Character.TYPE));
        assertFalse("float -> byte", Classes.isAssignable(Float.class, Byte.TYPE));
        assertFalse("float -> short", Classes.isAssignable(Float.class, Short.TYPE));
        assertFalse("float -> int", Classes.isAssignable(Float.class, Integer.TYPE));
        assertFalse("float -> long", Classes.isAssignable(Float.class, Long.TYPE));
        assertEquals("float -> float", autoboxing, Classes.isAssignable(Float.class, Float.TYPE));
        assertEquals("float -> double", autoboxing, Classes.isAssignable(Float.class, Double.TYPE));
        assertFalse("float -> boolean", Classes.isAssignable(Float.class, Boolean.TYPE));

        // test double conversions
        assertFalse("double -> char", Classes.isAssignable(Double.class, Character.TYPE));
        assertFalse("double -> byte", Classes.isAssignable(Double.class, Byte.TYPE));
        assertFalse("double -> short", Classes.isAssignable(Double.class, Short.TYPE));
        assertFalse("double -> int", Classes.isAssignable(Double.class, Integer.TYPE));
        assertFalse("double -> long", Classes.isAssignable(Double.class, Long.TYPE));
        assertFalse("double -> float", Classes.isAssignable(Double.class, Float.TYPE));
        assertEquals("double -> double", autoboxing, Classes.isAssignable(Double.class, Double.TYPE));
        assertFalse("double -> boolean", Classes.isAssignable(Double.class, Boolean.TYPE));

        // test boolean conversions
        assertFalse("boolean -> char", Classes.isAssignable(Boolean.class, Character.TYPE));
        assertFalse("boolean -> byte", Classes.isAssignable(Boolean.class, Byte.TYPE));
        assertFalse("boolean -> short", Classes.isAssignable(Boolean.class, Short.TYPE));
        assertFalse("boolean -> int", Classes.isAssignable(Boolean.class, Integer.TYPE));
        assertFalse("boolean -> long", Classes.isAssignable(Boolean.class, Long.TYPE));
        assertFalse("boolean -> float", Classes.isAssignable(Boolean.class, Float.TYPE));
        assertFalse("boolean -> double", Classes.isAssignable(Boolean.class, Double.TYPE));
        assertEquals("boolean -> boolean", autoboxing, Classes.isAssignable(Boolean.class, Boolean.TYPE));
    }	
	
	@Test
    public void testIsPrimitiveOrWrapper() {

        // test primitive wrapper classes
        assertTrue("Boolean.class", Classes.isPrimitiveOrWrapper(Boolean.class));
        assertTrue("Byte.class", Classes.isPrimitiveOrWrapper(Byte.class));
        assertTrue("Character.class", Classes.isPrimitiveOrWrapper(Character.class));
        assertTrue("Short.class", Classes.isPrimitiveOrWrapper(Short.class));
        assertTrue("Integer.class", Classes.isPrimitiveOrWrapper(Integer.class));
        assertTrue("Long.class", Classes.isPrimitiveOrWrapper(Long.class));
        assertTrue("Double.class", Classes.isPrimitiveOrWrapper(Double.class));
        assertTrue("Float.class", Classes.isPrimitiveOrWrapper(Float.class));
        
        // test primitive classes
        assertTrue("boolean", Classes.isPrimitiveOrWrapper(Boolean.TYPE));
        assertTrue("byte", Classes.isPrimitiveOrWrapper(Byte.TYPE));
        assertTrue("char", Classes.isPrimitiveOrWrapper(Character.TYPE));
        assertTrue("short", Classes.isPrimitiveOrWrapper(Short.TYPE));
        assertTrue("int", Classes.isPrimitiveOrWrapper(Integer.TYPE));
        assertTrue("long", Classes.isPrimitiveOrWrapper(Long.TYPE));
        assertTrue("double", Classes.isPrimitiveOrWrapper(Double.TYPE));
        assertTrue("float", Classes.isPrimitiveOrWrapper(Float.TYPE));
        assertTrue("Void.TYPE", Classes.isPrimitiveOrWrapper(Void.TYPE));
        
        // others
        assertFalse("null", Classes.isPrimitiveOrWrapper(null));
        assertFalse("Void.class", Classes.isPrimitiveOrWrapper(Void.class));
        assertFalse("String.class", Classes.isPrimitiveOrWrapper(String.class));
        assertFalse("this.getClass()", Classes.isPrimitiveOrWrapper(this.getClass()));
    }
    
	@Test
    public void testIsPrimitiveWrapper() {

        // test primitive wrapper classes
        assertTrue("Boolean.class", Classes.isPrimitiveWrapper(Boolean.class));
        assertTrue("Byte.class", Classes.isPrimitiveWrapper(Byte.class));
        assertTrue("Character.class", Classes.isPrimitiveWrapper(Character.class));
        assertTrue("Short.class", Classes.isPrimitiveWrapper(Short.class));
        assertTrue("Integer.class", Classes.isPrimitiveWrapper(Integer.class));
        assertTrue("Long.class", Classes.isPrimitiveWrapper(Long.class));
        assertTrue("Double.class", Classes.isPrimitiveWrapper(Double.class));
        assertTrue("Float.class", Classes.isPrimitiveWrapper(Float.class));
        
        // test primitive classes
        assertFalse("boolean", Classes.isPrimitiveWrapper(Boolean.TYPE));
        assertFalse("byte", Classes.isPrimitiveWrapper(Byte.TYPE));
        assertFalse("char", Classes.isPrimitiveWrapper(Character.TYPE));
        assertFalse("short", Classes.isPrimitiveWrapper(Short.TYPE));
        assertFalse("int", Classes.isPrimitiveWrapper(Integer.TYPE));
        assertFalse("long", Classes.isPrimitiveWrapper(Long.TYPE));
        assertFalse("double", Classes.isPrimitiveWrapper(Double.TYPE));
        assertFalse("float", Classes.isPrimitiveWrapper(Float.TYPE));
        
        // others
        assertFalse("null", Classes.isPrimitiveWrapper(null));
        assertFalse("Void.class", Classes.isPrimitiveWrapper(Void.class));
        assertFalse("Void.TYPE", Classes.isPrimitiveWrapper(Void.TYPE));
        assertFalse("String.class", Classes.isPrimitiveWrapper(String.class));
        assertFalse("this.getClass()", Classes.isPrimitiveWrapper(this.getClass()));
    }
    
	@Test
    public void testPrimitiveToWrapper() {

        // test primitive classes
        assertEquals("boolean -> Boolean.class",
            Boolean.class, Classes.primitiveToWrapper(Boolean.TYPE));
        assertEquals("byte -> Byte.class",
            Byte.class, Classes.primitiveToWrapper(Byte.TYPE));
        assertEquals("char -> Character.class",
            Character.class, Classes.primitiveToWrapper(Character.TYPE));
        assertEquals("short -> Short.class",
            Short.class, Classes.primitiveToWrapper(Short.TYPE));
        assertEquals("int -> Integer.class",
            Integer.class, Classes.primitiveToWrapper(Integer.TYPE));
        assertEquals("long -> Long.class",
            Long.class, Classes.primitiveToWrapper(Long.TYPE));
        assertEquals("double -> Double.class",
            Double.class, Classes.primitiveToWrapper(Double.TYPE));
        assertEquals("float -> Float.class",
            Float.class, Classes.primitiveToWrapper(Float.TYPE));

        // test a few other classes
        assertEquals("String.class -> String.class",
            String.class, Classes.primitiveToWrapper(String.class));
        assertEquals("Classes.class -> Classes.class",
            Classes.class,
            Classes.primitiveToWrapper(Classes.class));
        assertEquals("Void.TYPE -> Void.TYPE",
            Void.TYPE, Classes.primitiveToWrapper(Void.TYPE));

        // test null
        assertNull("null -> null",
            Classes.primitiveToWrapper(null));
    }

	@Test
    public void testWrapperToPrimitive() {
        // an array with classes to convert
        final Class<?>[] primitives = {
                Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE,
                Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE
        };
        for (Class<?> primitive : primitives) {
            Class<?> wrapperCls = Classes.primitiveToWrapper(primitive);
            assertFalse("Still primitive", wrapperCls.isPrimitive());
            assertEquals(wrapperCls + " -> " + primitive, primitive,
                    Classes.wrapperToPrimitive(wrapperCls));
        }
    }

	@Test
    public void testWrapperToPrimitiveNoWrapper() {
        assertEquals("Wrong result for non wrapper class",String.class, Classes.wrapperToPrimitive(String.class));
    }

	@Test
    public void testWrapperToPrimitiveNull() {
        assertNull("Wrong result for null class", Classes.wrapperToPrimitive(null));
    }
}
