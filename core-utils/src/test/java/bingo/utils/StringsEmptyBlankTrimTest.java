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

import junit.framework.TestCase;

import static bingo.utils.StringsTest.*;

/**
 * {@link TestCase} of {@link Strings}
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
public class StringsEmptyBlankTrimTest extends TestCase {
	
	public void testIsEmpty() {
		assertEquals(true, Strings.isEmpty(null));
		assertEquals(true, Strings.isEmpty(""));
		assertEquals(false, Strings.isEmpty(" "));
		assertEquals(false, Strings.isEmpty("foo"));
		assertEquals(false, Strings.isEmpty("  foo  "));
	}

	public void testIsNotEmpty() {
		assertEquals(false, Strings.isNotEmpty(null));
		assertEquals(false, Strings.isNotEmpty(""));
		assertEquals(true, Strings.isNotEmpty(" "));
		assertEquals(true, Strings.isNotEmpty("foo"));
		assertEquals(true, Strings.isNotEmpty("  foo  "));
	}

	public void testIsBlank() {
		assertEquals(true, Strings.isBlank(null));
		assertEquals(true, Strings.isBlank(""));
		assertEquals(true, Strings.isBlank(" "));
		assertEquals(false, Strings.isBlank("foo"));
		assertEquals(false, Strings.isBlank("  foo  "));
	}

	public void testIsNotBlank() {
		assertEquals(false, Strings.isNotBlank(null));
		assertEquals(false, Strings.isNotBlank(""));
		assertEquals(false, Strings.isNotBlank(StringsTest.WHITESPACE));
		assertEquals(true, Strings.isNotBlank("foo"));
		assertEquals(true, Strings.isNotBlank("  foo  "));
	}
	
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
}
