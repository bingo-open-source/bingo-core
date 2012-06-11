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
	
	@Test
    public void testGetPath() {
        assertEquals(null, Paths.getPath(null));
        assertEquals("", Paths.getPath("noseperator.inthispath"));
        assertEquals("", Paths.getPath("/noseperator.inthispath"));
        assertEquals("", Paths.getPath("\\noseperator.inthispath"));
        assertEquals("a/b/", Paths.getPath("a/b/c.txt"));
        assertEquals("a/b/", Paths.getPath("a/b/c"));
        assertEquals("a/b/c/", Paths.getPath("a/b/c/"));
        assertEquals("a\\b\\", Paths.getPath("a\\b\\c"));
        
        assertEquals(null, Paths.getPath(":"));
        assertEquals(null, Paths.getPath("1:/a/b/c.txt"));
        assertEquals(null, Paths.getPath("1:"));
        assertEquals(null, Paths.getPath("1:a"));
        assertEquals(null, Paths.getPath("///a/b/c.txt"));
        assertEquals(null, Paths.getPath("//a"));
        
        assertEquals("", Paths.getPath(""));
        assertEquals("", Paths.getPath("C:"));
        assertEquals("", Paths.getPath("C:/"));
        assertEquals("", Paths.getPath("//server/"));
        assertEquals("", Paths.getPath("~"));
        assertEquals("", Paths.getPath("~/"));
        assertEquals("", Paths.getPath("~user"));
        assertEquals("", Paths.getPath("~user/"));
        
        assertEquals("a/b/", Paths.getPath("a/b/c.txt"));
        assertEquals("a/b/", Paths.getPath("/a/b/c.txt"));
        assertEquals("", Paths.getPath("C:a"));
        assertEquals("a/b/", Paths.getPath("C:a/b/c.txt"));
        assertEquals("a/b/", Paths.getPath("C:/a/b/c.txt"));
        assertEquals("a/b/", Paths.getPath("//server/a/b/c.txt"));
        assertEquals("a/b/", Paths.getPath("~/a/b/c.txt"));
        assertEquals("a/b/", Paths.getPath("~user/a/b/c.txt"));
    }

	@Test
    public void testGetFullPath() {
        assertEquals(null, Paths.getFullPath(null));
        assertEquals("", Paths.getFullPath("noseperator.inthispath"));
        assertEquals("a/b/", Paths.getFullPath("a/b/c.txt"));
        assertEquals("a/b/", Paths.getFullPath("a/b/c"));
        assertEquals("a/b/c/", Paths.getFullPath("a/b/c/"));
        assertEquals("a\\b\\", Paths.getFullPath("a\\b\\c"));
        
        assertEquals(null, Paths.getFullPath(":"));
        assertEquals(null, Paths.getFullPath("1:/a/b/c.txt"));
        assertEquals(null, Paths.getFullPath("1:"));
        assertEquals(null, Paths.getFullPath("1:a"));
        assertEquals(null, Paths.getFullPath("///a/b/c.txt"));
        assertEquals(null, Paths.getFullPath("//a"));
        
        assertEquals("", Paths.getFullPath(""));
        assertEquals("C:", Paths.getFullPath("C:"));
        assertEquals("C:/", Paths.getFullPath("C:/"));
        assertEquals("//server/", Paths.getFullPath("//server/"));
        assertEquals("~/", Paths.getFullPath("~"));
        assertEquals("~/", Paths.getFullPath("~/"));
        assertEquals("~user/", Paths.getFullPath("~user"));
        assertEquals("~user/", Paths.getFullPath("~user/"));
        
        assertEquals("a/b/", Paths.getFullPath("a/b/c.txt"));
        assertEquals("/a/b/", Paths.getFullPath("/a/b/c.txt"));
        assertEquals("C:", Paths.getFullPath("C:a"));
        assertEquals("C:a/b/", Paths.getFullPath("C:a/b/c.txt"));
        assertEquals("C:/a/b/", Paths.getFullPath("C:/a/b/c.txt"));
        assertEquals("//server/a/b/", Paths.getFullPath("//server/a/b/c.txt"));
        assertEquals("~/a/b/", Paths.getFullPath("~/a/b/c.txt"));
        assertEquals("~user/a/b/", Paths.getFullPath("~user/a/b/c.txt"));
    }	
}
