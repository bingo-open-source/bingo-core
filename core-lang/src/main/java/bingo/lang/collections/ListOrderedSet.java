/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package bingo.lang.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bingo.lang.iterable.AbstractIteratorWrapper;

//from apache commons-collections

/**
 * Wraps another <code>Set</code> to ensure that the order of addition is retained and used by the iterator.
 * <p>
 * If an object is added to the set for a second time, it will remain in the original position in the iteration. The
 * order can be observed from the set via the iterator or toArray methods.
 * <p>
 * The ListOrderedSet also has various useful direct methods. These include many from <code>List</code>, such as
 * <code>get(int)</code>, <code>remove(int)</code> and <code>indexOf(int)</code>. An unmodifiable <code>List</code> view
 * of the set can be obtained via <code>asList()</code>.
 * <p>
 * This class cannot implement the <code>List</code> interface directly as various interface methods (notably
 * equals/hashCode) are incompatable with a set.
 * <p>
 * 
 * @author Stephen Colebourne
 * @author Henning P. Schmiedehausen
 */
public class ListOrderedSet<E> extends AbstractSetWrapper<E> implements Set<E> {

	/** Internal list to hold the sequence of objects */
	protected final List<E>	  setOrder;

	//-----------------------------------------------------------------------
	/**
	 * Constructs a new empty <code>ListOrderedSet</code> using a <code>HashSet</code> and an <code>ArrayList</code>
	 * internally.
	 * 
	 * @since Commons Collections 3.1
	 */
	public ListOrderedSet() {
		super(new HashSet<E>());
		setOrder = new ArrayList<E>();
	}

	//-----------------------------------------------------------------------
	/**
	 * Gets an unmodifiable view of the order of the Set.
	 * 
	 * @return an unmodifiable list view
	 */
	public List<E> asList() {
		return Collections.unmodifiableList(setOrder);
	}

	//-----------------------------------------------------------------------
	public void clear() {
		set.clear();
		setOrder.clear();
	}

	public Iterator<E> iterator() {
		return new OrderedSetIterator<E>(setOrder.iterator(), set);
	}

	public boolean add(E object) {
		if (set.contains(object)) {
			// re-adding doesn't change order
			return set.add(object);
		} else {
			// first add, so add to both set and list
			boolean result = set.add(object);
			setOrder.add(object);
			return result;
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = false;
		for (Iterator<? extends E> it = c.iterator(); it.hasNext();) {
			E object = it.next();
			result = result | add(object);
		}
		return result;
	}

	public boolean remove(Object object) {
		boolean result = set.remove(object);
		setOrder.remove(object);
		return result;
	}

	public boolean removeAll(Collection<?> coll) {
		boolean result = false;
		for (Iterator<?> it = coll.iterator(); it.hasNext();) {
			Object object = it.next();
			result = result | remove(object);
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean result = set.retainAll(c);
		if (result == false) {
			return false;
		} else if (set.size() == 0) {
			setOrder.clear();
		} else {
			for (Iterator<E> it = setOrder.iterator(); it.hasNext();) {
				Object object = it.next();
				if (set.contains(object) == false) {
					it.remove();
				}
			}
		}
		return result;
	}

	public Object[] toArray() {
		return setOrder.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return setOrder.toArray(a);
	}

	//-----------------------------------------------------------------------
	public Object get(int index) {
		return setOrder.get(index);
	}

	public int indexOf(E object) {
		return setOrder.indexOf(object);
	}

	public void add(int index, E object) {
		if (contains(object) == false) {
			set.add(object);
			setOrder.add(index, object);
		}
	}

	public boolean addAll(int index, Collection<E> coll) {
		boolean changed = false;
		for (Iterator<E> it = coll.iterator(); it.hasNext();) {
			E object = it.next();
			if (contains(object) == false) {
				set.add(object);
				setOrder.add(index, object);
				index++;
				changed = true;
			}
		}
		return changed;
	}

	public Object remove(int index) {
		Object obj = setOrder.remove(index);
		remove(obj);
		return obj;
	}

	/**
	 * Uses the underlying List's toString so that order is achieved. This means that the decorated Set's toString is
	 * not used, so any custom toStrings will be ignored.
	 */
	// Fortunately List.toString and Set.toString look the same
	public String toString() {
		return setOrder.toString();
	}

	//-----------------------------------------------------------------------
	/**
	 * Internal iterator handle remove.
	 */
	static class OrderedSetIterator<E> extends AbstractIteratorWrapper<E> {

		/** Object we iterate on */
		protected final Set<E>	set;

		/** Last object retrieved */
		protected E		       last;

		private OrderedSetIterator(Iterator<E> iterator, Set<E> set) {
			super(iterator);
			this.set = set;
		}

		public E next() {
			last = iterator.next();
			return last;
		}

		public void remove() {
			set.remove(last);
			iterator.remove();
			last = null;
		}
	}
}
