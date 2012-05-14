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

import bingo.lang.io.Files;

public class FilesTest {

	@Test
	public void testGetFilename() {
		assertEquals(null, Files.getFileName(null));
		assertEquals("", Files.getFileName(""));
		assertEquals("myfile", Files.getFileName("myfile"));
		assertEquals("myfile", Files.getFileName("mypath/myfile"));
		assertEquals("myfile.", Files.getFileName("myfile."));
		assertEquals("myfile.", Files.getFileName("mypath/myfile."));
		assertEquals("myfile.txt", Files.getFileName("myfile.txt"));
		assertEquals("myfile.txt", Files.getFileName("mypath/myfile.txt"));
	}

	@Test
	public void testGetFilenameExtension() {
		assertEquals(null, Files.getFileNameExtension(null));
		assertEquals(null, Files.getFileNameExtension(""));
		assertEquals(null, Files.getFileNameExtension("myfile"));
		assertEquals(null, Files.getFileNameExtension("myPath/myfile"));
		assertEquals(null, Files.getFileNameExtension("/home/user/.m2/settings/myfile"));
		assertEquals("", Files.getFileNameExtension("myfile."));
		assertEquals("", Files.getFileNameExtension("myPath/myfile."));
		assertEquals("txt", Files.getFileNameExtension("myfile.txt"));
		assertEquals("txt", Files.getFileNameExtension("mypath/myfile.txt"));
		assertEquals("txt", Files.getFileNameExtension("/home/user/.m2/settings/myfile.txt"));
	}
	
	@Test
	public void testCleanPath() {
		assertEquals("mypath/myfile", Files.cleanPath("mypath/myfile"));
		assertEquals("mypath/myfile", Files.cleanPath("mypath\\myfile"));
		assertEquals("mypath/myfile", Files.cleanPath("mypath/../mypath/myfile"));
		assertEquals("mypath/myfile", Files.cleanPath("mypath/myfile/../../mypath/myfile"));
		assertEquals("../mypath/myfile", Files.cleanPath("../mypath/myfile"));
		assertEquals("../mypath/myfile", Files.cleanPath("../mypath/../mypath/myfile"));
		assertEquals("../mypath/myfile", Files.cleanPath("mypath/../../mypath/myfile"));
		assertEquals("../mypath1/myfile", Files.cleanPath("mypath/../../mypath1/myfile"));
		assertEquals("/../mypath/myfile", Files.cleanPath("/../mypath/myfile"));
	}
	
	@Test
	public void testApplyRelativePath(){
		assertEquals("/mypath/myfile", Files.applyRelativePath("/mypath/", "myfile"));
		assertEquals("/mypath/myfile", Files.applyRelativePath("/mypath/", "/myfile"));
		assertEquals("/myfile", Files.applyRelativePath("/myfile1", "/myfile"));
		assertEquals("mypath/myfile", Files.applyRelativePath("mypath/", "myfile"));
		assertEquals("mypath/myfile", Files.applyRelativePath("mypath/", "/myfile"));
		assertEquals("mypath/myfile", Files.applyRelativePath("mypath/myfile1", "/myfile"));
	}
}
