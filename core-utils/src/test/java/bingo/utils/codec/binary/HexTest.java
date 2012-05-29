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

package bingo.utils.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import bingo.lang.Strings;
import bingo.lang.testing.junit.ConcurrentTestCase;
import bingo.utils.codec.DecoderException;
import bingo.utils.codec.EncoderException;

/**
 * Tests {@link org.apache.commons.codec.binary.Hex}.
 * 
 * @author Apache Software Foundation
 * @version $Id: HexTest.java 1157192 2011-08-12 17:27:38Z ggregory $
 */
public class HexTest extends ConcurrentTestCase {

    private void checkDecodeHexOddCharacters(char[] data) {
        try {
            Hex.decode(data);
            fail("An exception wasn't thrown when trying to decode an odd number of characters");
        } catch (DecoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testDecodeBadCharacterPos0() {
        try {
            Hex.decode("q0");
            fail("An exception wasn't thrown when trying to decode an illegal character");
        } catch (DecoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testDecodeBadCharacterPos1() {
        try {
            Hex.decode("0q");
            fail("An exception wasn't thrown when trying to decode an illegal character");
        } catch (DecoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testDecodeHexOddCharacters1() {
        checkDecodeHexOddCharacters(new char[]{'A'});
    }

    @Test
    public void testDecodeHexOddCharacters3() {
        checkDecodeHexOddCharacters(new char[]{'A', 'B', 'C'});
    }

    @Test
    public void testDecodeHexOddCharacters5() {
        checkDecodeHexOddCharacters(new char[]{'A', 'B', 'C', 'D', 'E'});
    }

    @Test
    public void testDecodeStringOddCharacters() {
        try {
            Hex.decode("6");
            fail("An exception wasn't thrown when trying to decode an odd number of characters");
        } catch (DecoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testDencodeEmpty() throws DecoderException {
        assertTrue(Arrays.equals(new byte[0], Hex.decode(new char[0])));
        assertTrue(Arrays.equals(new byte[0], (byte[]) Hex.decode("")));
    }

    @Test
    public void testEncodeDecodeRandom() throws DecoderException, EncoderException {
        Random random = new Random();

        for (int i = 5; i > 0; i--) {
            byte[] data = new byte[random.nextInt(10000) + 1];
            random.nextBytes(data);

            // static API
            char[] encodedChars = Hex.encodeToChars(data);
            byte[] decodedBytes = Hex.decode(encodedChars);
            assertTrue(Arrays.equals(data, decodedBytes));
        }
    }

    @Test
    public void testEncodeEmpty() throws EncoderException {
        assertTrue(Arrays.equals(new char[0], Hex.encodeToChars(new byte[0])));
    }

    @Test
    public void testEncodeZeroes() {
        char[] c = Hex.encodeToChars(new byte[36]);
        assertEquals("000000000000000000000000000000000000000000000000000000000000000000000000", new String(c));
    }

    @Test
    public void testHelloWorldLowerCaseHex() {
        byte[] b = Strings.getBytesUtf8("Hello World");
        final String expected = "48656c6c6f20576f726c64";
        char[] actual;
        actual = Hex.encodeToChars(b);
        assertTrue(expected.equals(new String(actual)));
        actual = Hex.encodeToChars(b, true);
        assertTrue(expected.equals(new String(actual)));
        actual = Hex.encodeToChars(b, false);
        assertFalse(expected.equals(new String(actual)));
    }

    @Test
    public void testHelloWorldUpperCaseHex() {
        byte[] b = Strings.getBytesUtf8("Hello World");
        final String expected = "48656C6C6F20576F726C64";
        char[] actual;
        actual = Hex.encodeToChars(b);
        assertFalse(expected.equals(new String(actual)));
        actual = Hex.encodeToChars(b, true);
        assertFalse(expected.equals(new String(actual)));
        actual = Hex.encodeToChars(b, false);
        assertTrue(expected.equals(new String(actual)));
    }
}
