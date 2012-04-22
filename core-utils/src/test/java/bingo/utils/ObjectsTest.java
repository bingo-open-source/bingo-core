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
package bingo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import static org.junit.Assert.*;

import org.junit.Test;


import bingo.lang.exceptions.CloneException;
import bingo.utils.mutable.MutableObject;

/**
 * {@link TestCase} of {@link Objects}
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
public class ObjectsTest {

	@Test
	public void testHashCode() {
		assertEquals(0, Objects.hashCode(null));
		assertEquals("a".hashCode(), Objects.hashCode("a"));
	}

	@Test
	public void testHashCodeMulti_multiple_emptyArray() {
		Object[] array = new Object[0];
		assertEquals(1, Objects.hashCodeMulti(array));
	}

	@Test
	public void testHashCodeMulti_multiple_nullArray() {
		Object[] array = null;
		assertEquals(1, Objects.hashCodeMulti(array));
	}

	@Test
	public void testHashCodeMulti_multiple_likeList() {
		List<Object> list0 = new ArrayList<Object>(Arrays.asList());
		assertEquals(list0.hashCode(), Objects.hashCodeMulti());

		List<Object> list1 = new ArrayList<Object>(Arrays.asList("a"));
		assertEquals(list1.hashCode(), Objects.hashCodeMulti("a"));

		List<Object> list2 = new ArrayList<Object>(Arrays.asList("a", "b"));
		assertEquals(list2.hashCode(), Objects.hashCodeMulti("a", "b"));

		List<Object> list3 = new ArrayList<Object>(Arrays.asList("a", "b", "c"));
		assertEquals(list3.hashCode(), Objects.hashCodeMulti("a", "b", "c"));
	}
	
    @Test
    public void testCloneOfCloneable() {
        final CloneableString string = new CloneableString("apache");
        final CloneableString stringClone = Objects.clone(string);
        assertEquals("apache", stringClone.getValue());
    }

    @Test
    public void testCloneOfNotCloneable() {
        final String string = new String("apache");
        assertNull(Objects.clone(string));
    }

    @Test(expected = NoSuchMethodException.class)
    public void testCloneOfUncloneable() throws Throwable {
        final UncloneableString string = new UncloneableString("apache");
        try {
            Objects.clone(string);
            fail("Thrown " + CloneException.class.getName() + " expected");
        } catch (final CloneException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testCloneOfStringArray() {
        assertTrue(Arrays.deepEquals(
            new String[]{"string"}, Objects.clone(new String[]{"string"})));
    }

    @Test
    public void testCloneOfPrimitiveArray() {
        assertTrue(Arrays.equals(new int[]{1}, Objects.clone(new int[]{1})));
    }	

	/**
	 * String that is cloneable.
	 */
	static final class CloneableString extends MutableObject<String> implements Cloneable {
		private static final long	serialVersionUID	= 1L;

		CloneableString(final String s) {
			super(s);
		}

		@Override
		public CloneableString clone() throws CloneNotSupportedException {
			return (CloneableString) super.clone();
		}
	}

	/**
	 * String that is not cloneable.
	 */
	static final class UncloneableString extends MutableObject<String> implements Cloneable {
		private static final long	serialVersionUID	= 1L;

		UncloneableString(final String s) {
			super(s);
		}
	}
}
