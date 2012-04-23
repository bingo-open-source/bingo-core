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
package bingo.utils.testing;

import bingo.lang.Action;
import bingo.utils.StopWatch;
import bingo.utils.Strings;
import bingo.utils.format.DurationFormatter;

/**
 * Performance measurements utiltiy for test case.
 *
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
public final class Perf {
	
    public static void run(String test,Action action){
        
    	StopWatch sw = StopWatch.startNew();
    	
    	action.execute();
    	
    	sw.stop();
    	
    	System.out.println(Strings.format("[{0}] -> duration : {1}",test,DurationFormatter.formatHMS(sw.getElapsedMilliseconds())));
    }	

    public static void run(String test,Action action,int times){
     
    	StopWatch sw = StopWatch.startNew();
    	
    	for(int i=0;i<times;i++){
    		action.execute();
    	}
    	
    	sw.stop();
    	
    	System.out.println(Strings.format("[{0}] -> run {1} times, duration : {2}", test,times,DurationFormatter.formatHMS(sw.getElapsedMilliseconds())));
    }
}