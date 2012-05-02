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
	
    /**
     * Create a new {@link ArrayList}.
     */
    public static <T> ArrayList<T> list() {
        return new ArrayList<T>();
    }
    
    /**
     * Create a new {@link ArrayList}.
     */
    public static <T> ArrayList<T> list(T... elements) {
        ArrayList<T> list = new ArrayList<T>();
        
        for(T e : elements){
        	list.add(e);
        }
        
        return list;
    }    
    
    /**
     * Create a new {@link ArrayList}.
     */
    public static <T> ArrayList<T> list(Collection<T> c) {
        return new ArrayList<T>(c);
    }
    
    /**
     * Create a new {@link HashSet}.
     */
    public static <T> HashSet<T> set(){
    	return new HashSet<T>();
    }
    
    /**
     * Create a new {@link HashSet}.
     */
    public static <T> HashSet<T> set(Collection<T> c){
    	return new HashSet<T>(c);
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
}