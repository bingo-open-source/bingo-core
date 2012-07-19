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
package bingo.lang.tuple;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bingo.lang.Assert;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.NamedValue;
import bingo.lang.NamedValues;
import bingo.lang.Predicates;

public class ImmutableNamedValues<V> implements NamedValues<V> {
	
	public static <V> ImmutableNamedValues<V> empty(){
		return new ImmutableNamedValues<V>(Enumerables.<NamedValue<V>>empty());
	}
	
	public static <V> ImmutableNamedValues<V> of(NamedValue<V>... array){
		return new ImmutableNamedValues<V>(Enumerables.of(array));
	}	
	
	public static <V> ImmutableNamedValues<V> of(List<NamedValue<V>> list){
		return new ImmutableNamedValues<V>(Enumerables.of(list));
	}
	
	public static <V> ImmutableNamedValues<V> of(Set<NamedValue<V>> set){
		return new ImmutableNamedValues<V>(Enumerables.of(set));
	}	
	
	public static <V> ImmutableNamedValues<V> of(Iterable<NamedValue<V>> iterable){
		return new ImmutableNamedValues<V>(Enumerables.of(iterable));
	}
	
	private final Enumerable<NamedValue<V>> enumerable;
	
	public ImmutableNamedValues(Enumerable<NamedValue<V>> enumerable){
		Assert.notNull(enumerable);
		this.enumerable = enumerable;
	}
	
	public boolean isEmpty() {
	    return enumerable.isEmpty();
    }

	public int size() {
	    return enumerable.size();
    }
	
	public NamedValue<V> get(int index) {
	    return enumerable.get(index);
    }

	public V getValue(int index) {
		NamedValue<V> nv = get(index);
		
	    return null == nv ? null : nv.getValue();
    }

	public Iterator<NamedValue<V>> iterator() {
	    return enumerable.iterator();
    }

	public Enumerable<NamedValue<V>> asEnumerable() {
	    return enumerable;
    }

	public V getValue(String name) {
		NamedValue<V> nv = enumerable.firstOrNull(Predicates.<NamedValue<V>>nameEquals(name));
		return null == nv ? null : nv.getValue();
    }
}
