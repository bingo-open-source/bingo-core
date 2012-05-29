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

package bingo.utils.codec.digest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Test;

import bingo.lang.Strings;
import bingo.lang.testing.junit.ConcurrentTestCase;
import bingo.utils.codec.digest.Digests;

/**
 * Tests DigestUtils methods.
 * 
 * @author Apache Software Foundation
 * @version $Id: DigestUtilsTest.java 1157192 2011-08-12 17:27:38Z ggregory $
 */
public class DigestsTest extends ConcurrentTestCase {

    private final byte[] testData = new byte[1024*1024];
    
    private byte[] getBytesUtf8(String hashMe) {
        return Strings.getBytesUtf8(hashMe);
    }

    @Test
    public void testConstructable() {
        new Digests();
    }
    
    @Test
    public void testInternalNoSuchAlgorithmException() {
        try {
            Digests.getDigest("Bogus Bogus");
            fail("A RuntimeException should have been thrown.");
        } catch (RuntimeException e) {
            // Expected exception.
        }
    }

    @Test
    public void testMd5Hex() throws IOException {
        // Examples from RFC 1321
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Digests.md5Hex(""));

        assertEquals("0cc175b9c0f1b6a831c399e269772661", Digests.md5Hex("a"));

        assertEquals("900150983cd24fb0d6963f7d28e17f72", Digests.md5Hex("abc"));

        assertEquals("f96b697d7cb7938d525a2f31aaf161d0", Digests.md5Hex("message digest"));

        assertEquals("c3fcd3d76192e4007dfb496cca67e13b", Digests.md5Hex("abcdefghijklmnopqrstuvwxyz"));

        assertEquals(
            "d174ab98d277d9f5a5611c2c9f419d9f",
            Digests.md5Hex("ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789"));

        assertEquals(
            "57edf4a22be3c955ac49da2e2107b67a",
            Digests.md5Hex("1234567890123456789012345678901234567890" + "1234567890123456789012345678901234567890"));

        assertEquals(Digests.md5Hex(testData),
                Digests.md5Hex(new ByteArrayInputStream(testData)));
    }

    /**
     * An MD5 hash converted to hex should always be 32 characters.
     */
    @Test
    public void testMD5HexLength() {
        String hashMe = "this is some string that is longer than 32 characters";
        String hash = Digests.md5Hex(getBytesUtf8(hashMe));
        assertEquals(32, hash.length());

        hashMe = "length < 32";
        hash = Digests.md5Hex(getBytesUtf8(hashMe));
        assertEquals(32, hash.length());
    }

    /**
     * An MD5 hash should always be a 16 element byte[].
     */
    @Test
    public void testMD5Length() {
        String hashMe = "this is some string that is longer than 16 characters";
        byte[] hash = Digests.md5(getBytesUtf8(hashMe));
        assertEquals(16, hash.length);

        hashMe = "length < 16";
        hash = Digests.md5(getBytesUtf8(hashMe));
        assertEquals(16, hash.length);
    }

    @Test
    public void testSha256() throws IOException {
    // Examples from FIPS 180-2
    assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
             Digests.sha256Hex("abc"));
    assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
             Digests.sha256Hex(getBytesUtf8("abc")));
    assertEquals("248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
             Digests.sha256Hex("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"));
    
    assertEquals(Digests.sha256Hex(testData),
            Digests.sha256Hex(new ByteArrayInputStream(testData)));
    }    

    @Test
    public void testSha384() throws IOException {
    // Examples from FIPS 180-2
    assertEquals("cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed" + 
             "8086072ba1e7cc2358baeca134c825a7",
             Digests.sha384Hex("abc"));
    assertEquals("cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed" + 
             "8086072ba1e7cc2358baeca134c825a7",
             Digests.sha384Hex(getBytesUtf8("abc")));
    assertEquals("09330c33f71147e83d192fc782cd1b4753111b173b3b05d22fa08086e3b0f712" +
            "fcc7c71a557e2db966c3e9fa91746039",
             Digests.sha384Hex("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmn" +
                       "hijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"));
    assertEquals(Digests.sha384Hex(testData),
            Digests.sha384Hex(new ByteArrayInputStream(testData)));
    }

    @Test
    public void testSha512() throws IOException {
    // Examples from FIPS 180-2
    assertEquals("ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a" +
            "2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f",
             Digests.sha512Hex("abc"));
    assertEquals("ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a" +
             "2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f",
             Digests.sha512Hex(getBytesUtf8("abc")));
    assertEquals("8e959b75dae313da8cf4f72814fc143f8f7779c6eb9f7fa17299aeadb6889018" + 
             "501d289e4900f7e4331b99dec4b5433ac7d329eeb6dd26545e96e55b874be909",
             Digests.sha512Hex("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmn" + 
                       "hijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"));
    assertEquals(Digests.sha512Hex(testData),
            Digests.sha512Hex(new ByteArrayInputStream(testData)));
    }    

    @Test
    public void testShaHex() throws IOException {
        // Examples from FIPS 180-1
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", Digests.shaHex("abc"));

        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", Digests.shaHex(getBytesUtf8("abc")));

        assertEquals(
            "84983e441c3bd26ebaae4aa1f95129e5e54670f1",
            Digests.shaHex("abcdbcdecdefdefgefghfghighij" + "hijkijkljklmklmnlmnomnopnopq"));
        assertEquals(Digests.shaHex(testData),
                Digests.shaHex(new ByteArrayInputStream(testData)));

    }
}
