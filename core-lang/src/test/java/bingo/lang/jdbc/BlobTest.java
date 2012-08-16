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
package bingo.lang.jdbc;

import org.junit.Assert;
import org.junit.Test;

import bingo.lang.io.IO;

public class BlobTest extends Assert {

	@Test
	public void testSimpleBlob() throws Exception {
		byte[] data = "0123456789".getBytes();
		
		BlobImpl blob = new BlobImpl(data);
		
		assertArrayEquals(data, IO.toByteArray(blob.getBinaryStream()));
	}

}
