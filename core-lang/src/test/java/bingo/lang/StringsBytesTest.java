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

package bingo.lang;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import bingo.lang.testing.junit.ConcurrentTestCase;

/**
 * Tests {@link Strings}
 */
public class StringsBytesTest extends ConcurrentTestCase {

    private static final byte[] BYTES_FIXTURE = {'a','b','c'};

    private static final String STRING_FIXTURE = "ABC";

    @Test
    public void testGetBytesIso8859_1() throws UnsupportedEncodingException {
        String charsetName = "ISO-8859-1";
        testGetBytesUnchecked(charsetName);
        byte[] expected = STRING_FIXTURE.getBytes(charsetName);
        byte[] actual = Strings.getBytesIso8859_1(STRING_FIXTURE);
        Assert.assertTrue(Arrays.equals(expected, actual));
    }

    private void testGetBytesUnchecked(String charsetName) throws UnsupportedEncodingException {
        byte[] expected = STRING_FIXTURE.getBytes(charsetName);
        byte[] actual = Strings.getBytes(STRING_FIXTURE, charsetName);
        Assert.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testGetBytesUsAscii() throws UnsupportedEncodingException {
        String charsetName = "US-ASCII";
        testGetBytesUnchecked(charsetName);
        byte[] expected = STRING_FIXTURE.getBytes(charsetName);
        byte[] actual = Strings.getBytesUsAscii(STRING_FIXTURE);
        Assert.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testGetBytesUtf8() throws UnsupportedEncodingException {
        String charsetName = "UTF-8";
        testGetBytesUnchecked(charsetName);
        byte[] expected = STRING_FIXTURE.getBytes(charsetName);
        byte[] actual = Strings.getBytesUtf8(STRING_FIXTURE);
        Assert.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testGetBytesUncheckedBadName() {
        try {
            Strings.getBytes(STRING_FIXTURE, "UNKNOWN");
            Assert.fail("Expected " + IllegalStateException.class.getName());
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    private void testNewString(String charsetName) throws UnsupportedEncodingException {
        String expected = new String(BYTES_FIXTURE, charsetName);
        String actual = Strings.newString(BYTES_FIXTURE, charsetName);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNewStringBadEnc() {
        try {
            Strings.newString(BYTES_FIXTURE, "UNKNOWN");
            Assert.fail("Expected " + IllegalStateException.class.getName());
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNewStringIso8859_1() throws UnsupportedEncodingException {
        String charsetName = "ISO-8859-1";
        testNewString(charsetName);
        String expected = new String(BYTES_FIXTURE, charsetName);
        String actual = Strings.newStringIso8859_1(BYTES_FIXTURE);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNewStringUsAscii() throws UnsupportedEncodingException {
        String charsetName = "US-ASCII";
        testNewString(charsetName);
        String expected = new String(BYTES_FIXTURE, charsetName);
        String actual = Strings.newStringUsAscii(BYTES_FIXTURE);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNewStringUtf8() throws UnsupportedEncodingException {
        String charsetName = "UTF-8";
        testNewString(charsetName);
        String expected = new String(BYTES_FIXTURE, charsetName);
        String actual = Strings.newStringUtf8(BYTES_FIXTURE);
        Assert.assertEquals(expected, actual);
    }
}
