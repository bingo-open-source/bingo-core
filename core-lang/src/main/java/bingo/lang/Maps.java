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
package bingo.lang;

import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.tuple.ImmutableNamedEntry;

/**
 * <code>null</code> safe {@link Map} utility.
 */
public class Maps {

	protected Maps() {

	}

	/**
	 * Return <code>true</code> if the supplied Map is <code>null</code> or empty. Otherwise, return <code>false</code>.
	 * 
	 * @param map the Map to check
	 * 
	 * @return whether the given Map is empty
	 */
	public static boolean isEmpty(Map<?,?> map) {
		return (map == null || map.isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	public static <V> NamedEntry<V>[] toNamedEntryArray(Map<String, V> map){
		if(null == map || map.isEmpty()){
			return new NamedEntry[]{};
		}
		
		NamedEntry<V>[] a = new NamedEntry[map.size()];
		
		int i=0;
		
		for(Entry<String, V> entry : map.entrySet()){
			a[i] = ImmutableNamedEntry.of(entry.getKey(),entry.getValue());
			
			i++;
		}
		
		return a;
	}
}
