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
package bingo.lang;

import java.io.IOException;
import java.sql.SQLException;

import bingo.lang.exceptions.NestedIOException;
import bingo.lang.exceptions.NestedSQLException;
import bingo.lang.exceptions.UncheckedIOException;
import bingo.lang.exceptions.UncheckedSQLException;

public class Exceptions {

	protected Exceptions(){
		
	}
	
	public static UncheckedIOException uncheck(IOException e) throws UncheckedIOException {
		throw new UncheckedIOException(e.getMessage(),e);
	}
	
	public static UncheckedIOException uncheck(IOException e,String message) throws UncheckedIOException {
		throw new UncheckedIOException(message,e);
	}
	
	public static UncheckedIOException uncheck(IOException e,String template,Object... args) throws UncheckedIOException {
		throw new UncheckedIOException(Strings.format(template, args) ,e);
	}
	
	public static UncheckedSQLException uncheck(SQLException e) throws UncheckedSQLException {
		throw new UncheckedSQLException(e.getMessage(),e);
	}
	
	public static UncheckedSQLException uncheck(SQLException e,String message) throws UncheckedSQLException {
		throw new UncheckedSQLException(message,e);
	}
	
	public static UncheckedSQLException uncheck(SQLException e,String template,Object... args) throws UncheckedIOException {
		throw new UncheckedSQLException(Strings.format(template, args) ,e);
	}
	
	public static NestedIOException wrap(IOException e) throws NestedIOException {
		throw new NestedIOException(e.getMessage(),e);
	}
	
	public static NestedIOException wrap(IOException e,String message) throws NestedIOException {
		throw new NestedIOException(message,e);
	}
	
	public static NestedIOException wrap(IOException e,String template,Object... args) throws NestedIOException {
		throw new NestedIOException(Strings.format(template, args),e);
	}
	
	public static NestedSQLException wrap(SQLException e) throws NestedSQLException {
		throw new NestedSQLException(e.getMessage(),e);
	}
	
	public static NestedSQLException wrap(SQLException e,String message) throws NestedSQLException {
		throw new NestedSQLException(message,e);
	}
	
	public static NestedSQLException wrap(SQLException e,String template,Object... args) throws NestedSQLException {
		throw new NestedSQLException(Strings.format(template, args),e);
	}
}