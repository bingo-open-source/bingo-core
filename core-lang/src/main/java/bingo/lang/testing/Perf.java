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

import bingo.lang.Assert;
import bingo.lang.Collections;
import bingo.lang.Named;
import bingo.lang.StopWatch;

/**
 * Performance measurements utility for unit test.
 */
public final class Perf {

	public static void run(int times,Runnable runnable) {
		run("",times,runnable);
	}
	
	public static void run(String name,int times, Runnable runnable) {
		new Perf(name,times).add(name, runnable).run();
	}
	
	public static Perf create(String name,int times) {
		return new Perf(name, times);
	}
	
	private String 		    name;
	private int    		times = 1;
	private List<PerfItem> items = new ArrayList<PerfItem>();
	
	Perf(int times){
		Assert.isTrue(times > 0,"times must be large than 0");
		this.times = times;
	}
	
	Perf(String name,int times){
		this(times);
		this.name = name;
	}
	
	public Perf add(String name,Runnable runnable) {
		items.add(new PerfItem(name, runnable));
		return this;
	}
	
	public void run() {
		//warm up
		for(PerfItem item : items){
			item.warmup();
		}

		//run performance items
		
		for(PerfItem item : items){
			item.run(times);
		}
		
		//output performance results
		
		//console
		new PerfOutput.ConsoleOutput(this).execute();
		
		//html
		new PerfOutput.HtmlFileOutput(this).execute();
	}
	
	String getName(){
		return name;
	}
	
	PerfItem[] getItems(){
		return Collections.toArray(items,PerfItem.class);
	}
	
	int getTimes(){
		return times;
	}
	
	static final class PerfItem implements Named {
		
		private String    name;
		private Runnable  runnable;
		private StopWatch result;
		
		public PerfItem(String name,Runnable runnable) {
			this.name     = name;
			this.runnable = runnable; 
        }

		public String getName() {
	        return name;
        }
		
		public Runnable getRunnable(){
			return runnable;
		}
		
		public StopWatch getResult(){
			return result;
		}
		
		public void warmup(){
			runnable.run();
		}
		
		public void run(int times){
			result = StopWatch.startNew();
			
			for(int i=0;i<times;i++){
				runnable.run();
			}
			
			result.stop();
		}
	}
}
