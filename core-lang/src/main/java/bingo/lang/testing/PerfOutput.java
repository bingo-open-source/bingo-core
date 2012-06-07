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
import bingo.lang.testing.Perf.PerfItem;

abstract class PerfOutput {
	
	private static final String HEADER_ITEM_NAME            = "Item Name";
	private static final String HEADER_ELAPSED_MILLISECONDS = "Elapsed Milliseconds";
	private static final String HEADER_ELAPSED_NANOSECONDS  = "Elapsed Nanoseconds";

	protected Perf perf; 
	
	PerfOutput(Perf perf) {
		this.perf = perf;
	}
	
	abstract void execute();
	
	
	/**
	 * 
	 * Benchmark [: {name} ] , Run Times : {times}  
	 * ---------------------------------------------------------------
	 * item			| ms	| ns	 
	 * ---------------------------------------------------------------
	 * {item1}		| 0     | 0
	 * {item2}      | 0     | 0
	 * 
	 */
	static final class ConsoleOutput extends PerfOutput {
		private int maxItemNameColumnLength;
		private int maxElapsedMillisecondsColumnLength;
		private StringBuilder out;
		
		public ConsoleOutput(Perf perf) {
			super(perf);
			this.maxItemNameColumnLength = getMaxItemNameLength(perf) + 5;
			this.maxElapsedMillisecondsColumnLength = getMaxElapsedMillisecondsLength(perf) + 5;
			this.out = new StringBuilder(128);
        }

		@Override
        void execute() {
			header();
			
			PerfItem[] items = perf.getItems();
			
			for(int i=0;i<items.length;i++){
				item(items[i],i+1);
			}
			
			footer();
			
			System.out.println(out);
        }
		
		private void header() {
			out.append("\n");
			out.append(" Benchmark");
			
			if(!Strings.isBlank(perf.getName())) {
				out.append(" [ ").append(perf.getName()).append(" ]");
			}
			
			out.append(" , Run Times [ ").append(perf.getTimes()).append(" ]\n")
			   .append(Strings.repeat('-', 100)).append("\n")
			   .append(" ")
			   .append(Strings.padRight(HEADER_ITEM_NAME, maxItemNameColumnLength))
			   .append("| ")
			   .append(Strings.padRight(HEADER_ELAPSED_MILLISECONDS, maxElapsedMillisecondsColumnLength))
			   .append("| ")
			   .append(HEADER_ELAPSED_NANOSECONDS)
			   .append("\n")
			   .append(Strings.repeat('-', 100)).append("\n");
			
		}
		
		private void item(PerfItem item,int index) {

			StopWatch result = item.getResult();
			String name = item.getName();
			if(Strings.isBlank(name)){
				name = "Item" + index;
			}
			
			out.append(" ")
			   .append(Strings.padRight(name, maxItemNameColumnLength))
			   .append("| ")
			   .append(Strings.padRight(String.valueOf(result.getElapsedMilliseconds()), maxElapsedMillisecondsColumnLength))
			   .append("| ")
			   .append(String.valueOf(result.getElapsedNanoseconds()))
			   .append("\n");
		}
		
		private void footer(){
			out.append("\n");
		}
		
		private static int getMaxItemNameLength(Perf perf){
			
			int max = HEADER_ITEM_NAME.length();
			
			for(PerfItem item : perf.getItems()){
				max = Math.max(max, Strings.length(item.getName()));
			}
			
			return max;
		}
		
		private static int getMaxElapsedMillisecondsLength(Perf perf){
			
			int max = HEADER_ELAPSED_MILLISECONDS.length();
			
			for(PerfItem item : perf.getItems()){
				max = Math.max(max,String.valueOf(item.getResult().getElapsedMilliseconds()).length());
			}
			
			return max;
		}
	}
	
	static final class HtmlFileOutput extends PerfOutput {

		public HtmlFileOutput(Perf perf) {
	        super(perf);
        }

		@Override
        void execute() {
	        
        }
	}
}
