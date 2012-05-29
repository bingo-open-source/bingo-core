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

import static bingo.lang.StringsTest.ARRAY_LIST;
import static bingo.lang.StringsTest.EMPTY_ARRAY_LIST;
import static bingo.lang.StringsTest.MIXED_ARRAY_LIST;
import static bingo.lang.StringsTest.MIXED_TYPE_LIST;
import static bingo.lang.StringsTest.NON_WHITESPACE;
import static bingo.lang.StringsTest.NULL_ARRAY_LIST;
import static bingo.lang.StringsTest.NULL_TO_STRING_LIST;
import static bingo.lang.StringsTest.SEPARATOR;
import static bingo.lang.StringsTest.SEPARATOR_CHAR;
import static bingo.lang.StringsTest.TEXT_LIST;
import static bingo.lang.StringsTest.TEXT_LIST_CHAR;
import static bingo.lang.StringsTest.TEXT_LIST_NOSEP;

import java.util.Collections;
import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import bingo.lang.testing.junit.ConcurrentTestCase;

import static org.junit.Assert.*;

/**
 * {@link TestCase} of {@link Strings}
 */
public class StringsJoinSplitTest extends ConcurrentTestCase {
	
    //-----------------------------------------------------------------------
	
	@Test
    public void testJoin_Objects() {
        assertEquals("a,b,c", Strings.join("a", "b", "c"));
        assertEquals(",,a",   Strings.join(null, "", "a"));
        assertEquals(Strings.EMPTY,  Strings.join((Object[])null));
    }

	@Test
    public void testJoin_Objectarray() {
//        assertEquals(null, Strings.join(null)); // generates warning
        assertEquals("", Strings.join((Object[]) null)); // equivalent explicit cast
        // test additional varargs calls
        assertEquals("", Strings.join()); // empty array
        assertEquals("", Strings.join((Object) null)); // => new Object[]{null}

        assertEquals("", Strings.join(EMPTY_ARRAY_LIST));
        assertEquals("", Strings.join(NULL_ARRAY_LIST));
        assertEquals("null", Strings.join(NULL_TO_STRING_LIST));
        assertEquals("a,b,c", Strings.join(new String[] {"a", "b", "c"}));
        assertEquals(",a,", Strings.join(new String[] {null, "a", ""}));
        assertEquals(",,foo", Strings.join(MIXED_ARRAY_LIST));
        assertEquals("foo,2", Strings.join(MIXED_TYPE_LIST));
    }
        
	@Test
    public void testJoin_ArrayChar() {
        assertEquals("", Strings.join((Object[]) null, ','));
        assertEquals(TEXT_LIST_CHAR, Strings.join(ARRAY_LIST, SEPARATOR_CHAR));
        assertEquals("", Strings.join(EMPTY_ARRAY_LIST, SEPARATOR_CHAR));
        assertEquals(";;foo", Strings.join(MIXED_ARRAY_LIST, SEPARATOR_CHAR));
        assertEquals("foo;2", Strings.join(MIXED_TYPE_LIST, SEPARATOR_CHAR));
    }
    
    @Test
    public void testJoin_ArrayString() {
        assertEquals("", Strings.join((Object[]) null, null));
        assertEquals(TEXT_LIST_NOSEP, Strings.join(ARRAY_LIST, null));
        assertEquals(TEXT_LIST_NOSEP, Strings.join(ARRAY_LIST, ""));
        
        assertEquals("", Strings.join(NULL_ARRAY_LIST, null));
        
        assertEquals("", Strings.join(EMPTY_ARRAY_LIST, null));
        assertEquals("", Strings.join(EMPTY_ARRAY_LIST, ""));
        assertEquals("", Strings.join(EMPTY_ARRAY_LIST, SEPARATOR));

        assertEquals(TEXT_LIST, Strings.join(ARRAY_LIST, SEPARATOR));
        assertEquals(",,foo", Strings.join(MIXED_ARRAY_LIST, SEPARATOR));
        assertEquals("foo,2", Strings.join(MIXED_TYPE_LIST, SEPARATOR));
    }
    
    @Test
    public void testJoin_IteratorChar() {
        assertEquals("", Strings.join((Iterator<?>) null, ','));
        assertEquals(TEXT_LIST_CHAR, Strings.join(Arrays.toList(ARRAY_LIST).iterator(), SEPARATOR_CHAR));
        assertEquals("", Strings.join(Arrays.toList(NULL_ARRAY_LIST).iterator(), SEPARATOR_CHAR));
        assertEquals("", Strings.join(Arrays.toList(EMPTY_ARRAY_LIST).iterator(), SEPARATOR_CHAR));
        assertEquals("foo", Strings.join(Collections.singleton("foo").iterator(), 'x'));
    }
    
    @Test
    public void testJoin_IteratorString() {
        assertEquals("", Strings.join((Iterator<?>) null, null));
        assertEquals(TEXT_LIST_NOSEP, Strings.join(Arrays.toList(ARRAY_LIST).iterator(), null));
        assertEquals(TEXT_LIST_NOSEP, Strings.join(Arrays.toList(ARRAY_LIST).iterator(), ""));
        assertEquals("foo", Strings.join(Collections.singleton("foo").iterator(), "x"));
        assertEquals("foo", Strings.join(Collections.singleton("foo").iterator(), null));

        assertEquals("", Strings.join(Arrays.toList(NULL_ARRAY_LIST).iterator(), null));
        
        assertEquals("", Strings.join(Arrays.toList(EMPTY_ARRAY_LIST).iterator(), null));
        assertEquals("", Strings.join(Arrays.toList(EMPTY_ARRAY_LIST).iterator(), ""));
        assertEquals("", Strings.join(Arrays.toList(EMPTY_ARRAY_LIST).iterator(), SEPARATOR));
        
        assertEquals(TEXT_LIST, Strings.join(Arrays.toList(ARRAY_LIST).iterator(), SEPARATOR));
    }

    @Test
    public void testJoin_IterableChar() {
        assertEquals("", Strings.join((Iterable<?>) null, ','));
        assertEquals(TEXT_LIST_CHAR, Strings.join(Arrays.toList(ARRAY_LIST), SEPARATOR_CHAR));
        assertEquals("", Strings.join(Arrays.toList(NULL_ARRAY_LIST), SEPARATOR_CHAR));
        assertEquals("", Strings.join(Arrays.toList(EMPTY_ARRAY_LIST), SEPARATOR_CHAR));
        assertEquals("foo", Strings.join(Collections.singleton("foo"), 'x'));
    }

    @Test
    public void testJoin_IterableString() {
        assertEquals("", Strings.join((Iterable<?>) null, null));
        assertEquals(TEXT_LIST_NOSEP, Strings.join(Arrays.toList(ARRAY_LIST), null));
        assertEquals(TEXT_LIST_NOSEP, Strings.join(Arrays.toList(ARRAY_LIST), ""));
        assertEquals("foo", Strings.join(Collections.singleton("foo"), "x"));
        assertEquals("foo", Strings.join(Collections.singleton("foo"), null));

        assertEquals("", Strings.join(Arrays.toList(NULL_ARRAY_LIST), null));

        assertEquals("", Strings.join(Arrays.toList(EMPTY_ARRAY_LIST), null));
        assertEquals("", Strings.join(Arrays.toList(EMPTY_ARRAY_LIST), ""));
        assertEquals("", Strings.join(Arrays.toList(EMPTY_ARRAY_LIST), SEPARATOR));

        assertEquals(TEXT_LIST, Strings.join(Arrays.toList(ARRAY_LIST), SEPARATOR));
    }
    
    @Test
    public void testSplit_String() {
    	Assert.assertArrayEquals(Arrays.EMPTY_STRING_ARRAY, Strings.split(null));
        assertEquals(0, Strings.split("").length);
        
        String str = "a,b,  .c";
        String[] res = Strings.split(str);
        assertEquals(3, res.length);
        assertEquals("a", res[0]);
        assertEquals("b", res[1]);
        assertEquals(".c", res[2]);
        
        str = "a,b,  .c";
        res = Strings.split(str,Strings.COMMA,false);
        assertEquals(3, res.length);
        assertEquals("a", res[0]);
        assertEquals("b", res[1]);
        assertEquals("  .c", res[2]);        
        
        str = " a ";
        res = Strings.split(str);
        assertEquals(1, res.length);
        assertEquals("a", res[0]);
        
        str = "a" + Strings.COMMA + "b" + NON_WHITESPACE + "c";
        res = Strings.split(str);
        assertEquals(2, res.length);
        assertEquals("a", res[0]);
        assertEquals("b" + NON_WHITESPACE + "c", res[1]);                       
    }
    
    @Test
    public void testSplit_StringChar() {
    	Assert.assertArrayEquals(Arrays.EMPTY_STRING_ARRAY, Strings.split(null, '.'));
        assertEquals(0, Strings.split("", '.').length);

        String str = "a.b.. c";
        String[] res = Strings.split(str, '.');
        assertEquals(3, res.length);
        assertEquals("a", res[0]);
        assertEquals("b", res[1]);
        assertEquals("c", res[2]);
        
        str = "a.b.. c";
        res = Strings.split(str, '.', false);
        assertEquals(3, res.length);
        assertEquals("a", res[0]);
        assertEquals("b", res[1]);
        assertEquals(" c", res[2]);        
            
        str = ".a.";
        res = Strings.split(str, '.');
        assertEquals(1, res.length);
        assertEquals("a", res[0]);
        
        str = "a b c";
        res = Strings.split(str,' ');
        assertEquals(3, res.length);
        assertEquals("a", res[0]);
        assertEquals("b", res[1]);
        assertEquals("c", res[2]);
        
        str = "a^_^b^_^c";
        res = Strings.split(str,"^_^");
        assertEquals(3, res.length);
        assertEquals("a", res[0]);
        assertEquals("b", res[1]);
        assertEquals("c", res[2]);        
    }
}
