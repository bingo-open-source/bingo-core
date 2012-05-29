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

import static bingo.lang.StringsTest.FOO;

import org.junit.Test;

import bingo.lang.testing.junit.ConcurrentTestCase;
import junit.framework.TestCase;

import static org.junit.Assert.*;

/**
 * {@link TestCase} of {@link Strings}
 */
public class StringsEmptyBlankTrimTest extends ConcurrentTestCase {
	
	@Test
	public void testIsEmpty() {
		assertEquals(true, Strings.isEmpty(null));
		assertEquals(true, Strings.isEmpty(""));
		assertEquals(false, Strings.isEmpty(" "));
		assertEquals(false, Strings.isEmpty("foo"));
		assertEquals(false, Strings.isEmpty("  foo  "));
	}
	
	@Test
	public void testIsNotEmpty() {
		assertEquals(false, Strings.isNotEmpty(null));
		assertEquals(false, Strings.isNotEmpty(""));
		assertEquals(true, Strings.isNotEmpty(" "));
		assertEquals(true, Strings.isNotEmpty("foo"));
		assertEquals(true, Strings.isNotEmpty("  foo  "));
	}

	@Test
	public void testIsBlank() {
		assertEquals(true, Strings.isBlank(null));
		assertEquals(true, Strings.isBlank(""));
		assertEquals(true, Strings.isBlank(" "));
		assertEquals(false, Strings.isBlank("foo"));
		assertEquals(false, Strings.isBlank("  foo  "));
	}

	@Test
	public void testIsNotBlank() {
		assertEquals(false, Strings.isNotBlank(null));
		assertEquals(false, Strings.isNotBlank(""));
		assertEquals(false, Strings.isNotBlank(StringsTest.WHITESPACE));
		assertEquals(true, Strings.isNotBlank("foo"));
		assertEquals(true, Strings.isNotBlank("  foo  "));
	}
	
	@Test
    public void testTrim() {
        assertEquals(FOO, Strings.trimOrNull(FOO + "  "));
        assertEquals(FOO, Strings.trimOrNull(" " + FOO + "  "));
        assertEquals(FOO, Strings.trimOrNull(" " + FOO));
        assertEquals(FOO, Strings.trimOrNull(FOO + ""));
        assertEquals("", Strings.trimOrNull(" \t\r\n\b "));
        assertEquals("", Strings.trimOrNull(StringsTest.TRIMMABLE));
        assertEquals(StringsTest.NON_TRIMMABLE, Strings.trimOrNull(StringsTest.NON_TRIMMABLE));
        assertEquals("", Strings.trimOrNull(""));
        assertEquals(null, Strings.trimOrNull(null));
    }

	@Test
    public void testTrimEmptyToNull() {
        assertEquals(FOO, Strings.trimToNull(FOO + "  "));
        assertEquals(FOO, Strings.trimToNull(" " + FOO + "  "));
        assertEquals(FOO, Strings.trimToNull(" " + FOO));
        assertEquals(FOO, Strings.trimToNull(FOO + ""));
        assertEquals(null, Strings.trimToNull(" \t\r\n\b "));
        assertEquals(null, Strings.trimToNull(StringsTest.TRIMMABLE));
        assertEquals(StringsTest.NON_TRIMMABLE, Strings.trimToNull(StringsTest.NON_TRIMMABLE));
        assertEquals(null, Strings.trimToNull(""));
        assertEquals(null, Strings.trimToNull(null));
    }

	@Test
    public void testTrimNullToEmpty() {
        assertEquals(FOO, Strings.trim(FOO + "  "));
        assertEquals(FOO, Strings.trim(" " + FOO + "  "));
        assertEquals(FOO, Strings.trim(" " + FOO));
        assertEquals(FOO, Strings.trim(FOO + ""));
        assertEquals("", Strings.trim(" \t\r\n\b "));
        assertEquals("", Strings.trim(StringsTest.TRIMMABLE));
        assertEquals(StringsTest.NON_TRIMMABLE, Strings.trim(StringsTest.NON_TRIMMABLE));
        assertEquals("", Strings.trim(""));
        assertEquals("", Strings.trim(null));
    }
    
	@Test
    public void testTrim_StringChars() {
        // null trim
        assertEquals("", Strings.trim(null, null));
        assertEquals("", Strings.trim("", null));
        assertEquals("", Strings.trim("        ", null));
        assertEquals("abc", Strings.trim("  abc  ", null));
        assertEquals(StringsTest.NON_TRIMMABLE, 
            Strings.trim(StringsTest.TRIMMABLE + StringsTest.NON_TRIMMABLE + StringsTest.TRIMMABLE, null));

        // [] trim
        assertEquals("", Strings.trim(null, Arrays.EMPTY_CHAR_ARRAY));
        assertEquals("        ", Strings.trim("        ", Arrays.EMPTY_CHAR_ARRAY));
        assertEquals("  abc  ", Strings.trim("  abc  ", Arrays.EMPTY_CHAR_ARRAY));
        assertEquals(StringsTest.WHITESPACE, Strings.trim(StringsTest.WHITESPACE, Arrays.EMPTY_CHAR_ARRAY));
        
        // " " trim
        assertEquals("", Strings.trim(null, ' '));
        assertEquals("", Strings.trim("", ' '));
        assertEquals("", Strings.trim("        ", ' '));
        assertEquals("abc", Strings.trim("  abc  ", ' '));
        
        //  'a','b' trim
        assertEquals("", Strings.trim(null, 'a','b'));
        assertEquals("", Strings.trim("",  'a','b'));
        assertEquals("        ", Strings.trim("        ",  'a','b'));
        assertEquals("  abc  ", Strings.trim("  abc  ",  'a','b'));
        assertEquals("c", Strings.trim("abcabab",  'a','b'));
        assertEquals(StringsTest.WHITESPACE, Strings.trim(StringsTest.WHITESPACE, Arrays.EMPTY_CHAR_ARRAY));
    }
    
	@Test
    public void testtrimStart_StringString() {
        // null trimStart
        assertEquals("", Strings.trimStart(null, null));
        assertEquals("", Strings.trimStart("", null));
        assertEquals("", Strings.trimStart("        ", null));
        assertEquals("abc  ", Strings.trimStart("  abc  ", null));
        assertEquals(StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE, 
            Strings.trimStart(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE, null));

        // "" trimStart
        assertEquals("", Strings.trimStart(null, Arrays.EMPTY_CHAR_ARRAY));
        assertEquals("", Strings.trimStart("", Arrays.EMPTY_CHAR_ARRAY));
        assertEquals("        ", Strings.trimStart("        ", Arrays.EMPTY_CHAR_ARRAY));
        assertEquals("  abc  ", Strings.trimStart("  abc  ", Arrays.EMPTY_CHAR_ARRAY));
        assertEquals(StringsTest.WHITESPACE, Strings.trimStart(StringsTest.WHITESPACE, Arrays.EMPTY_CHAR_ARRAY));
        
        // " " trimStart
        assertEquals("", Strings.trimStart(null, ' '));
        assertEquals("", Strings.trimStart("", ' '));
        assertEquals("", Strings.trimStart("        ", ' '));
        assertEquals("abc  ", Strings.trimStart("  abc  ", ' '));
        
        //  'a','b' trimStart
        assertEquals("", Strings.trimStart(null,  'a','b'));
        assertEquals("", Strings.trimStart("",  'a','b'));
        assertEquals("        ", Strings.trimStart("        ",  'a','b'));
        assertEquals("  abc  ", Strings.trimStart("  abc  ",  'a','b'));
        assertEquals("cabab", Strings.trimStart("abcabab",  'a','b'));
        assertEquals(StringsTest.WHITESPACE, Strings.trimStart(StringsTest.WHITESPACE, Arrays.EMPTY_CHAR_ARRAY));
    }
    
	@Test
    public void testtrimEnd_StringString() {
        // null trimEnd
        assertEquals("", Strings.trimEnd(null, null));
        assertEquals("", Strings.trimEnd("", null));
        assertEquals("", Strings.trimEnd("        ", null));
        assertEquals("  abc", Strings.trimEnd("  abc  ", null));
        assertEquals(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE, 
            Strings.trimEnd(StringsTest.WHITESPACE + StringsTest.NON_WHITESPACE + StringsTest.WHITESPACE, null));

        // "" trimEnd
        assertEquals("", Strings.trimEnd(null, Arrays.EMPTY_CHAR_ARRAY));
        assertEquals("", Strings.trimEnd("", Arrays.EMPTY_CHAR_ARRAY));
        assertEquals("        ", Strings.trimEnd("        ", Arrays.EMPTY_CHAR_ARRAY));
        assertEquals("  abc  ", Strings.trimEnd("  abc  ", Arrays.EMPTY_CHAR_ARRAY));
        assertEquals(StringsTest.WHITESPACE, Strings.trimEnd(StringsTest.WHITESPACE, Arrays.EMPTY_CHAR_ARRAY));
        
        // ' ' trimEnd
        assertEquals("", Strings.trimEnd(null, ' '));
        assertEquals("", Strings.trimEnd("", ' '));
        assertEquals("", Strings.trimEnd("        ", ' '));
        assertEquals("  abc", Strings.trimEnd("  abc  ", ' '));
        
        //  'a','b' trimEnd
        assertEquals("", Strings.trimEnd(null,  'a','b'));
        assertEquals("", Strings.trimEnd("",  'a','b'));
        assertEquals("        ", Strings.trimEnd("        ",  'a','b'));
        assertEquals("  abc  ", Strings.trimEnd("  abc  ",  'a','b'));
        assertEquals("abc", Strings.trimEnd("abcabab",  'a','b'));
        assertEquals(StringsTest.WHITESPACE, Strings.trimEnd(StringsTest.WHITESPACE, Arrays.EMPTY_CHAR_ARRAY));
    }
    
}
