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
import java.util.Collection;
import java.util.List;

import bingo.lang.Collections;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Predicate;
import bingo.lang.collections.AbstractCollectionWrapper;
import bingo.lang.exceptions.EmptyDataException;
import bingo.lang.exceptions.TooManyDataException;

public class CollectionEnumerable<E> extends AbstractCollectionWrapper<E> implements Enumerable<E> {

	public CollectionEnumerable() {
	    super();
    }

	public CollectionEnumerable(Collection<E> collection) {
	    super(collection);
    }
	
	public E first() throws EmptyDataException {
	    if(collection.size() == 0){
	    	throw new EmptyDataException();
	    }
	    return collection.iterator().next();
    }

	public E firstOrNull() {
	    if(isEmpty()){
	    	return null;
	    }
	    return collection.iterator().next();
    }
	
	public E firstOrNull(Predicate<E> predicate) {
	    return Enumerables.firstOrNull(this, predicate);
    }
	
	public E single() throws EmptyDataException, TooManyDataException {
		int size = collection.size();
	    if(size == 0){
	    	throw new EmptyDataException();
	    }
	    if(size > 1){
	    	throw new TooManyDataException();
	    }
	    return collection.iterator().next();
    }
	
	public Enumerable<E> where(Predicate<E> predicate) {
	    return Enumerables.of(Enumerables.where(this, predicate));
    }

	public <T> Enumerable<T> ofType(Class<T> type) {
	    return Enumerables.of(Enumerables.ofType(this, type));
    }
	
	public E[] toArray(Class<E> type) {
	    return Collections.toArray(this,type);
    }
	
	public List<E> toList() {
	    return new ArrayList<E>(this);
    }
}