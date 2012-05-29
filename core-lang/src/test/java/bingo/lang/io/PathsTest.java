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
package bingo.lang.io;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import bingo.lang.testing.junit.ConcurrentTestCase;

public class PathsTest extends ConcurrentTestCase {

	@Test
	public void testGetFilename() {
		assertEquals("", Paths.getFileName(null));
		assertEquals("", Paths.getFileName(""));
		assertEquals("myfile", Paths.getFileName("myfile"));
		assertEquals("myfile", Paths.getFileName("mypath/myfile"));
		assertEquals("myfile.", Paths.getFileName("myfile."));
		assertEquals("myfile.", Paths.getFileName("mypath/myfile."));
		assertEquals("myfile.txt", Paths.getFileName("myfile.txt"));
		assertEquals("myfile.txt", Paths.getFileName("mypath/myfile.txt"));
	}

	@Test
	public void testGetFilenameExtension() {
		assertEquals("", Paths.getFileExtension(null));
		assertEquals("",    Paths.getFileExtension(""));
		assertEquals("", Paths.getFileExtension("myfile"));
		assertEquals("", Paths.getFileExtension("myPath/myfile"));
		assertEquals("", Paths.getFileExtension("/home/user/.m2/settings/myfile"));
		assertEquals("", Paths.getFileExtension("myfile."));
		assertEquals("", Paths.getFileExtension("myPath/myfile."));
		assertEquals("txt", Paths.getFileExtension("myfile.txt"));
		assertEquals("txt", Paths.getFileExtension("mypath/myfile.txt"));
		assertEquals("txt", Paths.getFileExtension("/home/user/.m2/settings/myfile.txt"));
	}
	
	@Test
	public void testCleanPath() {
		assertEquals("mypath/myfile", Paths.normalize("mypath/myfile"));
		assertEquals("mypath/myfile", Paths.normalize("mypath\\myfile"));
		assertEquals("mypath/myfile", Paths.normalize("mypath/../mypath/myfile"));
		assertEquals("mypath/myfile", Paths.normalize("mypath/myfile/../../mypath/myfile"));
		assertEquals("../mypath/myfile", Paths.normalize("../mypath/myfile"));
		assertEquals("../mypath/myfile", Paths.normalize("../mypath/../mypath/myfile"));
		assertEquals("../mypath/myfile", Paths.normalize("mypath/../../mypath/myfile"));
		assertEquals("../mypath1/myfile", Paths.normalize("mypath/../../mypath1/myfile"));
		assertEquals("/../mypath/myfile", Paths.normalize("/../mypath/myfile"));
	}
}
