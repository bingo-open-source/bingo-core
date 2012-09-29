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
package bingo.lang.cloning;

import java.util.Map;

class BaseCloners {
	public static class StringBufferCloner implements TypeCloner<StringBuffer> {
		public StringBuffer clone(Cloner cloner, StringBuffer object, Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		    return new StringBuffer(object.toString());
	    }
	}
	
	public static class StringBuilderCloner implements TypeCloner<StringBuilder>{
		public StringBuilder clone(Cloner cloner, StringBuilder object, Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		    return new StringBuilder(object.toString());
	    }
	}
}
