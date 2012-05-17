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

import bingo.lang.NamedValue;
import bingo.lang.exceptions.ReadonlyException;

public class ImmutableNamedValue<V> extends Pair<String, V>  implements NamedValue<V> {
	
	private static final long serialVersionUID = -426792841413872323L;
	
	protected final String name;
	protected final V      value;
	
	public ImmutableNamedValue(String name,V value){
		this.name  = name;
		this.value = value;
	}
	
	@Override
    protected String getLeft() {
	    return name;
    }

	@Override
    protected V getRight() {
	    return value;
    }
	
	@Override
    public String getKey() {
	    return name;
    }

	public V getValue() {
	    return value;
    }

	public String getName() {
	    return name;
    }
	
	public V setValue(V value) {
	    throw new ReadonlyException();
    }
}