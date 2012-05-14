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
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import bingo.lang.testing.Perf;

import static org.junit.Assert.*;

public class CollectionsTest {

	@Test
	public void testPerformanceComparesToEnumerable() {
		final List<Integer> list = Enumerable.create(1, 3, 2, 4, 100, 1, 2, 3, 5, 531).toList();

		final Predicate<Integer> where = new Predicate<Integer>() {
			public boolean evaluate(Integer object) {
				return true;
			}
		};

		Perf.run("Collections.where", new Runnable() {
			public void run() {
				Collections.where(list, where).toArray(new Integer[] {});
			}
		}, 100000);

		Perf.run("Enumerable.where", new Runnable() {
			public void run() {
				Enumerable.create(list).where(where).toArray(new Integer[] {});
			}
		}, 100000);

		Perf.run("Handcode.foreach", new Runnable() {
			public void run() {
				List<Integer> newList = new ArrayList<Integer>();
				for (Integer i : list) {
					if (where.evaluate(i)) {
						newList.add(i);
					}
				}
				newList.toArray(new Integer[] {});
			}
		}, 100000);
	}

	@Test
	public void testToArrayWithComponentType() {
		String[] 	 array = new String[] { "test1", "test2", "test3" };
		List<String> list  = Arrays.asList(array);

		assertTrue(Arrays.equals(array, Collections.toArray(list, String.class)));
		assertTrue(Collections.toArray(new ArrayList<String>(), String.class).length == 0);
	}
}
