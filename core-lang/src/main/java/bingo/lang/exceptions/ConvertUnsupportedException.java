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
package bingo.lang.exceptions;

public class ConvertUnsupportedException extends ConvertException {

	private static final long serialVersionUID = -939197291782840101L;

	public ConvertUnsupportedException() {
		
	}

	public ConvertUnsupportedException(String message) {
		super(message);
	}

	public ConvertUnsupportedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConvertUnsupportedException(String message, Object... args) {
		super(message, args);
	}

	public ConvertUnsupportedException(Throwable cause) {
		super(cause);
	}

	public ConvertUnsupportedException(Throwable cause, String message, Object... args) {
		super(cause, message, args);
	}
}
