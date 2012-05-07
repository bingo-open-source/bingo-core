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

import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

import bingo.lang.Converts;
import bingo.lang.testing.Perf;

public class ConvertsTest {

	@Test
	public void testSimplePerformanceComparsion(){
		Perf.run("Convert.toType", new Runnable() {
			public void run() {
				Converts.convert(Integer.class, "100");
			}
		},1000000);
		
		Perf.run("Integer.parse", new Runnable() {
			public void run() {
				Integer.parseInt("100");
			}
		},1000000);
		
		Perf.run("BeanUtils.convert", new Runnable() {
			public void run() {
				ConvertUtils.convert((Object)"100", Integer.class);
			}
		},1000000);		
	}
	
}
