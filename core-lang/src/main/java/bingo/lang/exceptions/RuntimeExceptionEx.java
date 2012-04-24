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
 * 
 *
 * @author fenghm (fenghm@bingosoft.net)
 */
public class RuntimeExceptionEx extends RuntimeException {

    private static final long serialVersionUID = -288751546316286455L;

	public RuntimeExceptionEx() {

	}

	public RuntimeExceptionEx(String message) {
		super(message);
	}
	
	public RuntimeExceptionEx(String message,Object... args) {
		
	}

	public RuntimeExceptionEx(Throwable cause) {
		super(cause);
	}

	public RuntimeExceptionEx(String message, Throwable cause) {
		super(message, cause);
	}

}