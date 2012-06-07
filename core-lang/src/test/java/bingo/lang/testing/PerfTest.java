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
package bingo.lang.testing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


@SuppressWarnings("unused")
public class PerfTest {
	
	@Test
	public void testRunnableSingle(){
		Perf.run(10000,new Runnable() {
			public void run() {
				int i = 1 + 100;
			}
		});
		
		Perf.run("PerfTestSingle", 10000,new Runnable() {
			public void run() {
				int i = 1 + 100;
			}
		});
	}
	
	@Test
	public void testRunnableGroup(){
		final List<String> list = new ArrayList<String>();
		list.add("111");
		list.add("22");
		list.add("3");
		
		Perf.create("PerfTestGroup", 10000)
			.add("item1", new Runnable() {
				public void run() {
					list.size();
				}
			})
			.add("item2", new Runnable() {
				public void run() {
					list.size();
				}
			})		
			.add("item3", new Runnable() {
				public void run() {
					this.getClass();
				}
			})
			.add("item4", new Runnable() {
				public void run() {
					list.get(1);
				}
			})
			.run();
	}
}
