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

import java.util.List;

import bingo.lang.exceptions.EmptyDataException;
import bingo.lang.exceptions.TooManyDataException;

public interface Enumerable<E> extends Iterable<E> {

	int size();
	
	boolean isEmpty();
	
	E first() throws EmptyDataException;
	
	E firstOrNull();
	
	E firstOrNull(Predicate<E> predicate);
	
	E single() throws EmptyDataException,TooManyDataException;
	
	Enumerable<E> where(Predicate<E> predicate);
	
	<T> Enumerable<T> ofType(Class<T> type);
	
	Object[] toArray();
	
	E[] toArray(Class<E> type);
	
	List<E> toList();
}