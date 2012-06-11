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
package bingo.lang.xml;

import bingo.lang.exceptions.NestedRuntimeException;

public class XmlException extends NestedRuntimeException {

	private static final long serialVersionUID = -4932171728372959007L;

	public XmlException() {
	    super();
    }

	public XmlException(String message, Object... args) {
	    super(message, args);
    }

	public XmlException(String message, Throwable cause) {
	    super(message, cause);
    }

	public XmlException(String message) {
	    super(message);
    }

	public XmlException(Throwable cause) {
	    super(cause);
    }
}
