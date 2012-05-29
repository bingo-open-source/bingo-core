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


public class PerfTest {
	@Test
	public void testPerfGroup(){
		final List<String> list = new ArrayList<String>();
		list.add("111");
		list.add("22");
		list.add("3");
		
		RunnableGroup rg = new RunnableGroup("group100000")
		.add("item1", 123,new Runnable() {
			public void run() {
				list.size();
			}
		})
		.add("item2", 123,new Runnable() {
			public void run() {
				list.size();
			}
		})		
		.add("group1.1", 1, new RunnableGroup()
			.add("123456", 1000000, new Runnable() {
				public void run() {
					list.getClass();
				}
			})
			.add("group1.1.3", 1, new RunnableGroup()
				.add("item3", 123, new Runnable() {
					public void run() {
						list.equals(list);
					}
				}))
		)
		.add("item2", 321, new Runnable() {
			public void run() {
				list.get(1);
			}
		});
		
		Runnable r = new Runnable() {
			public void run() {
				list.get(1);
			}
		};

		Perf perf = new Perf(rg);
		perf.setProjectName("Array.get(native)");
		perf.run();
	}
	
	@Test
	public void testPerfMatrix(){
		RunnableMatrix matrix = new RunnableMatrix();
		matrix.addAction("this.getClass();", new Runnable(){
			public void run() {
				this.getClass();
            }
		}).addAction("this.equals(null);", new Runnable() {
			public void run() {
				this.equals(null);
			}
		}).addAction("this.equals(this);", new Runnable() {
			public void run() {
				this.equals(this);
			}
		}).addAction("this.hashCode();", new Runnable() {
			public void run() {
				this.hashCode();
			}
		});
		matrix.addRunTimes(1, 10, 100, 1000, 10000, 100000, 1000000);
		Perf perf = new Perf(matrix);
		perf.setProjectName("this methods");
		perf.setShowResultTo(Perf.TO_CONSOLE_AND_HTML);
		perf.run();
	}
	
}
