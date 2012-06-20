/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.lang.tuple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.junit.Test;

/**
 * Test the Pair class.
 * 
 * @version $Id: PairTest.java 1199724 2011-11-09 12:51:52Z sebb $
 */
public class PairTest {

    @Test
    public void testPairOf() throws Exception {
        Pair<Integer, String> pair = ImmutablePair.of(0, "foo");
        assertTrue(pair instanceof ImmutablePair<?, ?>);
        assertEquals(0, ((ImmutablePair<Integer, String>) pair).getLeft().intValue());
        assertEquals("foo", ((ImmutablePair<Integer, String>) pair).getRight());
        Pair<Object, String> pair2 = ImmutablePair.of(null, "bar");
        assertTrue(pair2 instanceof ImmutablePair<?, ?>);
        assertNull(((ImmutablePair<Object, String>) pair2).getLeft());
        assertEquals("bar", ((ImmutablePair<Object, String>) pair2).getRight());
    }

    @Test
    public void testCompatibilityBetweenPairs() throws Exception {
        PairBase<Integer, String> pair = ImmutablePair.of(0, "foo");
        MutablePair<Integer, String> pair2 = MutablePair.of(0, "foo");
        assertEquals(pair, pair2);
        assertEquals(pair.hashCode(), pair2.hashCode());
        HashSet<PairBase<Integer, String>> set = new HashSet<PairBase<Integer, String>>();
        set.add(pair);
        assertTrue(set.contains(pair2));

        pair2.setRight("bar");
        assertFalse(pair.equals(pair2));
        assertFalse(pair.hashCode() == pair2.hashCode());
    }

    @Test
    public void testMapEntry() throws Exception {
        Pair<Integer, String> pair = ImmutablePair.of(0, "foo");
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "foo");
        Entry<Integer, String> entry = map.entrySet().iterator().next();
        assertEquals(pair, entry);
        assertEquals(pair.hashCode(), entry.hashCode());
    }

    @Test
    public void testComparable1() throws Exception {
        PairBase<String, String> pair1 = ImmutablePair.of("A", "D");
        PairBase<String, String> pair2 = ImmutablePair.of("B", "C");
        assertTrue(pair1.compareTo(pair1) == 0);
        assertTrue(pair1.compareTo(pair2) < 0);
        assertTrue(pair2.compareTo(pair2) == 0);
        assertTrue(pair2.compareTo(pair1) > 0);
    }

    @Test
    public void testComparable2() throws Exception {
        PairBase<String, String> pair1 = ImmutablePair.of("A", "C");
        PairBase<String, String> pair2 = ImmutablePair.of("A", "D");
        assertTrue(pair1.compareTo(pair1) == 0);
        assertTrue(pair1.compareTo(pair2) < 0);
        assertTrue(pair2.compareTo(pair2) == 0);
        assertTrue(pair2.compareTo(pair1) > 0);
    }

    @Test
    public void testToString() throws Exception {
        Pair<String, String> pair = ImmutablePair.of("Key", "Value");
        assertEquals("(Key,Value)", pair.toString());
    }

    @Test
    public void testFormattable_simple() throws Exception {
        Pair<String, String> pair = ImmutablePair.of("Key", "Value");
        assertEquals("(Key,Value)", String.format("%1$s", pair));
    }

    @Test
    public void testFormattable_padded() throws Exception {
        Pair<String, String> pair = ImmutablePair.of("Key", "Value");
        assertEquals("         (Key,Value)", String.format("%1$20s", pair));
    }
}
