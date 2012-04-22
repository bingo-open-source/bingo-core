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
package bingo.utils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import bingo.lang.Enumerable;
import bingo.lang.Func;
import bingo.lang.Func1;
import bingo.lang.Func2;
import bingo.lang.Out;
import bingo.lang.Predicate;
import bingo.utils.iterable.ArrayIterable;
import bingo.utils.iterable.FuncIterable;
import bingo.utils.iterable.PredicateIterable;
import bingo.utils.iterable.ReadOnlyIterator;

/**
 * 实现{@link Enumerable}接口并提供工具类方法进行集合操作
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
//From org.core4j, under Apache License 2.0
public class Enumerator<T> implements Enumerable<T> {

	private final Iterable<T> values;
	private int size = -1;

	public static <T> Enumerator<T> create(T... values) {
		return new Enumerator<T>(new ArrayIterable<T>(values));
	}

	public static <T> Enumerator<T> create(Iterable<T> values) {
		return new Enumerator<T>(values);
	}

	@SuppressWarnings("unchecked")
	public static <T> Enumerator<T> create(Class<T> clazz, Enumeration<?> e) {
		List<T> rt = new ArrayList<T>();
		while (e.hasMoreElements()) {
			rt.add((T) e.nextElement());
		}
		return new Enumerator<T>(rt);
	}

	public static <T> Enumerator<T> create(final Func<Iterator<T>> fn) {
		return new Enumerator<T>(makeIterable(fn));
	}
	
	protected Enumerator(Iterable<T> values) {
		if (values == null) {
			throw new IllegalArgumentException("values cannot be null");
		}
		this.values = values;
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

	public T first() {
		for (T value : values) {
			return value;
		}
		throw new RuntimeException("No elements");
	}

	public T first(Predicate<T> predicate) {
		for (T value : values) {
			if (predicate.evaluate(value)) {
				return value;
			}
		}
		throw new RuntimeException("No elements match the predicate");
	}

	public T firstOrNull() {
		for (T value : values) {
			return value;
		}
		return null;
	}

	public T firstOrNull(Predicate<T> predicate) {
		for (T value : values) {
			if (predicate.evaluate(value)) {
				return value;
			}
		}
		return null;
	}

	public Enumerator<T> where(Predicate<T> predicate) {
		return new Enumerator<T>(new PredicateIterable<T>(this, predicate));
	}

	public <TOutput> Enumerator<TOutput> select(Func1<T, TOutput> projection) {
		return new Enumerator<TOutput>(new FuncIterable<T, TOutput>(this, projection));
	}

	public static <K, E> Map<K, List<E>> group(Collection<E> c, final Func1<E, K> projection) {
		Map<K, List<E>> map = new HashMap<K, List<E>>();
		for (E e : c) {
			K key = projection.evaluate(e);
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

	public T last() {
		T rt = null;
		boolean empty = true;
		for (T value : values) {
			empty = false;
			rt = value;
		}
		if (empty) {
			throw new RuntimeException("No elements");
		}
		return rt;
	}

	public Iterator<T> iterator() {
		return values.iterator();
	}

	public Enumerator<T> reverse() {
		List<T> rt = this.toList();
		Collections.reverse(rt);
		return new Enumerator<T>(rt);
	}

	private List<Iterable<T>> thisThenOthers(Iterable<T>... others) {
		List<Iterable<T>> rt = new ArrayList<Iterable<T>>();
		rt.add(this);
		for (Iterable<T> other : others) {
			rt.add(other);
		}
		return rt;
	}

	@SuppressWarnings("unchecked")
	public Enumerator<T> concat(Iterable<T> other) {
		return concat(new Iterable[] { other });
	}

	public Enumerator<T> concat(Iterable<T>... others) {
		List<Iterable<T>> rt = thisThenOthers(others);
		return new Enumerator<T>(new ConcatIterable<T>(rt));
	}

	@SuppressWarnings("unchecked")
	public Enumerator<T> concat(T... others) {
		return concat(new Enumerator[] { Enumerator.create(others) });
	}

	public Enumerator<T> take(final int count) {
		return create(new Func<Iterator<T>>() {
			public Iterator<T> evaluate() {
				return new TakeIterator<T>(Enumerator.this, count);
			}
		});
	}

	public boolean any(Predicate<T> predicate) {
		for (T value : values) {
			if (predicate.evaluate(value)) {
				return true;
			}
		}
		return false;
	}

	public boolean all(Predicate<T> predicate) {
		for (T value : values) {
			if (!predicate.evaluate(value)) {
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

	public T elementAt(int index) {
		int i = 0;
		for (T value : values) {
			if (index == i++) {
				return value;
			}
		}
		throw new RuntimeException("No element at index " + index);
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

	public <TReturn> TReturn aggregate(Class<TReturn> clazz, Func2<T, TReturn, TReturn> aggregation) {
		return aggregate(clazz, null, aggregation);
	}

	public <TReturn> TReturn aggregate(Class<TReturn> clazz, TReturn initialValue, Func2<T, TReturn, TReturn> aggregation) {
		TReturn rt = initialValue;
		for (T value : values) {
			rt = aggregation.evaluate(value, rt);
		}
		return rt;
	}

	public <TReturn> TReturn sum(final Class<TReturn> clazz) {
		if (clazz.equals(Double.class) || clazz.equals(Integer.class) || clazz.equals(BigDecimal.class)) {
			Func2<T, TReturn, TReturn> aggregation = new Func2<T, TReturn, TReturn>() {
				@SuppressWarnings("unchecked")
				public TReturn evaluate(T input1, TReturn input2) {

					Number n1 = (Number) input1; // assumes T (this) is Number
					Number n2 = (Number) input2; // this is safe, one of Double,Integer,BigDecimal

					// TODO better way?
					if (clazz.equals(Double.class)) {
						Double rt = n1.doubleValue() + (n2 == null ? 0 : n2.doubleValue());
						return (TReturn) rt;
					}
					if (clazz.equals(Integer.class)) {
						Integer rt = n1.intValue() + (n2 == null ? 0 : n2.intValue());
						return (TReturn) rt;
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
						return (TReturn) rt;
					}

					throw new UnsupportedOperationException("No default aggregation for class " + clazz.getSimpleName());
				}
			};
			return aggregate(clazz, aggregation);
		}

		throw new UnsupportedOperationException("No default aggregation for class " + clazz.getSimpleName());
	}

	public <TReturn> TReturn sum(Class<TReturn> clazz, Func1<T, TReturn> projection) {
		Enumerator<TReturn> rt = this.select(projection);
		return rt.sum(clazz);
	}

	public <TKey extends Comparable<TKey>> Enumerator<T> orderBy(final Func1<T, TKey> projection) {
		return orderBy(new Comparator<T>() {
			public int compare(T o1, T o2) {
				TKey lhs = projection.evaluate(o1);
				TKey rhs = projection.evaluate(o2);
				return lhs.compareTo(rhs);
			}
		});
	}

	public Enumerator<T> orderBy(Comparator<T> comparator) {
		List<T> rt = this.toList();
		Collections.sort(rt, comparator);
		return Enumerator.create(rt);
	}

	public Enumerator<T> orderBy() {
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

	@SuppressWarnings("unchecked")
	public static <T> Enumerator<T> empty(Class<T> clazz) {
		return Enumerator.create();
	}

	public static Enumerator<Integer> range(final int start, final int count) {
		return create(new Func<Iterator<Integer>>() {
			public Iterator<Integer> evaluate() {
				return new RangeIterator(start, count);
			}
		});
	}

	public <TOutput> Enumerator<TOutput> cast(Class<TOutput> clazz) {
		return this.select(new Func1<T, TOutput>() {
			@SuppressWarnings("unchecked")
			public TOutput evaluate(T input) {
				return (TOutput) input;
			}
		});
	}

	public <TOutput> Enumerator<TOutput> ofType(Class<TOutput> clazz) {
		final Class<TOutput> finalClazz = clazz;
		return this.where(new Predicate<T>() {
			public boolean evaluate(T input) {
				return input != null && finalClazz.isAssignableFrom(input.getClass());
			}
		}).cast(clazz);
	}

	public Enumerator<T> skip(int count) {
		return Enumerator.create(new SkipEnumerable<T>(this, count));
	}

	public Enumerator<T> skipWhile(final Predicate<T> predicate) {
		final Boolean[] skipping = new Boolean[] { true };
		return this.where(new Predicate<T>() {
			public boolean evaluate(T input) {
				if (!skipping[0]) {
					return true;
				}
				if (!predicate.evaluate(input)) {
					skipping[0] = false;
					return true;
				}
				return false;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public Enumerator<T> intersect(Enumerable<T> other) {
		return intersect(new Enumerable[] { other });
	}

	public Enumerator<T> intersect(Enumerable<T>... others) {
		List<T> rt = this.distinct().toList();
		for (Enumerable<T> other : others) {
			Set<T> set = other.toSet();
			for (T value : Enumerator.create(rt).toList()) {
				if (!set.contains(value)) {
					rt.remove(value);
				}
			}
		}
		return Enumerator.create(rt);
	}

	@SuppressWarnings("unchecked")
	public Enumerator<T> union(Enumerator<T> other) {
		return union(new Enumerator[] { other });
	}

	public Enumerator<T> union(Enumerator<T>... others) {
		final List<Iterable<T>> rt = thisThenOthers(others);
		return Enumerator.create(makeIterable(new Func<Iterator<T>>() {
			public Iterator<T> evaluate() {
				return new UnionIterator<T>(rt);
			}
		}));
	}

	public <TResult> Enumerator<TResult> selectMany(final Func1<T, Enumerator<TResult>> selector) {
		return Enumerator.create(new Func<Iterator<TResult>>() {
			public Iterator<TResult> evaluate() {
				return new SelectManyIterator<T, TResult>(Enumerator.this, selector);
			}
		});
	}

	public Enumerator<T> distinct() {
		return Enumerator.create(new Func<Iterator<T>>() {
			public Iterator<T> evaluate() {
				return new DistinctIterator<T>(Enumerator.this);
			}
		});
	}

	public <TKey> Enumerator<Grouping<TKey, T>> groupBy(Func1<T, TKey> keySelector) {
		List<TKey> ordering = new ArrayList<TKey>();
		final Map<TKey, List<T>> map = new HashMap<TKey, List<T>>();
		for (T value : this) {
			TKey key = keySelector.evaluate(value);
			if (!ordering.contains(key)) {
				ordering.add(key);
				map.put(key, new ArrayList<T>());
			}
			map.get(key).add(value);
		}

		return Enumerator.create(ordering).select(new Func1<TKey, Grouping<TKey, T>>() {
			public Grouping<TKey, T> evaluate(TKey input) {
				return new Grouping<TKey, T>(input, Enumerator.create(map.get(input)));
			}
		});
	}

	public <TResult extends Comparable<TResult>> TResult max(Func1<T, TResult> fn) {
		TResult rt = null;
		for (T value : this) {
			TResult newValue = fn.evaluate(value);
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

	public <TResult extends Comparable<TResult>> TResult min(Func1<T, TResult> fn) {
		TResult rt = null;
		for (T value : this) {
			TResult newValue = fn.evaluate(value);
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

	@SuppressWarnings("unchecked")
	public T[] toArray(Class<T> clazz) {
		List<T> rt = toList();
		T[] array = (T[]) Array.newInstance(clazz, rt.size());
		for (int i = 0; i < array.length; i++) {
			array[i] = rt.get(i);
		}
		return array;

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
			rt.put(keyFn.evaluate(value), value);
		}
		return rt;
	}

	// Private Methods
	//------------------------------------------------------------------------------------------------------------

	private static <T> Iterable<T> makeIterable(final Func<Iterator<T>> fn) {
		return new Iterable<T>() {
			public Iterator<T> iterator() {
				return fn.evaluate();
			}
		};
	}

	// Private Inner Classes
	//------------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unused")
	private static class Grouping<TKey, TElement> extends Enumerator<TElement> {

		private final TKey key;

		public Grouping(TKey key, Enumerator<TElement> values) {
			super(values);
			this.key = key;
		}

		public TKey getKey() {
			return key;
		}
	}

	private static class SkipEnumerable<T> implements Iterable<T> {

		private final Enumerator<T> target;
		private final int count;

		public SkipEnumerable(Enumerator<T> target, int count) {
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

	private static class TakeIterator<T> extends ReadOnlyIterator<T> {

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

	private static class RangeIterator extends ReadOnlyIterator<Integer> {

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

	private static class UnionIterator<T> extends ReadOnlyIterator<T> {

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

	private static class SelectManyIterator<TSource, TResult> extends ReadOnlyIterator<TResult> {

		private final Iterator<TSource> sourceIterator;
		private final Func1<TSource, Enumerator<TResult>> selector;
		private Iterator<TResult> resultIterator;

		public SelectManyIterator(Iterable<TSource> source, Func1<TSource, Enumerator<TResult>> selector) {
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
					resultIterator = selector.evaluate(source).iterator();
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
			return new ConcatIterator<T>(Enumerator.create(iterables).select(new Func1<Iterable<T>, Iterator<T>>() {
				public Iterator<T> evaluate(Iterable<T> x) {
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

	private static class DistinctIterator<T> extends ReadOnlyIterator<T> {

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