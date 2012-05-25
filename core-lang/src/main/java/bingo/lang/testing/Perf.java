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
	
    public static void run(String test, Runnable action){
    	run(test, action, 1);
    }	

    public static void run(String test,Runnable action,int times){
    	/* wrapped with runnable group */
    	String name = RunnableGroup.tryToGetName(action);
    	times = RunnableGroup.tryToGetRunTimes(action);
    	
    	RunnableGroup runnableGroup = new RunnableGroup(name, action, times);
    	
    	/* warm up */
    	runnableGroup.run();
    	
    	runnableGroup.run();
    	
    	Perf.toConsole(test, runnableGroup.getPerfResult());
    }
    
    public static void toConsole(String name, PerfResult perfResult){
    	System.out.println("PROJECT: " + name);
    	toConsole(perfResult.getChildren().get(0), "");
    }
    
    private static void toConsole(PerfResult perfResult, String prefix){
    	System.out.println(prefix + perfResult.toString());
    	prefix += "  ";
    	if(perfResult.isEnd() == false){
        	for (PerfResult perfResultChild : perfResult.getChildren()) {
    			toConsole(perfResultChild, prefix);
    		}
    	}
    }
}