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

import java.util.Iterator;

public class Iterators {

	protected Iterators() {

	}

	/**
	 * Returns the number of elements remaining in {@code iterator}. The iterator will be left exhausted: its {@code
	 * hasNext()} method will return {@code false}.
	 */
	public static int size(Iterator<?> iterator) {
		int count = 0;
		while (iterator.hasNext()) {
			iterator.next();
			count++;
		}
		return count;
	}

	/**
	 * Returns {@code true} if {@code iterator} contains {@code element}.
	 */
	public static boolean contains(Iterator<?> iterator, Object element) {
		if (element == null) {
			while (iterator.hasNext()) {
				if (iterator.next() == null) {
					return true;
				}
			}
		} else {
			while (iterator.hasNext()) {
				if (element.equals(iterator.next())) {
					return true;
				}
			}
		}
		return false;
	}
}
