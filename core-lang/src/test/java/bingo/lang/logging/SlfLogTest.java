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
package bingo.lang.logging;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.impl.SimpleContext;

public class SlfLogTest {
	
	private static final Log log = LogFactory.get(SlfLogTest.class);
	
	private SimpleContext context = SimpleContext.get();
	
	private String msg0 = "hello";
	private String msg1 = "hello {}";
	private String arg0 = "logging";
	private String msg2 = "hello logging";
	private Throwable t = new Throwable("error");
	
	@Before
	public void setup(){
		context.isTraceEnabled = true;
		context.isDebugEnabled = true;
		context.isInfoEnabled  = true;
		context.isWarnEnabled  = true;
		context.isErrorEanbled = true;
	}
	
	@Test
	public void testGetLog(){
		assertNotNull(log);
		assertTrue(log instanceof SlfLog);
	}

	@Test
	public void testLevelEnabled() {
		context.isTraceEnabled = false;
		assertFalse(log.isTraceEnabled());
		
		context.isDebugEnabled= false;
		assertFalse(log.isDebugEnabled());		
		
		context.isInfoEnabled= false;
		assertFalse(log.isInfoEnabled());
		
		context.isWarnEnabled= false;
		assertFalse(log.isWarnEnabled());

		context.isErrorEanbled = false;
		assertFalse(log.isErrorEnabled());
		
		context.isTraceEnabled = true;
		assertTrue(log.isTraceEnabled());
		
		context.isDebugEnabled = true;
		assertTrue(log.isDebugEnabled());
		
		context.isInfoEnabled = true;
		assertTrue(log.isInfoEnabled());
		
		context.isWarnEnabled = true;
		assertTrue(log.isWarnEnabled());
		
		context.isErrorEanbled = true;
		assertTrue(log.isErrorEnabled());
	}
	
	@Test
	public void testTrace() {
		log.trace("hello");
		assertEquals(msg0, context.msg());
		assertNull(context.throwable());
		
		log.trace(t);
		assertEquals(t.getMessage(), context.msg());
		assertSame(t,context.throwable());	
		
		log.trace(msg0,t);
		assertEquals(msg0, context.msg());
		assertSame(t,context.throwable());

		log.trace(msg1,arg0);
		assertEquals(msg2, context.msg());
		assertNull(context.throwable());	
		
		log.trace(msg1,arg0,t);
		assertEquals(msg2, context.msg());
		assertSame(t,context.throwable());		
		
		context.isTraceEnabled = false;
		
		log.trace("hello");
		assertNull(context.msg());
		assertNull(context.throwable());
		
		log.trace(t);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.trace(msg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());

		log.trace(msg1,arg0);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.trace(msg1,arg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());			
	}
	
	@Test
	public void testDebug() {
		log.debug("hello");
		assertEquals(msg0, context.msg());
		assertNull(context.throwable());
		
		log.debug(t);
		assertEquals(t.getMessage(), context.msg());
		assertSame(t,context.throwable());	
		
		log.debug(msg0,t);
		assertEquals(msg0, context.msg());
		assertSame(t,context.throwable());

		log.debug(msg1,arg0);
		assertEquals(msg2, context.msg());
		assertNull(context.throwable());	
		
		log.debug(msg1,arg0,t);
		assertEquals(msg2, context.msg());
		assertSame(t,context.throwable());		
		
		context.isDebugEnabled = false;
		
		log.debug("hello");
		assertNull(context.msg());
		assertNull(context.throwable());
		
		log.debug(t);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.debug(msg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());

		log.debug(msg1,arg0);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.debug(msg1,arg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());			
	}
	
	@Test
	public void testInfo() {
		log.info("hello");
		assertEquals(msg0, context.msg());
		assertNull(context.throwable());
		
		log.info(t);
		assertEquals(t.getMessage(), context.msg());
		assertSame(t,context.throwable());	
		
		log.info(msg0,t);
		assertEquals(msg0, context.msg());
		assertSame(t,context.throwable());

		log.info(msg1,arg0);
		assertEquals(msg2, context.msg());
		assertNull(context.throwable());	
		
		log.info(msg1,arg0,t);
		assertEquals(msg2, context.msg());
		assertSame(t,context.throwable());		
		
		context.isInfoEnabled = false;
		
		log.info("hello");
		assertNull(context.msg());
		assertNull(context.throwable());
		
		log.info(t);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.info(msg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());

		log.info(msg1,arg0);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.info(msg1,arg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());			
	}
	
	@Test
	public void testWarn() {
		log.warn("hello");
		assertEquals(msg0, context.msg());
		assertNull(context.throwable());
		
		log.warn(t);
		assertEquals(t.getMessage(), context.msg());
		assertSame(t,context.throwable());	
		
		log.warn(msg0,t);
		assertEquals(msg0, context.msg());
		assertSame(t,context.throwable());

		log.warn(msg1,arg0);
		assertEquals(msg2, context.msg());
		assertNull(context.throwable());	
		
		log.warn(msg1,arg0,t);
		assertEquals(msg2, context.msg());
		assertSame(t,context.throwable());		
		
		context.isWarnEnabled = false;
		
		log.warn("hello");
		assertNull(context.msg());
		assertNull(context.throwable());
		
		log.warn(t);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.warn(msg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());

		log.warn(msg1,arg0);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.warn(msg1,arg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());			
	}
	
	@Test
	public void testError() {
		log.error("hello");
		assertEquals(msg0, context.msg());
		assertNull(context.throwable());
		
		log.error(t);
		assertEquals(t.getMessage(), context.msg());
		assertSame(t,context.throwable());	
		
		log.error(msg0,t);
		assertEquals(msg0, context.msg());
		assertSame(t,context.throwable());

		log.error(msg1,arg0);
		assertEquals(msg2, context.msg());
		assertNull(context.throwable());	
		
		log.error(msg1,arg0,t);
		assertEquals(msg2, context.msg());
		assertSame(t,context.throwable());		
		
		context.isErrorEanbled = false;
		
		log.error("hello");
		assertNull(context.msg());
		assertNull(context.throwable());
		
		log.error(t);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.error(msg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());

		log.error(msg1,arg0);
		assertNull(context.msg());
		assertNull(context.throwable());	
		
		log.error(msg1,arg0,t);
		assertNull(context.msg());
		assertNull(context.throwable());			
	}
}
