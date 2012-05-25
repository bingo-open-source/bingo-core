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

public class UncheckedIOException extends NestedRuntimeException {

	private static final long serialVersionUID = 8828062067628915762L;

	public UncheckedIOException() {
		
	}

	public UncheckedIOException(String message) {
		super(message);
	}

	public UncheckedIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public UncheckedIOException(Throwable cause) {
		super(cause);
	}

	public UncheckedIOException(String message, Object... args) {
		super(message, args);
	}
}
