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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import bingo.lang.Reflects;

@SuppressWarnings({"rawtypes","unchecked"})
public class CollectionCloners {

	static class CollectionCloner<T extends Collection> extends AbstractCollectionCloner<T> {
	    @Override
	    public T newInstance(T o) {
		    return (T)Reflects.newInstance(o.getClass());
	    }
	}
	
	static class ArrayListCloner extends AbstractCollectionCloner<ArrayList>{
		@Override
	    public ArrayList newInstance(ArrayList o) {
		    return new ArrayList();
	    }
	}	
	
	static class LinkedListCloner extends AbstractCollectionCloner<LinkedList>{
		@Override
	    public LinkedList newInstance(LinkedList o) {
		    return new LinkedList();
	    }
	}
	
	static class CopyOnWriteArrayListCloner extends AbstractCollectionCloner<CopyOnWriteArrayList>{
		@Override
	    public CopyOnWriteArrayList newInstance(CopyOnWriteArrayList o) {
		    return new CopyOnWriteArrayList();
	    }
	}
	
	static class HashSetCloner extends AbstractCollectionCloner<HashSet> {
		@Override
        public HashSet newInstance(HashSet o) {
	        return new HashSet();
        }
	}
	
	static class LinkedHashSetCloner extends AbstractCollectionCloner<LinkedHashSet> {
		@Override
        public LinkedHashSet newInstance(LinkedHashSet o) {
	        return new LinkedHashSet();
        }
	}
	
	static class TreeSetCloner extends AbstractCollectionCloner<TreeSet> {
		@Override
        public TreeSet newInstance(TreeSet o) {
	        return new TreeSet(o.comparator());
        }
	}
	
	static class CopyOnWriteArraySetCloner extends AbstractCollectionCloner<CopyOnWriteArraySet> {
		@Override
        public CopyOnWriteArraySet newInstance(CopyOnWriteArraySet o) {
	        return new CopyOnWriteArraySet();
        }
	}
}
