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
package bingo.lang.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleLRUMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 4611621372516967117L;
	
	private final int maxSize;

	public SimpleLRUMap(final int initialSize, final int maxSize) {
		super(initialSize);
		this.maxSize = maxSize;
	}

	public SimpleLRUMap(final int size) {
		this(size, size);
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
		return size() > this.maxSize;
	}
}