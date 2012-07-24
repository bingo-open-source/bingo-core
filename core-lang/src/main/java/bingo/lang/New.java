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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class New {
	
	protected New() {
		
	}
	
	public static <T> Out<T> out() {
		return new OutObject<T>();
	}
	
    /**
     * Create a new {@link ArrayList}.
     */
    public static <E> ArrayList<E> list() {
        return new ArrayList<E>();
    }
    
    /**
     * Create a new {@link ArrayList}.
     */
    public static <E> ArrayList<E> list(E... elements) {
        ArrayList<E> list = new ArrayList<E>();
        
        for(E e : elements){
        	list.add(e);
        }
        
        return list;
    }    
    
    /**
     * Create a new {@link ArrayList}.
     */
    public static <E> ArrayList<E> list(Collection<E> c) {
        return new ArrayList<E>(c);
    }
    
    /**
     * Create a new {@link HashSet}.
     */
    public static <E> HashSet<E> set(){
    	return new HashSet<E>();
    }
    
    /**
     * Create a new {@link HashSet}.
     */
    public static <E> HashSet<E> set(E... elements){
    	HashSet<E> set = new HashSet<E>();
    	
    	for(E e : elements){
    		set.add(e);
    	}
    	
    	return set;
    }
    
    /**
     * Create a new {@link HashSet}.
     */
    public static <E> HashSet<E> set(Collection<E> c){
    	return new HashSet<E>(c);
    }    
    
    /**
     * Create a new {@link HashMap}
     */
    public static <K,V> HashMap<K,V> map(){
    	return new HashMap<K, V>();
    }
    
    /**
     * Create a new {@link HashMap}
     */
    public static <K,V> HashMap<K,V> map(Map<K,V> map){
    	return new HashMap<K, V>(map);
    }
    
    /**
     * Create a new {@link HashMap}
     */
    public static <K,V> HashMap<K,V> map(K key,V value){
    	HashMap<K, V> map = new HashMap<K, V>();
    	
    	map.put(key, value);
    	
    	return map;
    }

    /**
     * Create a new {@link HashMap}
     */
    public static <K,V> HashMap<K,V> map(K k1,V v1,K k2,V v2){
    	HashMap<K, V> map = new HashMap<K, V>();
    	
    	map.put(k1, v1);
    	map.put(k2, v2);
    	
    	return map;
    }
    
    /**
     * Create a new {@link HashMap}
     */
    public static <K,V> HashMap<K,V> map(K k1,V v1,K k2,V v2,K k3,V v3){
    	HashMap<K, V> map = new HashMap<K, V>();
    	
    	map.put(k1, v1);
    	map.put(k2, v2);
    	map.put(k3, v3);
    	
    	return map;
    }
    
    /**
     * Create a new {@link HashMap}
     */
    public static <K,V> HashMap<K,V> map(K k1,V v1,K k2,V v2,K k3,V v3,K k4,V v4){
    	HashMap<K, V> map = new HashMap<K, V>();
    	
    	map.put(k1, v1);
    	map.put(k2, v2);
    	map.put(k3, v3);
    	map.put(k4, v4);
    	
    	return map;
    }

    /**
     * Create a new {@link HashMap}
     */
    public static <K,V> HashMap<K,V> map(K k1,V v1,K k2,V v2,K k3,V v3,K k4,V v4,K k5,V v5){
    	HashMap<K, V> map = new HashMap<K, V>();
    	
    	map.put(k1, v1);
    	map.put(k2, v2);
    	map.put(k3, v3);
    	map.put(k4, v4);
    	map.put(k5, v5);
    	
    	return map;
    } 
}