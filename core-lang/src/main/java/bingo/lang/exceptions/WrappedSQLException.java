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

import java.sql.SQLException;

/**
 * Warp a {@link SQLException} to {@link RuntimeException}
 */
public class WrappedSQLException extends RuntimeException {

	private static final long serialVersionUID = -1637514337920224680L;

	public WrappedSQLException(SQLException cause) {
		super(cause.getMessage(), cause);
	}

	public SQLException getSQLException() {
		return (SQLException) getCause();
	}
}