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

import org.junit.Test;

import bingo.lang.testing.Perf;

public class LazyTest extends org.junit.Assert{

	@Test
	public void testPerf(){
		final T2 t2 = new T2();
		assertSame(t2.getValue(), t2.getValue());
		
		Perf.create("LazyPerf1", 1000000)
		    .add("Normal", new Runnable() {
				public void run() {
					T1 t1 = new T1();
					t1.getValue();
					t1.getValue();
					t1.getValue();
				}
			})
			.add("Lazy", new Runnable() {
				public void run() {
					T2 t2 = new T2();
					
					t2.getValue();
					t2.getValue();
					t2.getValue();
				}
			})
			.run();
		
		
		Perf.create("LazyPerf2", 1000000)
	    .add("Normal", new Runnable() {
			public void run() {
				new T1();
			}
		})
		.add("Lazy", new Runnable() {
			public void run() {
				new T2();
			}
		})
		.run();
	}
	
	private static final class T1 {
		private Object value;
		
		public Object getValue(){
			if(null == value){
				value = new Object();
			}
			return value;
		}
	}
	
	private static final class T2{
		private final Lazy<Object> value = new Lazy<Object>() {
			@Override
            protected Object get() {
	            return new Object();
            }
		};
		
		public Object getValue(){
			return value.apply();
		}
	}
}
