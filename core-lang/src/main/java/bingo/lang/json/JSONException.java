/*
 * Copyright 2010 the original author or authors.
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
package bingo.lang.json;

import bingo.lang.exceptions.NestedRuntimeException;

public class JSONException extends NestedRuntimeException {

    private static final long serialVersionUID = 6604866089585580442L;

	public JSONException() {
	    super();
    }

	public JSONException(String message, Object... args) {
	    super(message, args);
    }

	public JSONException(String message, Throwable cause) {
	    super(message, cause);
    }

	public JSONException(String message) {
	    super(message);
    }

	public JSONException(Throwable cause) {
	    super(cause);
    }
}
