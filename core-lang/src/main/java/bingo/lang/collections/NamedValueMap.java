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

import java.util.Map;

import bingo.lang.Named;
import bingo.lang.NamedEntry;

public interface NamedValueMap<NE extends NamedEntry<Object>> extends Map<String, Object>{
	
	boolean contains(String name);
	
	boolean contains(Named named);
	
	Object get(Named named);
	
	Object get(String name);

	Object get(int index) throws IndexOutOfBoundsException;
	
	/**
	 * same as {@link #put(int, Object)}
	 */
	NamedValueMap<NE> set(int index,Object value);
	
	/**
	 * same as {@link #put(String, Object)}
	 */
	NamedValueMap<NE> set(String name,Object value);
	
	/**
	 * same as {@link #put(Named, Object)}
	 */
	NamedValueMap<NE> set(Named named,Object value);
	
	boolean trySet(int index,Object value);
	
	boolean trySet(String name,Object value);
	
	boolean trySet(Named named,Object value);
	
	/**
	 * same as {@link #putAll(Map)}
	 */
	NamedValueMap<NE> setAll(Map<? extends String,? extends Object> map);
	
	Object remove(int index);
	
	Object remove(Named named);
	
	NE getEntry(int index) throws IndexOutOfBoundsException;
	
	NE getEntry(String name);
	
	NE getEntry(Named named);
	
	<T> T toObject(Class<T> type);
}