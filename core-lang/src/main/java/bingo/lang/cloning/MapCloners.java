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
package bingo.lang.cloning;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import bingo.lang.Reflects;

@SuppressWarnings({ "unchecked", "rawtypes" })
class MapCloners {
	
	static class MapCloner<T extends Map> extends AbstractMapCloner<T> {
		protected T newInstance(T object){
			return (T)Reflects.newInstance(object.getClass());
		}
	}
	
	static class HashMapCloner extends MapCloner<HashMap> {
		@Override
        protected HashMap newInstance(HashMap object) {
	        return new HashMap();
        }
	}
	
	static class ConcurrentHashMapCloner extends MapCloner<ConcurrentHashMap> {
		@Override
        protected ConcurrentHashMap newInstance(ConcurrentHashMap object) {
	        return new ConcurrentHashMap();
        }
	}
	
	static class LinkedHashMapCloner extends MapCloner<LinkedHashMap> {
		@Override
        protected LinkedHashMap newInstance(LinkedHashMap object) {
	        return new LinkedHashMap();
        }
	}
	
	static class TreeMapCloner extends MapCloner<TreeMap> {
		@Override
        protected TreeMap newInstance(TreeMap object) {
	        return new TreeMap(object.comparator());
        }
	}
}
