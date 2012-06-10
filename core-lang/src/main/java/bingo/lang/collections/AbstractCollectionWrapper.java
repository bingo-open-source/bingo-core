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
package bingo.lang.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractCollectionWrapper<E> implements Collection<E> {

	protected Collection<E> collection;
	
	protected AbstractCollectionWrapper(){
		this.collection = new ArrayList<E>();
	}
	
	protected AbstractCollectionWrapper(Collection<E> collection){
		this.collection = collection;
	}

	public boolean add(E o) {
	    return collection.add(o);
    }

	public boolean addAll(Collection<? extends E> c) {
	    return collection.addAll(c);
    }

	public void clear() {
		collection.clear();
    }

	public boolean contains(Object o) {
	    return collection.contains(o);
    }

	public boolean containsAll(Collection<?> c) {
	    return collection.containsAll(c);
    }

	public boolean isEmpty() {
	    return collection.isEmpty();
    }

	public Iterator<E> iterator() {
	    return collection.iterator();
    }

	public boolean remove(Object o) {
	    return collection.remove(o);
    }

	public boolean removeAll(Collection<?> c) {
	    return collection.removeAll(c);
    }

	public boolean retainAll(Collection<?> c) {
	    return collection.retainAll(c);
    }

	public int size() {
	    return collection.size();
    }

	public Object[] toArray() {
	    return collection.toArray();
    }

	public <T> T[] toArray(T[] a) {
	    return collection.toArray(a);
    }
}
