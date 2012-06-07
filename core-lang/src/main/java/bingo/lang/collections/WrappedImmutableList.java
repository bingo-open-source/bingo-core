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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import bingo.lang.Assert;

public class WrappedImmutableList<E> extends AbstractImmutableList<E> implements List<E> {
	
	private final List<E> list;
	
	public WrappedImmutableList(List<E> list){
		Assert.notNull(list,"the wrapped list must not be null");
		this.list = list;
	}
	
	public boolean contains(Object o) {
	    return list.contains(o);
    }

	public boolean containsAll(Collection<?> c) {
	    return list.containsAll(c);
    }

	public E get(int index) {
	    return list.get(index);
    }

	public int indexOf(Object o) {
	    return list.indexOf(o);
    }

	public boolean isEmpty() {
	    return list.isEmpty();
    }

	public Iterator<E> iterator() {
	    return list.iterator();
    }

	public int lastIndexOf(Object o) {
	    return list.lastIndexOf(o);
    }

	public ListIterator<E> listIterator() {
	    return list.listIterator();
    }

	public ListIterator<E> listIterator(int index) {
	    return list.listIterator(index);
    }

	public int size() {
	    return list.size();
    }

	public List<E> subList(int fromIndex, int toIndex) {
	    return list.subList(fromIndex, toIndex);
    }

	public Object[] toArray() {
	    return list.toArray();
    }

	public <T> T[] toArray(T[] a) {
	    return list.toArray(a);
    }
}