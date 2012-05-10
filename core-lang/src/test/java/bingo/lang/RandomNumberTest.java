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

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import bingo.lang.testing.Perf;

public class RandomNumberTest {

	private static final Random RANDOM = new Random();
	
	@Test
    public void testNextInt() {
		Perf.run("Randonms.nextInt", new Runnable() {
			public void run() {
				assertTrue(Randoms.nextInt() >= 0);
			}
		},100000);
		
		Perf.run("Random.nextInt", new Runnable() {
			public void run() {
				assertTrue(RANDOM.nextInt(Integer.MAX_VALUE) >= 0);
			}
		},100000);
    }
}