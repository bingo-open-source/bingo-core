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
package bingo.lang.enumerable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bingo.lang.Arrays;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Predicate;
import bingo.lang.Strings;
import bingo.lang.exceptions.EmptyDataException;
import bingo.lang.exceptions.TooManyDataException;
import bingo.lang.iterable.EmptyIterator;

public class EmptyEnumerable<E> implements Enumerable<E> {
	
	private EmptyIterator<E> iterator = null;
	
	public E get(int index) throws IndexOutOfBoundsException {
	    throw new IndexOutOfBoundsException("index:" + index);
    }

	public E first() throws EmptyDataException {
        throw new EmptyDataException();
    }

	public E firstOrNull() {
        return null;
    }
	
	public E firstOrNull(Predicate<E> predicate) {
	    return null;
    }

	public boolean isEmpty() {
        return true;
    }

	public E single() throws EmptyDataException, TooManyDataException {
		throw new EmptyDataException();
    }
	
	public String join(String seperator) {
	    return Strings.EMPTY;
    }

	public <T> Enumerable<T> ofType(Class<T> type) {
	    return Enumerables.empty();
    }

	public Enumerable<E> where(Predicate<E> predicate) {
	    return this;
    }

	public int size() {
        return 0;
    }
	
	public Object[] toArray() {
        return Arrays.EMPTY_OBJECT_ARRAY;
    }

	public E[] toArray(Class<E> type) {
        return Arrays.newInstance(type, 0);
    }

	public List<E> toList() {
        return new ArrayList<E>();
    }
	
	public Set<E> toSet() {
		return new HashSet<E>();
    }

	public Iterator<E> iterator() {
		if(null == iterator){
			iterator = new EmptyIterator<E>();
		}
        return iterator;
    }
}