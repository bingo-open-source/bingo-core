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

import bingo.lang.testing.Perf.OutputType;


public class PerfTest {
	@Test
	public void testPerfGroup(){
		final List<String> list = new ArrayList<String>();
		list.add("111");
		list.add("22");
		list.add("3");
		
		RunnableList rg = new RunnableList()
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
		.add("item3", 1, new Runnable() {
			public void run() {
				this.getClass();
			}
		})
		.add("item4", 321, new Runnable() {
			public void run() {
				list.get(1);
			}
		});

		new Perf(rg).setProjectName("Array.get(native)").setOutputType(OutputType.ALL).run();
	}
	
	@Test
	public void testPerfMatrix(){
		RunnableMatrix matrix = new RunnableMatrix()
		.addRunnableColumn("this.getClass();", new Runnable(){
			public void run() {
				this.getClass();
            }
		}).addRunnableColumn("thisequal", new Runnable() {
			public void run() {
				this.equals(null);
			}
		}).addRunnableColumn("this", new Runnable() {
			public void run() {
				this.equals(this);
			}
		}).addRunnableColumn("this.hashCode();", new Runnable() {
			public void run() {
				this.hashCode();
			}
		}).setDefaultRunTimeFrom1to1000000();
		
		new Perf(matrix).setProjectName("this methods").setOutputType(OutputType.ALL).run();
	}
	
}
