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

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Collections;
import bingo.lang.Enumerable;
import bingo.lang.collections.AbstractListWrapper;
import bingo.lang.exceptions.EmptyDataException;
import bingo.lang.exceptions.TooManyDataException;

public class ListEnumerable<E> extends AbstractListWrapper<E> implements Enumerable<E>,List<E>  {
	
	public ListEnumerable() {
	    this.list = new ArrayList<E>();
    }

	public ListEnumerable(List<E> list) {
	    super(list);
    }
	
	public E first() throws EmptyDataException {
	    if(list.size() == 0){
	    	throw new EmptyDataException();
	    }
	    return list.get(0);
    }

	public E firstOrNull() {
	    if(isEmpty()){
	    	return null;
	    }
	    return list.get(0);
    }
	
	public E single() throws EmptyDataException, TooManyDataException {
		int size = list.size();
	    if(size == 0){
	    	throw new EmptyDataException();
	    }
	    if(size > 1){
	    	throw new TooManyDataException();
	    }
	    return list.get(0);
    }
	
	public List<E> list(){
		return list;
	}

	public E[] toArray(Class<E> type) {
	    return Collections.toArray(this,type);
    }
	
	public List<E> toList() {
	    return new ArrayList<E>(this);
    }
}