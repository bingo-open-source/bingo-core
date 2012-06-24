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

import java.util.Iterator;
import java.util.List;

import bingo.lang.Arrays;
import bingo.lang.Assert;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Predicate;
import bingo.lang.exceptions.EmptyDataException;
import bingo.lang.exceptions.TooManyDataException;
import bingo.lang.iterable.ArrayIterator;

public class ArrayEnumerable<E> implements Enumerable<E> {

	private final E[] array;
	private final ArrayIterator<E> iterator;
	
	public ArrayEnumerable(E... array) {
		Assert.notNull(array);
		this.array = Arrays.copyOf(array);
		this.iterator = new ArrayIterator<E>(array);
	}
	
	public int size() {
	    return array.length;
    }
	
	public boolean isEmpty() {
	    return array.length > 0;
    }
	
	public E first() throws EmptyDataException {
	    if(array.length == 0){
	    	throw new EmptyDataException();
	    }
	    return array[0];
    }

	public E firstOrNull() {
	    if(array.length == 0){
	    	return null;
	    }
	    return array[0];
    }
	
	public E firstOrNull(Predicate<E> predicate) {
	    return Enumerables.firstOrNull(this, predicate);
    }
	
	public Enumerable<E> where(Predicate<E> predicate) {
	    return Enumerables.of(Enumerables.where(this, predicate));
    }

	public <T> Enumerable<T> ofType(Class<T> type) {
	    return Enumerables.of(Enumerables.ofType(this, type));
    }

	public E single() throws EmptyDataException, TooManyDataException {
	    if(array.length == 0){
	    	throw new EmptyDataException();
	    }
	    if(array.length > 1){
	    	throw new TooManyDataException();
	    }
	    return array[0];
    }
	
	public void set(int index,E e){
		array[index] = e;
	}
	
	public E get(int index){
		return array[index];
	}
	
	public E[] array(){
		return array;
	}
	
	public Object[] toArray() {
	    return Arrays.copyOf(array, array.length);
    }

	public E[] toArray(Class<E> type) {
		return Arrays.copyOf(array, array.length);
    }

	public List<E> toList() {
	    return Arrays.toList(array);
    }

	public Iterator<E> iterator() {
	    return iterator;
    }
}
