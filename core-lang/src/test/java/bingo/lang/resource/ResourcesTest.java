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
package bingo.lang.resource;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.junit.Test;

import static org.junit.Assert.*;


public class ResourcesTest {
	
	@Test
	public void testIsURL() throws Exception {
		assertFalse(Resources.isUrl("bingo/lang/resource/Resource.class"));
		assertTrue(Resources.isUrl("classpath:bingo/lang/resource/Resource.class"));
	}
	
	@Test
	public void testGetResource() throws Exception {
		Resource res = Resources.getResource(Resource.class,"Resource.class");
		assertNotNull(res);
		assertTrue(res.exists());
		
		res = Resources.getResource("bingo/lang/resource/Resource.class");
		assertNotNull(res);
		assertTrue(res.exists());
	}
	
	@Test
	public void testIsJarURL() throws Exception {
		assertTrue(Resources.isJarURL(new URL("jar:file:myjar.jar!/mypath")));
		assertTrue(Resources.isJarURL(new URL(null, "zip:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertTrue(Resources.isJarURL(new URL(null, "wsjar:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertFalse(Resources.isJarURL(new URL("file:myjar.jar")));
		assertFalse(Resources.isJarURL(new URL("http:myserver/myjar.jar")));
	}

	@Test
	public void testExtractJarFileURL() throws Exception {
		assertEquals(new URL("file:myjar.jar"),
				Resources.extractJarFileURL(new URL("jar:file:myjar.jar!/mypath")));
		assertEquals(new URL("file:/myjar.jar"),
				Resources.extractJarFileURL(new URL(null, "jar:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				Resources.extractJarFileURL(new URL(null, "zip:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				Resources.extractJarFileURL(new URL(null, "wsjar:file:myjar.jar!/mypath", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				Resources.extractJarFileURL(new URL("jar:file:myjar.jar!/")));
		assertEquals(new URL("file:myjar.jar"),
				Resources.extractJarFileURL(new URL(null, "zip:file:myjar.jar!/", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				Resources.extractJarFileURL(new URL(null, "wsjar:file:myjar.jar!/", new DummyURLStreamHandler())));
		assertEquals(new URL("file:myjar.jar"),
				Resources.extractJarFileURL(new URL("file:myjar.jar")));
	}

	/**
	 * Dummy URLStreamHandler that's just specified to suppress the standard
	 * <code>java.net.URL</code> URLStreamHandler lookup, to be able to
	 * use the standard URL class for parsing "rmi:..." URLs.
	 */
	private static class DummyURLStreamHandler extends URLStreamHandler {

		protected URLConnection openConnection(URL url) throws IOException {
			throw new UnsupportedOperationException();
		}
	}
}
