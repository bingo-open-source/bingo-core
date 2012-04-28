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

/**
 * Exception thrown when a clone cannot be created. In contrast to {@link CloneNotSupportedException} this is a
 * {@link RuntimeException}.
 */
public class CloneException extends RuntimeExceptionEx {

	private static final long	serialVersionUID	= 102030044497665595L;

	public CloneException() {

	}

	public CloneException(String message) {
		super(message);
	}

	public CloneException(Throwable cause) {
		super(cause);
	}

	public CloneException(String message, Throwable cause) {
		super(message, cause);
	}

	public CloneException(String message, Object... args) {
	    super(message, args);
    }

	public CloneException(Throwable cause, String message, Object... args) {
	    super(cause, message, args);
    }
}