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

public abstract class AbstractListWrapper<E> implements List<E> {

	protected List<E>	list;

	protected AbstractListWrapper() {

	}

	protected AbstractListWrapper(List<E> list) {
		Assert.notNull(list);

		this.list = list;
	}

	public boolean add(E o) {
		return list.add(o);
	}

	public boolean addAll(Collection<? extends E> c) {
		return list.addAll(c);
	}

	public void clear() {
		list.clear();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public Iterator<E> iterator() {
		return list.iterator();
	}

	public boolean remove(Object o) {
		return list.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return list.removeAll(c);
	}

	public int size() {
		return list.size();
	}

	public Object[] toArray() {
		return list.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	public void add(int index, E element) {
		list.add(index, element);
    }

	public boolean addAll(int index, Collection<? extends E> c) {
	    return list.addAll(index, c);
    }

	public E get(int index) {
	    return list.get(index);
    }

	public int indexOf(Object o) {
	    return list.indexOf(o);
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

	public E remove(int index) {
	    return list.remove(index);
    }

	public E set(int index, E element) {
	    return list.set(index, element);
    }

	public List<E> subList(int fromIndex, int toIndex) {
	    return list.subList(fromIndex, toIndex);
    }
}
