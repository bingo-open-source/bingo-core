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

import bingo.lang.Strings;

class PerfResult {

	private String name;
	private int runTimes;
	private long elapsedNanoseconds;
	private long elapsedMilliseconds;
	
	public PerfResult(){
	}
	
	public PerfResult(long elapsedNanoseconds, long elapsedMilliseconds) {
		this.elapsedNanoseconds = elapsedNanoseconds;
		this.elapsedMilliseconds = elapsedMilliseconds;
	}
	
	public String toString(){
		return Strings.format("[{0}] -> run {3} times duration : {1}ms, {2}ns",
				getName(), getElapsedMilliseconds(), getElapsedNanoseconds(),
				getRunTimes());
	}

	public long getElapsedNanoseconds() {
		return elapsedNanoseconds;
	}

	public void setElapsedNanoseconds(long elapsedNanoseconds) {
		this.elapsedNanoseconds = elapsedNanoseconds;
	}

	public long getElapsedMilliseconds() {
		return elapsedMilliseconds;
	}

	public void setElapsedMilliseconds(long elapsedMilliseconds) {
		this.elapsedMilliseconds = elapsedMilliseconds;
	}
	
	public String getName() {
		return name;
	}

	public PerfResult setName(String name) {
		this.name = name;
		return this;
	}

	public int getRunTimes() {
		return runTimes;
	}

	public PerfResult setRunTimes(int runTimes) {
		this.runTimes = runTimes;
		return this;
	}
	
}
