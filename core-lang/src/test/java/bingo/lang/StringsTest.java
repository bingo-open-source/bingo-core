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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.CharBuffer;

import junit.framework.TestCase;

import org.junit.Test;

import bingo.lang.testing.junit.ConcurrentTestCase;

import static org.junit.Assert.*;

/**
 * {@link TestCase} of {@link Strings}
 */
public class StringsTest extends ConcurrentTestCase {

	static final String	        FOO	                = "FOO";
	static final String	        BAR	                = "BAR";
	static final String	        foo	                = "foo";
	static final String	        bar	                = "bar";
	static final String	        foobar	            = "foobar";
	static final String	        FOOBAR	            = "FOOBAR";

	static final String	        WHITESPACE;
	static final String	        NON_WHITESPACE;
	static final String	        TRIMMABLE;
	static final String	        NON_TRIMMABLE;

	static final String[]	    ARRAY_LIST	        = { "foo", "bar", "baz" };
	static final String[]	    EMPTY_ARRAY_LIST	= {};
	static final String[]	    NULL_ARRAY_LIST	    = { null };
	static final Object[]	    NULL_TO_STRING_LIST	= { new Object() {
		                                                @Override
		                                                public String toString() {
			                                                return null;
		                                                }
	                                                } };
	static final String[]	    MIXED_ARRAY_LIST	= { null, "", "foo" };
	static final Object[]	    MIXED_TYPE_LIST	    = { "foo", Long.valueOf(2L) };

	static final String	        SEPARATOR	        = ",";
	static final char	        SEPARATOR_CHAR	    = ';';

	static final String	        TEXT_LIST	        = "foo,bar,baz";
	static final String	        TEXT_LIST_CHAR	    = "foo;bar;baz";
	static final String	        TEXT_LIST_NOSEP	    = "foobarbaz";

	static final String	        FOO_UNCAP	        = "foo";
	static final String	        FOO_CAP	            = "Foo";

	static final String	        SENTENCE_UNCAP	    = "foo bar baz";
	static final String	        SENTENCE_CAP	    = "Foo Bar Baz";

	/**
	 * Supplementary character U+20000 See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
	 */
	private static final String	CharU20000	        = "\uD840\uDC00";
	/**
	 * Supplementary character U+20001 See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
	 */
	private static final String	CharU20001	        = "\uD840\uDC01";
	
    /**
     * Incomplete supplementary character U+20000, high surrogate only.
     * See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
     */
    private static final String CharUSuppCharHigh = "\uDC00";

    /**
     * Incomplete supplementary character U+20000, low surrogate only.
     * See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
     */
    private static final String CharUSuppCharLow = "\uD840";	
	
	private static final String BAZ = "baz";
	private static final String SENTENCE = "foo bar baz";

	static {
		String ws = "";
		String nws = "";
		String tr = "";
		String ntr = "";
		for (int i = 0; i < Character.MAX_VALUE; i++) {
			if (Character.isWhitespace((char) i)) {
				ws += String.valueOf((char) i);
				if (i > 32) {
					ntr += String.valueOf((char) i);
				}
			} else if (i < 40) {
				nws += String.valueOf((char) i);
			}
		}
		for (int i = 0; i <= 32; i++) {
			tr += String.valueOf((char) i);
		}
		WHITESPACE = ws;
		NON_WHITESPACE = nws;
		TRIMMABLE = tr;
		NON_TRIMMABLE = ntr;
	}

	@Test
	public void testEmptyConstant() {
		assertNotNull(Strings.EMPTY);
		assertEquals("", Strings.EMPTY);
	}

	@Test
	public void testConstructor() {
		new Strings();
		new SubStrings();

		Constructor<?>[] cons = Strings.class.getDeclaredConstructors();
		assertEquals(1, cons.length);
		assertEquals(true, Modifier.isProtected(cons[0].getModifiers()));
		assertEquals(true, Modifier.isPublic(Strings.class.getModifiers()));
		assertEquals(false, Modifier.isFinal(Strings.class.getModifiers()));
	}

	@Test
	public void testSafe() {
		assertNotNull(Strings.safe(null));
		assertNotNull(Strings.safe(""));
		assertEquals("", Strings.safe(""));
		assertEquals(" x", Strings.safe(" x"));
		assertEquals(" x ", Strings.safe(" x "));
	}

	@Test
	public void testCaseFunctions() {
		assertEquals(Strings.EMPTY, Strings.upperCase(null));
		assertEquals(Strings.EMPTY, Strings.lowerCase(null));

		assertEquals("upperCase(String) failed", "FOO TEST THING", Strings.upperCase("fOo test THING"));
		assertEquals("upperCase(empty-string) failed", "", Strings.upperCase(""));
		assertEquals("lowerCase(String) failed", "foo test thing", Strings.lowerCase("fOo test THING"));
		assertEquals("lowerCase(empty-string) failed", "", Strings.lowerCase(""));
	}

	@Test
	public void testReplace_StringStringString() {
		assertEquals(Strings.EMPTY, Strings.replace(null, null, null));
		assertEquals(Strings.EMPTY, Strings.replace(null, null, "any"));
		assertEquals(Strings.EMPTY, Strings.replace(null, "any", null));
		assertEquals(Strings.EMPTY, Strings.replace(null, "any", "any"));

		assertEquals("", Strings.replace("", null, null));
		assertEquals("", Strings.replace("", null, "any"));
		assertEquals("", Strings.replace("", "any", null));
		assertEquals("", Strings.replace("", "any", "any"));

		assertEquals("FOO", Strings.replace("FOO", "", "any"));
		assertEquals("FOO", Strings.replace("FOO", null, "any"));
		assertEquals("FOO", Strings.replace("FOO", "F", null));
		assertEquals("FOO", Strings.replace("FOO", null, null));

		assertEquals("", Strings.replace("foofoofoo", "foo", ""));
		assertEquals("barbarbar", Strings.replace("foofoofoo", "foo", "bar"));
		assertEquals("farfarfar", Strings.replace("foofoofoo", "oo", "ar"));
	}

	@Test
	public void testReplaceOnce_StringStringString() {
		assertEquals(Strings.EMPTY, Strings.replaceOnce(null, null, null));
		assertEquals(Strings.EMPTY, Strings.replaceOnce(null, null, "any"));
		assertEquals(Strings.EMPTY, Strings.replaceOnce(null, "any", null));
		assertEquals(Strings.EMPTY, Strings.replaceOnce(null, "any", "any"));

		assertEquals("", Strings.replaceOnce("", null, null));
		assertEquals("", Strings.replaceOnce("", null, "any"));
		assertEquals("", Strings.replaceOnce("", "any", null));
		assertEquals("", Strings.replaceOnce("", "any", "any"));

		assertEquals("FOO", Strings.replaceOnce("FOO", "", "any"));
		assertEquals("FOO", Strings.replaceOnce("FOO", null, "any"));
		assertEquals("FOO", Strings.replaceOnce("FOO", "F", null));
		assertEquals("FOO", Strings.replaceOnce("FOO", null, null));

		assertEquals("foofoo", Strings.replaceOnce("foofoofoo", "foo", ""));
	}

	@Test
	public void testIndexOfAnyBut_StringCharArray() {
		assertEquals(-1, Strings.indexOfAnyBut(null, (char[]) null));
		assertEquals(-1, Strings.indexOfAnyBut(null, new char[0]));
		assertEquals(-1, Strings.indexOfAnyBut(null, new char[] { 'a', 'b' }));

		assertEquals(-1, Strings.indexOfAnyBut("", (char[]) null));
		assertEquals(-1, Strings.indexOfAnyBut("", new char[0]));
		assertEquals(-1, Strings.indexOfAnyBut("", new char[] { 'a', 'b' }));

		assertEquals(-1, Strings.indexOfAnyBut("zzabyycdxx", (char[]) null));
		assertEquals(-1, Strings.indexOfAnyBut("zzabyycdxx", new char[0]));
		assertEquals(3, Strings.indexOfAnyBut("zzabyycdxx", new char[] { 'z', 'a' }));
		assertEquals(0, Strings.indexOfAnyBut("zzabyycdxx", new char[] { 'b', 'y' }));
		assertEquals(-1, Strings.indexOfAnyBut("aba", new char[] { 'a', 'b' }));
		assertEquals(0, Strings.indexOfAnyBut("aba", new char[] { 'z' }));

	}

	@Test
	public void testIndexOfAnyBut_StringCharArrayWithSupplementaryChars() {
		assertEquals(2, Strings.indexOfAnyBut(CharU20000 + CharU20001, CharU20000.toCharArray()));
		assertEquals(0, Strings.indexOfAnyBut(CharU20000 + CharU20001, CharU20001.toCharArray()));
		assertEquals(-1, Strings.indexOfAnyBut(CharU20000, CharU20000.toCharArray()));
		assertEquals(0, Strings.indexOfAnyBut(CharU20000, CharU20001.toCharArray()));
	}

	@Test
	public void testIndexOfAnyBut_StringString() {
		assertEquals(-1, Strings.indexOfAnyBut(null, (String) null));
		assertEquals(-1, Strings.indexOfAnyBut(null, ""));
		assertEquals(-1, Strings.indexOfAnyBut(null, "ab"));

		assertEquals(-1, Strings.indexOfAnyBut("", (String) null));
		assertEquals(-1, Strings.indexOfAnyBut("", ""));
		assertEquals(-1, Strings.indexOfAnyBut("", "ab"));

		assertEquals(-1, Strings.indexOfAnyBut("zzabyycdxx", (String) null));
		assertEquals(-1, Strings.indexOfAnyBut("zzabyycdxx", ""));
		assertEquals(3, Strings.indexOfAnyBut("zzabyycdxx", "za"));
		assertEquals(0, Strings.indexOfAnyBut("zzabyycdxx", "by"));
		assertEquals(0, Strings.indexOfAnyBut("ab", "z"));
	}

	@Test
	public void testIndexOfAnyBut_StringStringWithSupplementaryChars() {
		assertEquals(2, Strings.indexOfAnyBut(CharU20000 + CharU20001, CharU20000));
		assertEquals(0, Strings.indexOfAnyBut(CharU20000 + CharU20001, CharU20001));
		assertEquals(-1, Strings.indexOfAnyBut(CharU20000, CharU20000));
		assertEquals(0, Strings.indexOfAnyBut(CharU20000, CharU20001));
	}

	/**
	 * Test Strings.startsWith()
	 */
	@Test
	public void testStartsWith() {
		assertTrue("startsWith(null, null)", Strings.startsWith(null, (String) null));
		assertFalse("startsWith(FOOBAR, null)", Strings.startsWith(FOOBAR, (String) null));
		assertFalse("startsWith(null, FOO)", Strings.startsWith(null, FOO));
		assertTrue("startsWith(FOOBAR, \"\")", Strings.startsWith(FOOBAR, ""));

		assertTrue("startsWith(foobar, foo)", Strings.startsWith(foobar, foo));
		assertTrue("startsWith(FOOBAR, FOO)", Strings.startsWith(FOOBAR, FOO));
		assertFalse("startsWith(foobar, FOO)", Strings.startsWith(foobar, FOO));
		assertFalse("startsWith(FOOBAR, foo)", Strings.startsWith(FOOBAR, foo));

		assertFalse("startsWith(foo, foobar)", Strings.startsWith(foo, foobar));
		assertFalse("startsWith(foo, foobar)", Strings.startsWith(bar, foobar));

		assertFalse("startsWith(foobar, bar)", Strings.startsWith(foobar, bar));
		assertFalse("startsWith(FOOBAR, BAR)", Strings.startsWith(FOOBAR, BAR));
		assertFalse("startsWith(foobar, BAR)", Strings.startsWith(foobar, BAR));
		assertFalse("startsWith(FOOBAR, bar)", Strings.startsWith(FOOBAR, bar));
	}

	/**
	 * Test Strings.testStartsWithIgnoreCase()
	 */
	@Test
	public void testStartsWithIgnoreCase() {
		assertTrue("startsWithIgnoreCase(null, null)", Strings.startsWithIgnoreCase(null, (String) null));
		assertFalse("startsWithIgnoreCase(FOOBAR, null)", Strings.startsWithIgnoreCase(FOOBAR, (String) null));
		assertFalse("startsWithIgnoreCase(null, FOO)", Strings.startsWithIgnoreCase(null, FOO));
		assertTrue("startsWithIgnoreCase(FOOBAR, \"\")", Strings.startsWithIgnoreCase(FOOBAR, ""));

		assertTrue("startsWithIgnoreCase(foobar, foo)", Strings.startsWithIgnoreCase(foobar, foo));
		assertTrue("startsWithIgnoreCase(FOOBAR, FOO)", Strings.startsWithIgnoreCase(FOOBAR, FOO));
		assertTrue("startsWithIgnoreCase(foobar, FOO)", Strings.startsWithIgnoreCase(foobar, FOO));
		assertTrue("startsWithIgnoreCase(FOOBAR, foo)", Strings.startsWithIgnoreCase(FOOBAR, foo));

		assertFalse("startsWithIgnoreCase(foo, foobar)", Strings.startsWithIgnoreCase(foo, foobar));
		assertFalse("startsWithIgnoreCase(foo, foobar)", Strings.startsWithIgnoreCase(bar, foobar));

		assertFalse("startsWithIgnoreCase(foobar, bar)", Strings.startsWithIgnoreCase(foobar, bar));
		assertFalse("startsWithIgnoreCase(FOOBAR, BAR)", Strings.startsWithIgnoreCase(FOOBAR, BAR));
		assertFalse("startsWithIgnoreCase(foobar, BAR)", Strings.startsWithIgnoreCase(foobar, BAR));
		assertFalse("startsWithIgnoreCase(FOOBAR, bar)", Strings.startsWithIgnoreCase(FOOBAR, bar));
	}

	/**
	 * Test Strings.endsWith()
	 */
	@Test
	public void testEndsWith() {
		assertTrue("endsWith(null, null)", Strings.endsWith(null, (String) null));
		assertFalse("endsWith(FOOBAR, null)", Strings.endsWith(FOOBAR, (String) null));
		assertFalse("endsWith(null, FOO)", Strings.endsWith(null, FOO));
		assertTrue("endsWith(FOOBAR, \"\")", Strings.endsWith(FOOBAR, ""));

		assertFalse("endsWith(foobar, foo)", Strings.endsWith(foobar, foo));
		assertFalse("endsWith(FOOBAR, FOO)", Strings.endsWith(FOOBAR, FOO));
		assertFalse("endsWith(foobar, FOO)", Strings.endsWith(foobar, FOO));
		assertFalse("endsWith(FOOBAR, foo)", Strings.endsWith(FOOBAR, foo));

		assertFalse("endsWith(foo, foobar)", Strings.endsWith(foo, foobar));
		assertFalse("endsWith(foo, foobar)", Strings.endsWith(bar, foobar));

		assertTrue("endsWith(foobar, bar)", Strings.endsWith(foobar, bar));
		assertTrue("endsWith(FOOBAR, BAR)", Strings.endsWith(FOOBAR, BAR));
		assertFalse("endsWith(foobar, BAR)", Strings.endsWith(foobar, BAR));
		assertFalse("endsWith(FOOBAR, bar)", Strings.endsWith(FOOBAR, bar));
	}

	/**
	 * Test Strings.endsWithIgnoreCase()
	 */
	@Test
	public void testEndsWithIgnoreCase() {
		assertTrue("endsWithIgnoreCase(null, null)", Strings.endsWithIgnoreCase(null, (String) null));
		assertFalse("endsWithIgnoreCase(FOOBAR, null)", Strings.endsWithIgnoreCase(FOOBAR, (String) null));
		assertFalse("endsWithIgnoreCase(null, FOO)", Strings.endsWithIgnoreCase(null, FOO));
		assertTrue("endsWithIgnoreCase(FOOBAR, \"\")", Strings.endsWithIgnoreCase(FOOBAR, ""));

		assertFalse("endsWithIgnoreCase(foobar, foo)", Strings.endsWithIgnoreCase(foobar, foo));
		assertFalse("endsWithIgnoreCase(FOOBAR, FOO)", Strings.endsWithIgnoreCase(FOOBAR, FOO));
		assertFalse("endsWithIgnoreCase(foobar, FOO)", Strings.endsWithIgnoreCase(foobar, FOO));
		assertFalse("endsWithIgnoreCase(FOOBAR, foo)", Strings.endsWithIgnoreCase(FOOBAR, foo));

		assertFalse("endsWithIgnoreCase(foo, foobar)", Strings.endsWithIgnoreCase(foo, foobar));
		assertFalse("endsWithIgnoreCase(foo, foobar)", Strings.endsWithIgnoreCase(bar, foobar));

		assertTrue("endsWithIgnoreCase(foobar, bar)", Strings.endsWithIgnoreCase(foobar, bar));
		assertTrue("endsWithIgnoreCase(FOOBAR, BAR)", Strings.endsWithIgnoreCase(FOOBAR, BAR));
		assertTrue("endsWithIgnoreCase(foobar, BAR)", Strings.endsWithIgnoreCase(foobar, BAR));
		assertTrue("endsWithIgnoreCase(FOOBAR, bar)", Strings.endsWithIgnoreCase(FOOBAR, bar));

		// javadoc
		assertTrue(Strings.endsWithIgnoreCase("abcdef", "def"));
		assertTrue(Strings.endsWithIgnoreCase("ABCDEF", "def"));
		assertFalse(Strings.endsWithIgnoreCase("ABCDEF", "cde"));
	}

	@Test
	public void testRemoveStart() {
		// Strings.removeStart("", *)        = ""
		assertEquals("", Strings.removeStart(null, null));
		assertEquals("", Strings.removeStart(null, ""));
		assertEquals("", Strings.removeStart(null, "a"));

		// Strings.removeStartsWith(*, null)      = *
		assertEquals(Strings.removeStart("", null), "");
		assertEquals(Strings.removeStart("", ""), "");
		assertEquals(Strings.removeStart("", "a"), "");

		// All others:
		assertEquals(Strings.removeStart("www.domain.com", "www."), "domain.com");
		assertEquals(Strings.removeStart("domain.com", "www."), "domain.com");
		assertEquals(Strings.removeStart("domain.com", ""), "domain.com");
		assertEquals(Strings.removeStart("domain.com", null), "domain.com");
	}

	@Test
	public void testremoveStartsWithIgnoreCase() {
		// Strings.removeStartsWith("", *)        = ""
		assertEquals("", Strings.removeStartIgnoreCase(null, null));
		assertEquals("", Strings.removeStartIgnoreCase(null, ""));
		assertEquals("", Strings.removeStartIgnoreCase(null, "a"));

		// Strings.removeStartsWith(*, null)      = *
		assertEquals("removeStartsWithIgnoreCase(\"\", null)", Strings.removeStartIgnoreCase("", null), "");
		assertEquals("removeStartsWithIgnoreCase(\"\", \"\")", Strings.removeStartIgnoreCase("", ""), "");
		assertEquals("removeStartsWithIgnoreCase(\"\", \"a\")", Strings.removeStartIgnoreCase("", "a"), "");

		// All others:
		assertEquals("removeStartsWithIgnoreCase(\"www.domain.com\", \"www.\")", Strings.removeStartIgnoreCase("www.domain.com", "www."),
		        "domain.com");
		assertEquals("removeStartsWithIgnoreCase(\"domain.com\", \"www.\")", Strings.removeStartIgnoreCase("domain.com", "www."), "domain.com");
		assertEquals("removeStartsWithIgnoreCase(\"domain.com\", \"\")", Strings.removeStartIgnoreCase("domain.com", ""), "domain.com");
		assertEquals("removeStartsWithIgnoreCase(\"domain.com\", null)", Strings.removeStartIgnoreCase("domain.com", null), "domain.com");

		// Case insensitive:
		assertEquals("removeStartsWithIgnoreCase(\"www.domain.com\", \"WWW.\")", Strings.removeStartIgnoreCase("www.domain.com", "WWW."),
		        "domain.com");
	}

	@Test
	public void testRemoveEnd() {
		// Strings.removeEnd("", *)        = ""
		assertEquals("", Strings.removeEnd(null, null));
		assertEquals("", Strings.removeEnd(null, ""));
		assertEquals("", Strings.removeEnd(null, "a"));

		// Strings.removeEndsWith(*, null)      = *
		assertEquals(Strings.removeEnd("", null), "");
		assertEquals(Strings.removeEnd("", ""), "");
		assertEquals(Strings.removeEnd("", "a"), "");

		// All others:
		assertEquals(Strings.removeEnd("www.domain.com.", ".com"), "www.domain.com.");
		assertEquals(Strings.removeEnd("www.domain.com", ".com"), "www.domain");
		assertEquals(Strings.removeEnd("www.domain", ".com"), "www.domain");
		assertEquals(Strings.removeEnd("domain.com", ""), "domain.com");
		assertEquals(Strings.removeEnd("domain.com", null), "domain.com");
	}

	@Test
	public void testremoveEndsWithIgnoreCase() {
		// Strings.removeEndsWithIgnoreCase("", *)        = ""
		assertEquals("", Strings.removeEndIgnoreCase(null, null));
		assertEquals("", Strings.removeEndIgnoreCase(null, ""));
		assertEquals("", Strings.removeEndIgnoreCase(null, "a"));

		// Strings.removeEndsWith(*, null)      = *
		assertEquals("removeEndsWithIgnoreCase(\"\", null)", Strings.removeEndIgnoreCase("", null), "");
		assertEquals("removeEndsWithIgnoreCase(\"\", \"\")", Strings.removeEndIgnoreCase("", ""), "");
		assertEquals("removeEndsWithIgnoreCase(\"\", \"a\")", Strings.removeEndIgnoreCase("", "a"), "");

		// All others:
		assertEquals("removeEndsWithIgnoreCase(\"www.domain.com.\", \".com\")", Strings.removeEndIgnoreCase("www.domain.com.", ".com"),
		        "www.domain.com.");
		assertEquals("removeEndsWithIgnoreCase(\"www.domain.com\", \".com\")", Strings.removeEndIgnoreCase("www.domain.com", ".com"),
		        "www.domain");
		assertEquals("removeEndsWithIgnoreCase(\"www.domain\", \".com\")", Strings.removeEndIgnoreCase("www.domain", ".com"), "www.domain");
		assertEquals("removeEndsWithIgnoreCase(\"domain.com\", \"\")", Strings.removeEndIgnoreCase("domain.com", ""), "domain.com");
		assertEquals("removeEndsWithIgnoreCase(\"domain.com\", null)", Strings.removeEndIgnoreCase("domain.com", null), "domain.com");

		// Case insensitive:
		assertEquals("removeEndsWithIgnoreCase(\"www.domain.com\", \".COM\")", Strings.removeEndIgnoreCase("www.domain.com", ".COM"),
		        "www.domain");
		assertEquals("removeEndsWithIgnoreCase(\"www.domain.COM\", \".com\")", Strings.removeEndIgnoreCase("www.domain.COM", ".com"),
		        "www.domain");
	}

	@Test
	public void testRemove_String() {
		// Strings.remove(null, *)        = null
		assertEquals("", Strings.remove(null, null));
		assertEquals("", Strings.remove(null, ""));
		assertEquals("", Strings.remove(null, "a"));

		// Strings.remove("", *)          = ""
		assertEquals("", Strings.remove("", null));
		assertEquals("", Strings.remove("", ""));
		assertEquals("", Strings.remove("", "a"));

		// Strings.remove(*, null)        = *
		assertEquals("", Strings.remove(null, null));
		assertEquals("", Strings.remove("", null));
		assertEquals("a", Strings.remove("a", null));

		// Strings.remove(*, "")          = *
		assertEquals("", Strings.remove(null, ""));
		assertEquals("", Strings.remove("", ""));
		assertEquals("a", Strings.remove("a", ""));

		// Strings.remove("queued", "ue") = "qd"
		assertEquals("qd", Strings.remove("queued", "ue"));

		// Strings.remove("queued", "zz") = "queued"
		assertEquals("queued", Strings.remove("queued", "zz"));
	}

	@Test
	public void testRemove_char() {
		// Strings.remove(null, *)       = null
		assertEquals("", Strings.remove(null, 'a'));

		// Strings.remove("", *)          = ""
		assertEquals("", Strings.remove("", 'a'));
		assertEquals("", Strings.remove("", 'a'));
		assertEquals("", Strings.remove("", 'a'));

		// Strings.remove("queued", 'u') = "qeed"
		assertEquals("qeed", Strings.remove("queued", 'u'));

		// Strings.remove("queued", 'z') = "queued"
		assertEquals("queued", Strings.remove("queued", 'z'));
	}

	@Test
	public void testRepeat_StringInt() {
		assertEquals("", Strings.repeat(null, 2));
		assertEquals("", Strings.repeat("ab", 0));
		assertEquals("", Strings.repeat("", 3));
		assertEquals("aaa", Strings.repeat("a", 3));
		assertEquals("ababab", Strings.repeat("ab", 3));
		assertEquals("abcabcabc", Strings.repeat("abc", 3));
		String str = Strings.repeat("a", 10000); // bigger than pad limit
		assertEquals(10000, str.length());
		assertTrue(Strings.containsOnly(str, new char[] { 'a' }));
	}

	@Test
	public void testRepeat_StringStringInt() {
		assertEquals("", Strings.repeat(null, null, 2));
		assertEquals("", Strings.repeat(null, "x", 2));
		assertEquals("", Strings.repeat("", null, 2));

		assertEquals("", Strings.repeat("ab", "", 0));
		assertEquals("", Strings.repeat("", "", 2));

		assertEquals("xx", Strings.repeat("", "x", 3));

		assertEquals("?, ?, ?", Strings.repeat("?", ", ", 3));
	}

	@Test
	public void testRightPad_StringInt() {
		assertEquals("", Strings.padRight(null, 5));
		assertEquals("     ", Strings.padRight("", 5));
		assertEquals("abc  ", Strings.padRight("abc", 5));
		assertEquals("abc", Strings.padRight("abc", 2));
		assertEquals("abc", Strings.padRight("abc", -1));
	}

	@Test
	public void testpaddingRight_StringIntChar() {
		assertEquals("", Strings.padRight(null, ' ', 5));
		assertEquals("     ", Strings.padRight("", ' ', 5));
		assertEquals("abc  ", Strings.padRight("abc", ' ', 5));
		assertEquals("abc", Strings.padRight("abc", ' ', 2));
		assertEquals("abc", Strings.padRight("abc", ' ', -1));
		assertEquals("abcxx", Strings.padRight("abc", 'x', 5));
		String str = Strings.padRight("aaa", 'a', 10000); // bigger than pad length
		assertEquals(10000, str.length());
		assertTrue(Strings.containsOnly(str, new char[] { 'a' }));
	}

	@Test
	public void testpaddingRight_StringIntString() {
		assertEquals("", Strings.padRight(null, "-+", 5));
		assertEquals("     ", Strings.padRight("", " ", 5));
		assertEquals("", Strings.padRight(null, null, 8));
		assertEquals("abc-+-+", Strings.padRight("abc", "-+", 7));
		assertEquals("abc-+~", Strings.padRight("abc", "-+~", 6));
		assertEquals("abc-+", Strings.padRight("abc", "-+~", 5));
		assertEquals("abc", Strings.padRight("abc", " ", 2));
		assertEquals("abc", Strings.padRight("abc", " ", -1));
		assertEquals("abc  ", Strings.padRight("abc", null, 5));
		assertEquals("abc  ", Strings.padRight("abc", "", 5));
	}

	//-----------------------------------------------------------------------
	@Test
	public void testLeftPad_StringInt() {
		assertEquals("", Strings.padLeft(null, 5));
		assertEquals("     ", Strings.padLeft("", 5));
		assertEquals("  abc", Strings.padLeft("abc", 5));
		assertEquals("abc", Strings.padLeft("abc", 2));
	}

	@Test
	public void testpaddingLeft_StringIntChar() {
		assertEquals("", Strings.padLeft(null, ' ', 5));
		assertEquals("     ", Strings.padLeft("", ' ', 5));
		assertEquals("  abc", Strings.padLeft("abc", ' ', 5));
		assertEquals("xxabc", Strings.padLeft("abc",'x', 5));
		assertEquals("\uffff\uffffabc", Strings.padLeft("abc",  '\uffff', 5));
		assertEquals("abc", Strings.padLeft("abc", ' ', 2));
		String str = Strings.padLeft("aaa", 'a', 10000); // bigger than pad length
		assertEquals(10000, str.length());
		assertTrue(Strings.containsOnly(str, new char[] { 'a' }));
	}

	@Test
	public void testpaddingLeft_StringIntString() {
		assertEquals("", Strings.padLeft(null, "-+", 5));
		assertEquals("", Strings.padLeft(null, null, 5));
		assertEquals("     ", Strings.padLeft("", " ", 5));
		assertEquals("-+-+abc", Strings.padLeft("abc", "-+", 7));
		assertEquals("-+~abc", Strings.padLeft("abc", "-+~", 6));
		assertEquals("-+abc", Strings.padLeft("abc", "-+~", 5));
		assertEquals("abc", Strings.padLeft("abc", " ", 2));
		assertEquals("abc", Strings.padLeft("abc", " ", -1));
		assertEquals("  abc", Strings.padLeft("abc", null, 5));
		assertEquals("  abc", Strings.padLeft("abc", "", 5));
	}

	@Test
	public void testLengthString() {
		assertEquals(0, Strings.length(null));
		assertEquals(0, Strings.length(""));
		assertEquals(0, Strings.length(Strings.EMPTY));
		assertEquals(1, Strings.length("A"));
		assertEquals(1, Strings.length(" "));
		assertEquals(8, Strings.length("ABCDEFGH"));
	}

	@Test
	public void testLengthStringBuffer() {
		assertEquals(0, Strings.length(new StringBuffer("")));
		assertEquals(0, Strings.length(new StringBuffer(Strings.EMPTY)));
		assertEquals(1, Strings.length(new StringBuffer("A")));
		assertEquals(1, Strings.length(new StringBuffer(" ")));
		assertEquals(8, Strings.length(new StringBuffer("ABCDEFGH")));
	}

	@Test
	public void testLengthStringBuilder() {
		assertEquals(0, Strings.length(new StringBuilder("")));
		assertEquals(0, Strings.length(new StringBuilder(Strings.EMPTY)));
		assertEquals(1, Strings.length(new StringBuilder("A")));
		assertEquals(1, Strings.length(new StringBuilder(" ")));
		assertEquals(8, Strings.length(new StringBuilder("ABCDEFGH")));
	}

	@Test
	public void testLength_CharBuffer() {
		assertEquals(0, Strings.length(CharBuffer.wrap("")));
		assertEquals(1, Strings.length(CharBuffer.wrap("A")));
		assertEquals(1, Strings.length(CharBuffer.wrap(" ")));
		assertEquals(8, Strings.length(CharBuffer.wrap("ABCDEFGH")));
	}
	
	@Test
    public void testSubstring_StringInt() {
        assertEquals("", Strings.substring(null, 0));
        assertEquals("", Strings.substring("", 0));
        assertEquals("", Strings.substring("", 2));
        
        assertEquals("", Strings.substring(SENTENCE, 80));
        assertEquals(BAZ, Strings.substring(SENTENCE, 8));
        assertEquals(BAZ, Strings.substring(SENTENCE, -3));
        assertEquals(SENTENCE, Strings.substring(SENTENCE, 0));
        assertEquals("abc", Strings.substring("abc", -4));
        assertEquals("abc", Strings.substring("abc", -3));
        assertEquals("bc", Strings.substring("abc", -2));
        assertEquals("c", Strings.substring("abc", -1));
        assertEquals("abc", Strings.substring("abc", 0));
        assertEquals("bc", Strings.substring("abc", 1));
        assertEquals("c", Strings.substring("abc", 2));
        assertEquals("", Strings.substring("abc", 3));
        assertEquals("", Strings.substring("abc", 4));
    }
    
	@Test
    public void testSubstring_StringIntInt() {
        assertEquals("", Strings.substring(null, 0, 0));
        assertEquals("", Strings.substring(null, 1, 2));
        assertEquals("", Strings.substring("", 0, 0));
        assertEquals("", Strings.substring("", 1, 2));
        assertEquals("", Strings.substring("", -2, -1));
        
        assertEquals("", Strings.substring(SENTENCE, 8, 6));
        assertEquals(foo, Strings.substring(SENTENCE, 0, 3));
        assertEquals("o", Strings.substring(SENTENCE, -9, 3));
        assertEquals(foo, Strings.substring(SENTENCE, 0, -8));
        assertEquals("o", Strings.substring(SENTENCE, -9, -8));
        assertEquals(SENTENCE, Strings.substring(SENTENCE, 0, 80));
        assertEquals("", Strings.substring(SENTENCE, 2, 2));
        assertEquals("b",Strings.substring("abc", -2, -1));
    }
           
	@Test
    public void testLeft_String() {
        assertSame("", Strings.left(null, -1));
        assertSame("", Strings.left(null, 0));
        assertSame("", Strings.left(null, 2));
        
        assertEquals("", Strings.left("", -1));
        assertEquals("", Strings.left("", 0));
        assertEquals("", Strings.left("", 2));
        
        assertEquals("", Strings.left(FOOBAR, -1));
        assertEquals("", Strings.left(FOOBAR, 0));
        assertEquals(FOO, Strings.left(FOOBAR, 3));
        assertSame(FOOBAR, Strings.left(FOOBAR, 80));
    }
    
	@Test
    public void testRight_String() {
        assertSame("", Strings.right(null, -1));
        assertSame("", Strings.right(null, 0));
        assertSame("", Strings.right(null, 2));
        
        assertEquals("", Strings.right("", -1));
        assertEquals("", Strings.right("", 0));
        assertEquals("", Strings.right("", 2));
        
        assertEquals("", Strings.right(FOOBAR, -1));
        assertEquals("", Strings.right(FOOBAR, 0));
        assertEquals(BAR, Strings.right(FOOBAR, 3));
        assertSame(FOOBAR, Strings.right(FOOBAR, 80));
    }	
    
	@Test
    public void testContains_Char() {
        assertEquals(false, Strings.contains(null, ' '));
        assertEquals(false, Strings.contains("", ' '));
        assertEquals(false, Strings.contains("", null));
        assertEquals(false, Strings.contains(null, null));
        assertEquals(true, Strings.contains("abc", 'a'));
        assertEquals(true, Strings.contains("abc", 'b'));
        assertEquals(true, Strings.contains("abc", 'c'));
        assertEquals(false, Strings.contains("abc", 'z'));
    }

	@Test
    public void testContains_String() {
        assertEquals(false, Strings.contains(null, null));
        assertEquals(false, Strings.contains(null, ""));
        assertEquals(false, Strings.contains(null, "a"));
        assertEquals(false, Strings.contains("", null));
        assertEquals(true, Strings.contains("", ""));
        assertEquals(false, Strings.contains("", "a"));
        assertEquals(true, Strings.contains("abc", "a"));
        assertEquals(true, Strings.contains("abc", "b"));
        assertEquals(true, Strings.contains("abc", "c"));
        assertEquals(true, Strings.contains("abc", "abc"));
        assertEquals(false, Strings.contains("abc", "z"));
    }

    /**
     * See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
     */
	@Test
    public void testContains_StringWithBadSupplementaryChars() {
        // Test edge case: 1/2 of a (broken) supplementary char
        assertEquals(false, Strings.contains(CharUSuppCharHigh, CharU20001));
        assertEquals(false, Strings.contains(CharUSuppCharLow, CharU20001));
        assertEquals(false, Strings.contains(CharU20001, CharUSuppCharHigh));
        assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
        assertEquals(true, Strings.contains(CharU20001, CharUSuppCharLow));
        assertEquals(true, Strings.contains(CharU20001 + CharUSuppCharLow + "a", "a"));
        assertEquals(true, Strings.contains(CharU20001 + CharUSuppCharHigh + "a", "a"));
    }

    /**
     * See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
     */
	@Test
    public void testContains_StringWithSupplementaryChars() {
        assertEquals(true, Strings.contains(CharU20000 + CharU20001, CharU20000));
        assertEquals(true, Strings.contains(CharU20000 + CharU20001, CharU20001));
        assertEquals(true, Strings.contains(CharU20000, CharU20000));
        assertEquals(false, Strings.contains(CharU20000, CharU20001));
    }    

	@Test
    public void testContainsIgnoreCase_StringString() {
        assertFalse(Strings.containsIgnoreCase(null, null));

        // Null tests
        assertFalse(Strings.containsIgnoreCase(null, ""));
        assertFalse(Strings.containsIgnoreCase(null, "a"));
        assertFalse(Strings.containsIgnoreCase(null, "abc"));

        assertFalse(Strings.containsIgnoreCase("", null));
        assertFalse(Strings.containsIgnoreCase("a", null));
        assertFalse(Strings.containsIgnoreCase("abc", null));

        // Match len = 0
        assertTrue(Strings.containsIgnoreCase("", ""));
        assertTrue(Strings.containsIgnoreCase("a", ""));
        assertTrue(Strings.containsIgnoreCase("abc", ""));

        // Match len = 1
        assertFalse(Strings.containsIgnoreCase("", "a"));
        assertTrue(Strings.containsIgnoreCase("a", "a"));
        assertTrue(Strings.containsIgnoreCase("abc", "a"));
        assertFalse(Strings.containsIgnoreCase("", "A"));
        assertTrue(Strings.containsIgnoreCase("a", "A"));
        assertTrue(Strings.containsIgnoreCase("abc", "A"));

        // Match len > 1
        assertFalse(Strings.containsIgnoreCase("", "abc"));
        assertFalse(Strings.containsIgnoreCase("a", "abc"));
        assertTrue(Strings.containsIgnoreCase("xabcz", "abc"));
        assertFalse(Strings.containsIgnoreCase("", "ABC"));
        assertFalse(Strings.containsIgnoreCase("a", "ABC"));
        assertTrue(Strings.containsIgnoreCase("xabcz", "ABC"));
    }

	@Test
    public void testContainsNone_CharArray() {
        String str1 = "a";
        String str2 = "b";
        String str3 = "ab.";
        char[] chars1= {'b'};
        char[] chars2= {'.'};
        char[] chars3= {'c', 'd'};
        char[] emptyChars = new char[0];
        assertEquals(true, Strings.containsNone(null, (char[]) null));
        assertEquals(true, Strings.containsNone("", (char[]) null));
        assertEquals(true, Strings.containsNone(null, emptyChars));
        assertEquals(true, Strings.containsNone(str1, emptyChars));
        assertEquals(true, Strings.containsNone("", emptyChars));
        assertEquals(true, Strings.containsNone("", chars1));
        assertEquals(true, Strings.containsNone(str1, chars1));
        assertEquals(true, Strings.containsNone(str1, chars2));
        assertEquals(true, Strings.containsNone(str1, chars3));
        assertEquals(false, Strings.containsNone(str2, chars1));
        assertEquals(true, Strings.containsNone(str2, chars2));
        assertEquals(true, Strings.containsNone(str2, chars3));
        assertEquals(false, Strings.containsNone(str3, chars1));
        assertEquals(false, Strings.containsNone(str3, chars2));
        assertEquals(true, Strings.containsNone(str3, chars3));
    }

    /**
     * See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
     */
	@Test
    public void testContainsNone_CharArrayWithBadSupplementaryChars() {
        // Test edge case: 1/2 of a (broken) supplementary char
        assertEquals(true, Strings.containsNone(CharUSuppCharHigh, CharU20001.toCharArray()));
        assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
        assertEquals(true, Strings.containsNone(CharUSuppCharLow, CharU20001.toCharArray()));
        assertEquals(-1, CharU20001.indexOf(CharUSuppCharHigh));
        assertEquals(true, Strings.containsNone(CharU20001, CharUSuppCharHigh.toCharArray()));
        assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
        assertEquals(false, Strings.containsNone(CharU20001, CharUSuppCharLow.toCharArray()));
    }

    /**
     * See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
     */
	@Test
    public void testContainsNone_CharArrayWithSupplementaryChars() {
        assertEquals(false, Strings.containsNone(CharU20000 + CharU20001, CharU20000.toCharArray()));
        assertEquals(false, Strings.containsNone(CharU20000 + CharU20001, CharU20001.toCharArray()));
        assertEquals(false, Strings.containsNone(CharU20000, CharU20000.toCharArray()));
        // Sanity check:
        assertEquals(-1, CharU20000.indexOf(CharU20001));
        assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
        assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
        // Test:
        assertEquals(true, Strings.containsNone(CharU20000, CharU20001.toCharArray()));
        assertEquals(true, Strings.containsNone(CharU20001, CharU20000.toCharArray()));
    }

	@Test
    public void testContainsNone_String() {
        String str1 = "a";
        String str2 = "b";
        String str3 = "ab.";
        String chars1= "b";
        String chars2= ".";
        String chars3= "cd";
        assertEquals(true, Strings.containsNone(null, (String) null));
        assertEquals(true, Strings.containsNone("", (String) null));
        assertEquals(true, Strings.containsNone(null, ""));
        assertEquals(true, Strings.containsNone(str1, ""));
        assertEquals(true, Strings.containsNone("", ""));
        assertEquals(true, Strings.containsNone("", chars1));
        assertEquals(true, Strings.containsNone(str1, chars1));
        assertEquals(true, Strings.containsNone(str1, chars2));
        assertEquals(true, Strings.containsNone(str1, chars3));
        assertEquals(false, Strings.containsNone(str2, chars1));
        assertEquals(true, Strings.containsNone(str2, chars2));
        assertEquals(true, Strings.containsNone(str2, chars3));
        assertEquals(false, Strings.containsNone(str3, chars1));
        assertEquals(false, Strings.containsNone(str3, chars2));
        assertEquals(true, Strings.containsNone(str3, chars3));
    }

    /**
     * See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
     */
	@Test
    public void testContainsNone_StringWithBadSupplementaryChars() {
        // Test edge case: 1/2 of a (broken) supplementary char
        assertEquals(true, Strings.containsNone(CharUSuppCharHigh, CharU20001));
        assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
        assertEquals(true, Strings.containsNone(CharUSuppCharLow, CharU20001));
        assertEquals(-1, CharU20001.indexOf(CharUSuppCharHigh));
        assertEquals(true, Strings.containsNone(CharU20001, CharUSuppCharHigh));
        assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
        assertEquals(false, Strings.containsNone(CharU20001, CharUSuppCharLow));
    }

    /**
     * See http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
     */
	@Test
    public void testContainsNone_StringWithSupplementaryChars() {
        assertEquals(false, Strings.containsNone(CharU20000 + CharU20001, CharU20000));
        assertEquals(false, Strings.containsNone(CharU20000 + CharU20001, CharU20001));
        assertEquals(false, Strings.containsNone(CharU20000, CharU20000));
        // Sanity check:
        assertEquals(-1, CharU20000.indexOf(CharU20001));
        assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
        assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
        // Test:
        assertEquals(true, Strings.containsNone(CharU20000, CharU20001));
        assertEquals(true, Strings.containsNone(CharU20001, CharU20000));
    }

	@Test
    public void testContainsOnly_CharArray() {
        String str1 = "a";
        String str2 = "b";
        String str3 = "ab";
        char[] chars1= {'b'};
        char[] chars2= {'a'};
        char[] chars3= {'a', 'b'};
        char[] emptyChars = new char[0];
        assertEquals(false, Strings.containsOnly(null, (char[]) null));
        assertEquals(false, Strings.containsOnly("", (char[]) null));
        assertEquals(false, Strings.containsOnly(null, emptyChars));
        assertEquals(false, Strings.containsOnly(str1, emptyChars));
        assertEquals(true, Strings.containsOnly("", emptyChars));
        assertEquals(true, Strings.containsOnly("", chars1));
        assertEquals(false, Strings.containsOnly(str1, chars1));
        assertEquals(true, Strings.containsOnly(str1, chars2));
        assertEquals(true, Strings.containsOnly(str1, chars3));
        assertEquals(true, Strings.containsOnly(str2, chars1));
        assertEquals(false, Strings.containsOnly(str2, chars2));
        assertEquals(true, Strings.containsOnly(str2, chars3));
        assertEquals(false, Strings.containsOnly(str3, chars1));
        assertEquals(false, Strings.containsOnly(str3, chars2));
        assertEquals(true, Strings.containsOnly(str3, chars3));
    }

	@Test
    public void testContainsOnly_String() {
        String str1 = "a";
        String str2 = "b";
        String str3 = "ab";
        String chars1= "b";
        String chars2= "a";
        String chars3= "ab";
        assertEquals(false, Strings.containsOnly(null, (String) null));
        assertEquals(false, Strings.containsOnly("", (String) null));
        assertEquals(false, Strings.containsOnly(null, ""));
        assertEquals(false, Strings.containsOnly(str1, ""));
        assertEquals(true, Strings.containsOnly("", ""));
        assertEquals(true, Strings.containsOnly("", chars1));
        assertEquals(false, Strings.containsOnly(str1, chars1));
        assertEquals(true, Strings.containsOnly(str1, chars2));
        assertEquals(true, Strings.containsOnly(str1, chars3));
        assertEquals(true, Strings.containsOnly(str2, chars1));
        assertEquals(false, Strings.containsOnly(str2, chars2));
        assertEquals(true, Strings.containsOnly(str2, chars3));
        assertEquals(false, Strings.containsOnly(str3, chars1));
        assertEquals(false, Strings.containsOnly(str3, chars2));
        assertEquals(true, Strings.containsOnly(str3, chars3));
    }

	@Test
    public void testContainsWhitespace() {
        assertFalse( Strings.containsWhitespace("") );
        assertTrue( Strings.containsWhitespace(" ") );
        assertFalse( Strings.containsWhitespace("a") );
        assertTrue( Strings.containsWhitespace("a ") );
        assertTrue( Strings.containsWhitespace(" a") );
        assertTrue( Strings.containsWhitespace("a\t") );
        assertTrue( Strings.containsWhitespace("\n") );
    }    
    
	@Test
    public void testCountOccurrences_String() {
        assertEquals(0, Strings.countOccurrences(null, null));
        assertEquals(0, Strings.countOccurrences("blah", null));
        assertEquals(0, Strings.countOccurrences(null, "DD"));

        assertEquals(0, Strings.countOccurrences("x", ""));
        assertEquals(0, Strings.countOccurrences("", ""));

        assertEquals(3, 
             Strings.countOccurrences("one long someone sentence of one", "one"));
        assertEquals(0, 
             Strings.countOccurrences("one long someone sentence of one", "two"));
        assertEquals(4, 
             Strings.countOccurrences("oooooooooooo", "ooo"));
    }    
    
	@Test
    public void testTrimToNull_Object(){
    	assertNull(Strings.trimToNull("   "));
    	assertNotNull(Strings.trimToNull("1   "));
    	assertNotNull(Strings.trimToNull("   1"));
    	assertNotNull(Strings.trimToNull("  1  "));
    	assertNull(Strings.trimToNull(""));
    	assertEquals("123", Strings.trimToNull("   123   "));
    }
    
    //-----------------------------------------------------------------------
    @Test
    public void testAbbreviate_StringInt() {
        assertEquals(null, Strings.abbreviate(null, 10));
        assertEquals("", Strings.abbreviate("", 10));
        assertEquals("short", Strings.abbreviate("short", 10));
        assertEquals("Now is ...", Strings.abbreviate("Now is the time for all good men to come to the aid of their party.", 10));

        String raspberry = "raspberry peach";
        assertEquals("raspberry p...", Strings.abbreviate(raspberry, 14));
        assertEquals("raspberry peach", Strings.abbreviate("raspberry peach", 15));
        assertEquals("raspberry peach", Strings.abbreviate("raspberry peach", 16));
        assertEquals("abc...", Strings.abbreviate("abcdefg", 6));
        assertEquals("abcdefg", Strings.abbreviate("abcdefg", 7));
        assertEquals("abcdefg", Strings.abbreviate("abcdefg", 8));
        assertEquals("a...", Strings.abbreviate("abcdefg", 4));
        assertEquals("", Strings.abbreviate("", 4));
        
        try {
            @SuppressWarnings("unused")
            String res = Strings.abbreviate("abc", 3);
            fail("Strings.abbreviate expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
                // empty
        }              
    }
    
    @Test
    public void testAbbreviate_StringIntInt() {
        assertEquals(null, Strings.abbreviate(null, 10, 12));
        assertEquals("", Strings.abbreviate("", 0, 10));
        assertEquals("", Strings.abbreviate("", 2, 10));
        
        try {
            @SuppressWarnings("unused")
            String res = Strings.abbreviate("abcdefghij", 0, 3);
            fail("Strings.abbreviate expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
                // empty
        }      
        try {
            @SuppressWarnings("unused")
            String res = Strings.abbreviate("abcdefghij", 5, 6);
            fail("Strings.abbreviate expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
                // empty
        }      
        

        String raspberry = "raspberry peach";
        assertEquals("raspberry peach", Strings.abbreviate(raspberry, 11, 15));

        assertEquals(null, Strings.abbreviate(null, 7, 14));
        assertAbbreviateWithOffset("abcdefg...", -1, 10);
        assertAbbreviateWithOffset("abcdefg...", 0, 10);
        assertAbbreviateWithOffset("abcdefg...", 1, 10);
        assertAbbreviateWithOffset("abcdefg...", 2, 10);
        assertAbbreviateWithOffset("abcdefg...", 3, 10);
        assertAbbreviateWithOffset("abcdefg...", 4, 10);
        assertAbbreviateWithOffset("...fghi...", 5, 10);
        assertAbbreviateWithOffset("...ghij...", 6, 10);
        assertAbbreviateWithOffset("...hijk...", 7, 10);
        assertAbbreviateWithOffset("...ijklmno", 8, 10);
        assertAbbreviateWithOffset("...ijklmno", 9, 10);
        assertAbbreviateWithOffset("...ijklmno", 10, 10);
        assertAbbreviateWithOffset("...ijklmno", 10, 10);
        assertAbbreviateWithOffset("...ijklmno", 11, 10);
        assertAbbreviateWithOffset("...ijklmno", 12, 10);
        assertAbbreviateWithOffset("...ijklmno", 13, 10);
        assertAbbreviateWithOffset("...ijklmno", 14, 10);
        assertAbbreviateWithOffset("...ijklmno", 15, 10);
        assertAbbreviateWithOffset("...ijklmno", 16, 10);
        assertAbbreviateWithOffset("...ijklmno", Integer.MAX_VALUE, 10);
    }

    private void assertAbbreviateWithOffset(String expected, int offset, int maxWidth) {
        String abcdefghijklmno = "abcdefghijklmno";
        String message = "abbreviate(String,int,int) failed";
        String actual = Strings.abbreviate(abcdefghijklmno, offset, maxWidth);
        if (offset >= 0 && offset < abcdefghijklmno.length()) {
            assertTrue(message + " -- should contain offset character",
                    actual.indexOf((char)('a'+offset)) != -1);
        }
        assertTrue(message + " -- should not be greater than maxWidth",
                actual.length() <= maxWidth);
        assertEquals(message, expected, actual);
    }

    @Test
    public void testAbbreviateMiddle() {
        // javadoc examples
        assertNull( Strings.abbreviateMiddle(null, null, 0) );
        assertEquals( "abc", Strings.abbreviateMiddle("abc", null, 0) );
        assertEquals( "abc", Strings.abbreviateMiddle("abc", ".", 0) );
        assertEquals( "abc", Strings.abbreviateMiddle("abc", ".", 3) );
        assertEquals( "ab.f", Strings.abbreviateMiddle("abcdef", ".", 4) );
        assertEquals("ab...j",Strings.abbreviateMiddle("abcdefghij", "...", 6));

        // JIRA issue (LANG-405) example (slightly different than actual expected result)
        assertEquals( 
            "A very long text with un...f the text is complete.",
            Strings.abbreviateMiddle(
                "A very long text with unimportant stuff in the middle but interesting start and " +
                "end to see if the text is complete.", "...", 50) );

        // Test a much longer text :)
        String longText = "Start text" + Strings.repeat("x", 10000) + "Close text";
        assertEquals( 
            "Start text->Close text",
            Strings.abbreviateMiddle( longText, "->", 22 ) );

        // Test negative length
        assertEquals("abc", Strings.abbreviateMiddle("abc", ".", -1));

        // Test boundaries
        // Fails to change anything as method ensures first and last char are kept
        assertEquals("abc", Strings.abbreviateMiddle("abc", ".", 1));
        assertEquals("abc", Strings.abbreviateMiddle("abc", ".", 2));

        // Test length of n=1
        assertEquals("a", Strings.abbreviateMiddle("a", ".", 1));

        // Test smallest length that can lead to success
        assertEquals("a.d", Strings.abbreviateMiddle("abcd", ".", 3));

        // More from LANG-405
        assertEquals("a..f", Strings.abbreviateMiddle("abcdef", "..", 4));
        assertEquals("ab.ef", Strings.abbreviateMiddle("abcdef", ".", 5));
    }    
    
    @Test
    public void testLowerCamel(){
    	assertEquals("", Strings.lowerCamel(null, '_'));
    	assertEquals("", Strings.lowerCamel("", '_'));
    	assertEquals("helloWorld", Strings.lowerCamel("hello_world", '_'));
    	assertEquals("helloWorld", Strings.lowerCamel("hello_World", '_'));
    	assertEquals("helloWorld", Strings.lowerCamel("Hello_World", '_'));
    	assertEquals("helloWorld", Strings.lowerCamel("HeLlo_WOrld", '_'));
    	assertEquals("aaBbCc", Strings.lowerCamel("aa_bb_cc", '_'));
    }
    
    @Test
    public void testUpperCamel(){
    	assertEquals("", Strings.upperCamel(null, '_'));
    	assertEquals("", Strings.upperCamel("", '_'));
    	assertEquals("HelloWorld", Strings.upperCamel("hello_world", '_'));
    	assertEquals("HelloWorld", Strings.upperCamel("hello_World", '_'));
    	assertEquals("HelloWorld", Strings.upperCamel("Hello_World", '_'));
    	assertEquals("HelloWorld", Strings.upperCamel("HeLlo_WOrld", '_'));
    	assertEquals("AaBbCc", Strings.upperCamel("aa_bb_cc", '_'));
    }
    
	private static class SubStrings extends Strings {
		public SubStrings() {
			super();
		}
	}
}