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

import java.util.List;
import java.util.Set;

import bingo.lang.collections.WrappedImmutableList;
import bingo.lang.collections.WrappedImmutableSet;

public class Immutables {

	protected Immutables(){
		
	}
	
	public static <E> List<E> listOf(List<E> list) {
		return new WrappedImmutableList<E>(list);
	}
	
	public static <E> Set<E> setOf(Set<E> set) {
		return new WrappedImmutableSet<E>(set);
	}
}
