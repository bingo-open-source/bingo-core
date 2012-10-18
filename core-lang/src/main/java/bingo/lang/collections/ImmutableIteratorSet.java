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

import java.util.AbstractSet;
import java.util.Iterator;

import bingo.lang.Func;
import bingo.lang.Immutable;

public class ImmutableIteratorSet<E> extends AbstractSet<E> implements Immutable {

	private final Func<Iterator<E>> iterator;
	private final int               size;
	
	public ImmutableIteratorSet(int size,Func<Iterator<E>> iterator) {
		this.iterator = iterator;
		this.size     = size;
	}

	@Override
    public Iterator<E> iterator() {
	    return iterator.apply();
    }

	@Override
    public int size() {
	    return size;
    }
}