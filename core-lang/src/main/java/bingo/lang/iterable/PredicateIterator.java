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
package bingo.lang.iterable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import bingo.lang.Out;
import bingo.lang.Predicate;

public class PredicateIterator<T> extends ImmutableIteratorBase<T> {

	private final Iterator<T>	iterator;
	private final Predicate<T>	predicate;
	private boolean	           useEx	= true; // ( slightly faster in perf tests)

	public PredicateIterator(Iterator<T> i, Predicate<T> p) {
		this.iterator = i;
		this.predicate = p;
	}

	@Override
	protected boolean next(Out<T> out) throws Exception {
		// exception-backed method 
		if (useEx) {
			try {
				T rt = iterator.next();
				while (!predicate.apply(rt)) {
					rt = iterator.next();
				}

				out.setValue(rt);
				return true;
			} catch (NoSuchElementException e) {
				return false;
			}
		} else {
			// non-exception-backed method
			if (iterator.hasNext()) {
				T rt = iterator.next();
				while (!predicate.apply(rt)) {
					if (iterator.hasNext()) {
						rt = iterator.next();
					} else {
						return false;
					}
				}
				out.setValue(rt);
				return true;
			} else {
				return false;
			}
		}
	}
}