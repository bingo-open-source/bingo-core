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

public class For {
	
	public static <T> void each(T[] array,For.Action<T> action){
		if(null == array){
			return;
		}
		
		for(T item : array){
			action.item(item);
		}
	}
	
	public static <T> void each(Iterable<T> iterable,For.Action<T> action){
		if(null == iterable){
			return;
		}
		
		for(T item : iterable){
			action.item(item);
		}
	}
	
	public static <T> void each(T[] array,For.Action1<T> action){
		if(null == array){
			return ;
		}
		
		int i=0;
		for (T e : array) {
			if (i == 0) {
				action.item(true,e);
				i=1;
			}else{
				action.item(false,e);
			}
		}
	}
	
	public static <T> void each(Iterable<T> iterable,For.Action1<T> action){
		if(null == iterable){
			return ;
		}
		
		int i=0;
		for (T e : iterable) {
			if (i == 0) {
				action.item(true,e);
				i=1;
			}else{
				action.item(false,e);
			}
		}
	}	

	public static interface Action<T> {
		void item(T item);
	}
	
	public static interface Action1<T> {
		void item(boolean isFirstItem,T item);
	}
	
	protected For(){}
}