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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import bingo.lang.Assert;
import bingo.lang.Enumerable;
import bingo.lang.Func;
import bingo.lang.Func1;
import bingo.lang.Func2;
import bingo.lang.Out;
import bingo.lang.Predicate;
import bingo.lang.exceptions.EmptyDataException;
import bingo.lang.exceptions.TooManyDataException;
import bingo.lang.iterable.ArrayIterable;
import bingo.lang.iterable.FuncIterable;
import bingo.lang.iterable.ImmutableIteratorBase;
import bingo.lang.iterable.PredicateIterable;

//From org.core4j, under Apache License 2.0
public class IterableEnumerable<T> implements Enumerable<T> {

	public static <T> IterableEnumerable<T> of(T... values) {
		return new IterableEnumerable<T>(new ArrayIterable<T>(values));
	}

	public static <T> IterableEnumerable<T> of(Iterable<T> values) {
		return new IterableEnumerable<T>(values);
	}

	public static <T> IterableEnumerable<T> of(final Func<Iterator<T>> fn) {
		return new IterableEnumerable<T>(makeIterable(fn));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> IterableEnumerable<T> emptyOf(Class<T> clazz) {
		return IterableEnumerable.of();
	}
	
	private final Iterable<T> values;
	private int size = -1;
	
	public IterableEnumerable(Iterable<T> values) {
		Assert.notNull(values,"values must not be null");
		this.values = values;
	}
	
	public Iterator<T> iterator() {
		return values.iterator();
	}

	@SuppressWarnings("unused")
	public int size() {
		if(size == -1){
			int rt = 0;
			for (T value : values) {
				rt++;
			}
			size = rt;
		}
		return size;
	}

	public boolean isEmpty() {
		return size() <= 0;
	}
	
	public T get(int index) throws IndexOutOfBoundsException {
		int size = size();
		
		if(size <= 0){
			throw new IndexOutOfBoundsException("index:" + index);
		}
		
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException("index:" + index);
		}

		int i=0;
		
		for(T e : this){
			if(i == index){
				return e;
			}
			i++;
		}
		
		throw new IllegalStateException();
    }

	public T first() throws EmptyDataException {
		for (T value : values) {
			return value;
		}
		throw new EmptyDataException("No elements");
	}

	public T firstOrNull() {
		for (T value : values) {
			return value;
		}
		return null;
	}
	
	public T single() throws EmptyDataException, TooManyDataException {
		Iterator<T> it = iterator();
		
		if(it.hasNext()){
			
			T t = it.next();
			
			if(it.hasNext()){
				throw new TooManyDataException();
			}
			
			return t;
			
		}else{
			throw new EmptyDataException();
		}
    }
	
	public T first(Predicate<T> predicate) throws EmptyDataException  {
		for (T value : values) {
			if (predicate.apply(value)) {
				return value;
			}
		}
		throw new EmptyDataException("No elements match the predicate");
	}

	public T firstOrNull(Predicate<T> predicate) {
		for (T value : values) {
			if (predicate.apply(value)) {
				return value;
			}
		}
		return null;
	}

	public T last() {
		T rt = null;
		boolean empty = true;
		for (T value : values) {
			empty = false;
			rt = value;
		}
		if (empty) {
			throw new EmptyDataException("No elements");
		}
		return rt;
	}
	
	public IterableEnumerable<T> reverse() {
		List<T> rt = this.toList();
		Collections.reverse(rt);
		return new IterableEnumerable<T>(rt);
	}
	
	public IterableEnumerable<T> distinct() {
		return IterableEnumerable.of(new Func<Iterator<T>>() {
			public Iterator<T> apply() {
				return new DistinctIterator<T>(IterableEnumerable.this);
			}
		});
	}	

	@SuppressWarnings("unchecked")
	public IterableEnumerable<T> concat(Iterable<T> other) {
		return concat(new Iterable[] { other });
	}

	public IterableEnumerable<T> concat(Iterable<T>... others) {
		List<Iterable<T>> rt = thisThenOthers(others);
		return new IterableEnumerable<T>(new ConcatIterable<T>(rt));
	}

	@SuppressWarnings("unchecked")
	public IterableEnumerable<T> concat(T... others) {
		return concat(new IterableEnumerable[] { IterableEnumerable.of(others) });
	}

	public IterableEnumerable<T> take(final int count) {
		return of(new Func<Iterator<T>>() {
			public Iterator<T> apply() {
				return new TakeIterator<T>(IterableEnumerable.this, count);
			}
		});
	}
	
	public IterableEnumerable<T> where(Predicate<T> predicate) {
		return new IterableEnumerable<T>(new PredicateIterable<T>(this, predicate));
	}

	public <O> IterableEnumerable<O> select(Func1<T, O> projection) {
		return new IterableEnumerable<O>(new FuncIterable<T, O>(this, projection));
	}
	
	public boolean any(Predicate<T> predicate) {
		for (T value : values) {
			if (predicate.apply(value)) {
				return true;
			}
		}
		return false;
	}

	public boolean all(Predicate<T> predicate) {
		for (T value : values) {
			if (!predicate.apply(value)) {
				return false;
			}
		}
		return true;
	}

	public boolean contains(T value) {
		for (T existingValue : values) {
			if (existingValue.equals(value)) {
				return true;
			}
		}
		return false;

	}

	public T elementAt(int index) throws EmptyDataException {
		int i = 0;
		for (T value : values) {
			if (index == i++) {
				return value;
			}
		}
		throw new EmptyDataException("No element at index " + index);
	}

	public T elementAtOrNull(int index) {
		int i = 0;
		for (T value : values) {
			if (index == i++) {
				return value;
			}
		}
		return null;
	}

	protected <R> R aggregate(Class<R> clazz, Func2<T, R, R> aggregation) {
		return aggregate(clazz, null, aggregation);
	}

	protected <R> R aggregate(Class<R> clazz, R initialValue, Func2<T, R, R> aggregation) {
		R rt = initialValue;
		for (T value : values) {
			rt = aggregation.evaluate(value, rt);
		}
		return rt;
	}

	protected <R> R sum(final Class<R> clazz) {
		if (clazz.equals(Double.class) || clazz.equals(Integer.class) || clazz.equals(BigDecimal.class)) {
			Func2<T, R, R> aggregation = new Func2<T, R, R>() {
				@SuppressWarnings("unchecked")
				public R evaluate(T input1, R input2) {

					Number n1 = (Number) input1; // assumes T (this) is Number
					Number n2 = (Number) input2; // this is safe, one of Double,Integer,BigDecimal

					// TODO better way?
					if (clazz.equals(Double.class)) {
						Double rt = n1.doubleValue() + (n2 == null ? 0 : n2.doubleValue());
						return (R) rt;
					}
					if (clazz.equals(Integer.class)) {
						Integer rt = n1.intValue() + (n2 == null ? 0 : n2.intValue());
						return (R) rt;
					}
					if (clazz.equals(BigDecimal.class)) {
						if (n1 instanceof Integer) {
							n1 = BigDecimal.valueOf((Integer) n1);
						}
						if (n1 instanceof Double) {
							n1 = BigDecimal.valueOf((Double) n1);
						}
						BigDecimal bd1 = n1 == null ? BigDecimal.ZERO : (BigDecimal) n1;
						BigDecimal bd2 = n2 == null ? BigDecimal.ZERO : (BigDecimal) n2;

						BigDecimal rt = bd1.add(bd2);
						return (R) rt;
					}

					throw new UnsupportedOperationException("No default aggregation for class " + clazz.getSimpleName());
				}
			};
			return aggregate(clazz, aggregation);
		}

		throw new UnsupportedOperationException("No default aggregation for class " + clazz.getSimpleName());
	}

	protected <R> R sum(Class<R> clazz, Func1<T, R> projection) {
		IterableEnumerable<R> rt = this.select(projection);
		return rt.sum(clazz);
	}

	protected <K extends Comparable<K>> IterableEnumerable<T> orderBy(final Func1<T, K> projection) {
		return orderBy(new Comparator<T>() {
			public int compare(T o1, T o2) {
				K lhs = projection.apply(o1);
				K rhs = projection.apply(o2);
				return lhs.compareTo(rhs);
			}
		});
	}

	public IterableEnumerable<T> orderBy(Comparator<T> comparator) {
		List<T> rt = this.toList();
		Collections.sort(rt, comparator);
		return IterableEnumerable.of(rt);
	}

	public IterableEnumerable<T> orderBy() {
		return orderBy(new Comparator<T>() {
			@SuppressWarnings("unchecked")
			public int compare(T o1, T o2) {
				Comparable<T> lhs = (Comparable<T>) o1;
				return lhs.compareTo(o2);
			}
		});
	}

	public String join(String separator) {
		StringBuilder rt = new StringBuilder();
		boolean isFirst = true;
		for (T value : this) {
			if (isFirst) {
				isFirst = false;
			} else {
				rt.append(separator);
			}
			rt.append(value == null ? "" : value.toString());
		}
		return rt.toString();
	}

	public static IterableEnumerable<Integer> range(final int start, final int count) {
		return of(new Func<Iterator<Integer>>() {
			public Iterator<Integer> apply() {
				return new RangeIterator(start, count);
			}
		});
	}

	public <O> IterableEnumerable<O> cast(Class<O> clazz) {
		return this.select(new Func1<T, O>() {
			@SuppressWarnings("unchecked")
			public O apply(T input) {
				return (O) input;
			}
		});
	}

	public <O> IterableEnumerable<O> ofType(Class<O> clazz) {
		final Class<O> finalClazz = clazz;
		return this.where(new Predicate<T>() {
			public boolean apply(T input) {
				return input != null && finalClazz.isAssignableFrom(input.getClass());
			}
		}).cast(clazz);
	}

	public IterableEnumerable<T> skip(int count) {
		return IterableEnumerable.of(new SkipEnumerable<T>(this, count));
	}

	public IterableEnumerable<T> skipWhile(final Predicate<T> predicate) {
		final Boolean[] skipping = new Boolean[] { true };
		return this.where(new Predicate<T>() {
			public boolean apply(T input) {
				if (!skipping[0]) {
					return true;
				}
				if (!predicate.apply(input)) {
					skipping[0] = false;
					return true;
				}
				return false;
			}
		});
	}

	public <R> IterableEnumerable<R> selectMany(final Func1<T, IterableEnumerable<R>> selector) {
		return IterableEnumerable.of(new Func<Iterator<R>>() {
			public Iterator<R> apply() {
				return new SelectManyIterator<T, R>(IterableEnumerable.this, selector);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray(Class<T> clazz) {
		List<T> rt = toList();
		T[] array = (T[]) Array.newInstance(clazz, rt.size());
		for (int i = 0; i < array.length; i++) {
			array[i] = rt.get(i);
		}
		return array;
	}
	
	public Object[] toArray() {
	    return toList().toArray();
    }

	public T[] toArray(T[] array) {
		return toList().toArray(array);
	}

	public List<T> toList() {
		List<T> rt = new ArrayList<T>();
		for (T value : values) {
			rt.add(value);
		}
		return rt;
	}

	public Set<T> toSet() {
		Set<T> rt = new HashSet<T>();
		for (T value : values) {
			rt.add(value);
		}
		return rt;
	}

	public SortedSet<T> toSortedSet() {
		SortedSet<T> rt = new TreeSet<T>();
		for (T value : values) {
			rt.add(value);
		}
		return rt;
	}

	public SortedSet<T> toSortedSet(Comparator<? super T> comparator) {
		SortedSet<T> rt = new TreeSet<T>(comparator);
		for (T value : values) {
			rt.add(value);
		}
		return rt;
	}

	public <K> Map<K, T> toMap(Func1<T, K> keyFn) {
		Map<K, T> rt = new HashMap<K, T>();
		for (T value : values) {
			rt.put(keyFn.apply(value), value);
		}
		return rt;
	}
	
	// Protected Methods
	//------------------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	protected IterableEnumerable<T> intersect(IterableEnumerable<T> other) {
		return intersect(new IterableEnumerable[] { other });
	}

	protected IterableEnumerable<T> intersect(IterableEnumerable<T>... others) {
		List<T> rt = this.distinct().toList();
		for (IterableEnumerable<T> other : others) {
			Set<T> set = other.toSet();
			for (T value : IterableEnumerable.of(rt).toList()) {
				if (!set.contains(value)) {
					rt.remove(value);
				}
			}
		}
		return IterableEnumerable.of(rt);
	}	
	
	@SuppressWarnings("unchecked")
	protected IterableEnumerable<T> union(IterableEnumerable<T> other) {
		return union(new IterableEnumerable[] { other });
	}

	protected IterableEnumerable<T> union(IterableEnumerable<T>... others) {
		final List<Iterable<T>> rt = thisThenOthers(others);
		return IterableEnumerable.of(makeIterable(new Func<Iterator<T>>() {
			public Iterator<T> apply() {
				return new UnionIterator<T>(rt);
			}
		}));
	}

	protected <K> IterableEnumerable<Grouping<K, T>> groupBy(Func1<T, K> keySelector) {
		List<K> ordering = new ArrayList<K>();
		final Map<K, List<T>> map = new HashMap<K, List<T>>();
		for (T value : this) {
			K key = keySelector.apply(value);
			if (!ordering.contains(key)) {
				ordering.add(key);
				map.put(key, new ArrayList<T>());
			}
			map.get(key).add(value);
		}

		return IterableEnumerable.of(ordering).select(new Func1<K, Grouping<K, T>>() {
			public Grouping<K, T> apply(K input) {
				return new Grouping<K, T>(input, IterableEnumerable.of(map.get(input)));
			}
		});
	}

	protected <R extends Comparable<R>> R max(Func1<T, R> fn) {
		R rt = null;
		for (T value : this) {
			R newValue = fn.apply(value);
			if (newValue == null) {
				continue;
			}
			if (rt == null) {
				rt = newValue;
			} else {
				if (newValue.compareTo(rt) > 0) {
					rt = newValue;
				}
			}
		}
		return rt;
	}

	protected <R extends Comparable<R>> R min(Func1<T, R> fn) {
		R rt = null;
		for (T value : this) {
			R newValue = fn.apply(value);
			if (newValue == null) {
				continue;
			}
			if (rt == null) {
				rt = newValue;
			} else {
				if (newValue.compareTo(rt) < 0) {
					rt = newValue;
				}
			}
		}
		return rt;
	}	
	
	protected static <K, E> Map<K, List<E>> group(Collection<E> c, final Func1<E, K> projection) {
		Map<K, List<E>> map = new HashMap<K, List<E>>();
		for (E e : c) {
			K key = projection.apply(e);
			if (key != null) {
				List<E> list = map.get(key);
				if (list == null) {
					list = new ArrayList<E>();
					map.put(key, list);
				}
				list.add(e);
			}
		}
		return map;
	}	

	// Private Methods
	//------------------------------------------------------------------------------------------------------------
	private List<Iterable<T>> thisThenOthers(Iterable<T>... others) {
		List<Iterable<T>> rt = new ArrayList<Iterable<T>>();
		rt.add(this);
		for (Iterable<T> other : others) {
			rt.add(other);
		}
		return rt;
	}	

	private static <T> Iterable<T> makeIterable(final Func<Iterator<T>> fn) {
		return new Iterable<T>() {
			public Iterator<T> iterator() {
				return fn.apply();
			}
		};
	}

	// Private Inner Classes
	//------------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unused")
	private static class Grouping<TKey, TElement> extends IterableEnumerable<TElement> {

		private final TKey key;

		public Grouping(TKey key, IterableEnumerable<TElement> values) {
			super(values);
			this.key = key;
		}

		public TKey getKey() {
			return key;
		}
	}

	private static class SkipEnumerable<T> implements Iterable<T> {

		private final IterableEnumerable<T> target;
		private final int count;

		public SkipEnumerable(IterableEnumerable<T> target, int count) {
			this.target = target;
			this.count = count;
		}

		public Iterator<T> iterator() {
			Iterator<T> rt = target.iterator();
			for (int i = 0; i < count; i++) {
				if (!rt.hasNext()) {
					return rt;
				}
				rt.next();
			}
			return rt;
		}
	}

	// Private Inner Iterators
	//------------------------------------------------------------------------------------------------------------

	private static class TakeIterator<T> extends ImmutableIteratorBase<T> {

		private int left;
		private Iterator<T> iterator;

		public TakeIterator(Iterable<T> values, int count) {
			iterator = values.iterator();
			left = count;
		}
		
		@Override
        protected boolean next(Out<T> out) throws Exception {
			if (left <= 0) {
				return false;
			}

			if (!iterator.hasNext()) {
				return false;
			}

			left--;

			out.setValue(iterator.next());
			
			return true;
        }
	}

	private static class RangeIterator extends ImmutableIteratorBase<Integer> {

		private final int end;
		private Integer current;

		public RangeIterator(int start, int count) {
			current = start;
			end = start + count - 1;
		}
		
		@Override
        protected boolean next(Out<Integer> out) throws Exception {
			if (current == null) {
				return false;
			}
			
			int rt = current;
			
			if (rt == end) {
				current = null;
			} else {
				current = rt + 1;
			}
			
			out.setValue(rt);
			
			return true;
        }
	}

	private static class UnionIterator<T> extends ImmutableIteratorBase<T> {

		private final List<Iterable<T>> involved;

		public UnionIterator(List<Iterable<T>> involved) {
			this.involved = involved;
		}

		private Set<T> seen;
		private int currentIndex = -1;
		private Iterator<T> currentIterator;
		
		@Override
        protected boolean next(Out<T> out) throws Exception {
			if (seen == null) {
				seen = new HashSet<T>();
			}
			while (true) {
				if (currentIterator == null) {
					currentIndex++;
					if (currentIndex >= involved.size()) {
						return false;
					}
					currentIterator = involved.get(currentIndex).iterator();
				}
				if (!currentIterator.hasNext()) {
					currentIterator = null;
				} else {
					T value = currentIterator.next();
					if (!seen.contains(value)) {
						seen.add(value);
						
						out.setValue(value);
						
						return true;
					}
				}
			}
        }
	}

	private static class SelectManyIterator<TSource, TResult> extends ImmutableIteratorBase<TResult> {

		private final Iterator<TSource> sourceIterator;
		private final Func1<TSource, IterableEnumerable<TResult>> selector;
		private Iterator<TResult> resultIterator;

		public SelectManyIterator(Iterable<TSource> source, Func1<TSource, IterableEnumerable<TResult>> selector) {
			this.selector = selector;
			this.sourceIterator = source.iterator();
		}
		
		@Override
        protected boolean next(Out<TResult> out) throws Exception {
			while (true) {
				if (resultIterator == null) {
					if (!sourceIterator.hasNext()) {
						return false;
					}
					TSource source = sourceIterator.next();
					resultIterator = selector.apply(source).iterator();
				}
				if (!resultIterator.hasNext()) {
					resultIterator = null;
				} else {
					out.setValue(resultIterator.next());
					
					return true;
				}
			}
        }
	}

	private static class ConcatIterable<T> implements Iterable<T> {

		private final Iterable<Iterable<T>> iterables;

		public ConcatIterable(Iterable<Iterable<T>> iterables) {
			this.iterables = iterables;
		}

		public Iterator<T> iterator() {
			return new ConcatIterator<T>(IterableEnumerable.of(iterables).select(new Func1<Iterable<T>, Iterator<T>>() {
				public Iterator<T> apply(Iterable<T> x) {
					return x.iterator();
				}
			}).toList());
		}
	}

	private static class ConcatIterator<T> implements Iterator<T> {

		private final List<Iterator<T>> iterators;
		private int current;

		public ConcatIterator(List<Iterator<T>> iterators) {
			this.iterators = iterators;
		}

		public boolean hasNext() {
			boolean rt = iterators.get(current).hasNext();
			while (!rt) {
				if (current == iterators.size() - 1) {
					return rt;
				}
				current++;
				rt = iterators.get(current).hasNext();
			}
			return rt;
		}

		public T next() {
			while (true) {
				try {
					return iterators.get(current).next();
				} catch (NoSuchElementException e) {
					if (current == iterators.size() - 1) {
						throw new NoSuchElementException();
					}
					current++;
				}
			}
		}

		public void remove() {
			throw new UnsupportedOperationException("remove()");
		}
	}

	private static class DistinctIterator<T> extends ImmutableIteratorBase<T> {

		private final Iterator<T> iterator;
		private Set<T> seen;

		public DistinctIterator(Iterable<T> source) {
			iterator = source.iterator();
		}
		
		@Override
        protected boolean next(Out<T> out) throws Exception {
			if (seen == null) {
				seen = new HashSet<T>();
			}
			while (iterator.hasNext()) {
				T value = iterator.next();
				if (!seen.contains(value)) {
					seen.add(value);
					out.setValue(value);
					return true;
				}
			}
			return false;
        }
	}
}