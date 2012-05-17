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

import java.util.Collection;

import bingo.lang.Reflects;

//internal collection utils
abstract class CollectionUtils {

	static Object[] toArrayImpl(Collection<?> c) {
		return fillArray(c, new Object[c.size()]);
	}

	@SuppressWarnings("unchecked")
	static <T> T[] toArrayImpl(Collection<?> c, T[] array) {
		int size = c.size();
		if (array.length < size) {
			array = Reflects.newArray((Class<T>) array.getClass().getComponentType(), size);
		}
		fillArray(c, array);
		if (array.length > size) {
			array[size] = null;
		}
		return array;
	}

	static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
		if(null == self){
			return false;
		}
		
		for (Object o : c) {
			if (!self.contains(o)) {
				return false;
			}
		}
		
		return true;
	}
	
	private static Object[] fillArray(Iterable<?> elements, Object[] array) {
		int i = 0;
		for (Object element : elements) {
			array[i++] = element;
		}
		return array;
	}
}
