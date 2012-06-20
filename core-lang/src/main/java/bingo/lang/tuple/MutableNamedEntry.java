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

import bingo.lang.NamedEntry;
import bingo.lang.NamedValue;

public class MutableNamedEntry<V> extends PairBase<String, V> implements NamedEntry<V>, NamedValue<V> {
	
	private static final long serialVersionUID = -8052820161777994131L;
	
	private final String name;
	private V 	value;
	
	public MutableNamedEntry(String name) {
	    this.name = name;
    }	

	public MutableNamedEntry(String name, V value) {
		this.name  = name;
		this.value = value;
    }
	
	public String getName() {
	    return name;
    }

	public V setValue(V value) {
		this.value = value;
	    return value;
    }
	
    public String getKey() {
	    return name;
    }

    public V getValue() {
	    return value;
    }

	public String getLeft() {
	    return name;
    }

	public V getRight() {
	    return value;
    }
}