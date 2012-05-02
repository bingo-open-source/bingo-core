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

import bingo.lang.StopWatch;
import bingo.lang.Strings;

/**
 * Performance measurements utiltiy for unit test.
 */
public final class Perf {
	
    public static void run(String test,Runnable action){
    	//warmup
    	action.run();
        
    	StopWatch sw = StopWatch.startNew();
    	
    	action.run();
    	
    	sw.stop();
    	
    	System.out.println(Strings.format("[{0}] -> duration : {1}ms, {2}ns",test,sw.getElapsedMilliseconds(),sw.getElapsedNanoseconds()));
    }	

    public static void run(String test,Runnable action,int times){
    	//warmup
    	action.run();
     
    	StopWatch sw = StopWatch.startNew();
    	
    	for(int i=0;i<times;i++){
    		action.run();
    	}
    	
    	sw.stop();
    	
    	System.out.println(Strings.format("[{0}] -> run {1} times, duration : {2}ms, {3}ns", test,times,sw.getElapsedMilliseconds(),sw.getElapsedNanoseconds()));
    }
}