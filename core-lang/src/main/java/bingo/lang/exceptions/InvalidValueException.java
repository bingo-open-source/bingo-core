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

public class InvalidValueException extends RuntimeExceptionEx {

	private static final long serialVersionUID = -5095348732682041107L;

	public InvalidValueException() {
		
	}

	public InvalidValueException(String message) {
		super(message);
	}

	public InvalidValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidValueException(String message, Object... args) {
		super(message, args);
	}

	public InvalidValueException(Throwable cause) {
		super(cause);
	}

	public InvalidValueException(Throwable cause, String message, Object... args) {
		super(cause, message, args);
	}
}
