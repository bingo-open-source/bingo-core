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
package bingo.utils.json;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import bingo.lang.Strings;
import bingo.lang.testing.junit.ConcurrentTestCase;

public class JSONEncodeOverflowTest extends ConcurrentTestCase {
	
	@Test
	public void testSimpleOverflowEncode(){
		
		OverflowBean bean = new OverflowBean();

		try {
	        JSON.encode(bean);
	        
	        fail("should throw JSONException with overflow message");
        } catch (JSONException e) {
        	assertTrue(Strings.contains(e.getMessage(), "stack"));
        }
	}

	@SuppressWarnings("unused")
	static final class OverflowBean {
		
		private Object overflowAttribute;
		
		public Object getOverflowAttribute(){
			return new OverflowBean();
		}
		
	}
}
