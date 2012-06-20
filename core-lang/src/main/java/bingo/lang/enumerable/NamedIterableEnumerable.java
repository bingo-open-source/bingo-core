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

import bingo.lang.Enumerables;
import bingo.lang.Named;
import bingo.lang.NamedEnumerable;
import bingo.lang.Predicates;

public class NamedIterableEnumerable<E extends Named> extends IterableEnumerable<E> implements NamedEnumerable<E> {

	public NamedIterableEnumerable(Iterable<E> values) {
	    super(values);
    }

	public boolean contains(String name) {
	    return get(name) != null;
    }

	public boolean containsIgnoreCase(String name) {
	    return getIgnoreCase(name) != null;
    }

	public E get(String name) {
	    return Enumerables.firstOrNull(this,Predicates.<E>nameEquals(name));
    }

	public E getIgnoreCase(String name) {
		return Enumerables.firstOrNull(this,Predicates.<E>nameEqualsIgnoreCase(name));
    }
}
