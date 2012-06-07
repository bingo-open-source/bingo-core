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
import java.util.LinkedList;
import java.util.List;

import bingo.lang.Collections;
import bingo.lang.StopWatch;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

/**
 * represents a runnable group, which can contains other runnable groups or runnable actions or both.
 * @author Calvin Chen
 */
public class RunnableList implements Runnable{
	
	private static final Log log = LogFactory.get(RunnableList.class);
	
	public static final int DEFAULT_GLOBAL_RUN_TIMES = 100000;
	
	private List<NamedRunnable> list;
	
	private List<PerfResult> resultList;
	
	private int globalRunTimes = DEFAULT_GLOBAL_RUN_TIMES;
	
	/* constructors */
	
	public RunnableList(){
		this(DEFAULT_GLOBAL_RUN_TIMES);
	}
	
	public RunnableList(int globalRunTimes){
		log.debug("execute construtor method in [{}] with globalRunTimes[{}]",
				this.getClass().getName(), globalRunTimes);
		this.globalRunTimes = globalRunTimes;
	}
	
	/* public methods */

	public RunnableList add(NamedRunnable item){
		if(null == list){
			list = new ArrayList<NamedRunnable>();
		}
		list.add(item);
		log.debug("add item named [{}]", item.getName());
		return this;
	}
	
	public RunnableList add(String name, Runnable runnable){
		add(new NamedRunnable(name, runnable));
		return this;
	}
	
	public RunnableList add(String name, int runTime, Runnable runnable){
		add(new NamedRunnable(name, runTime, runnable));
		return this;
	}

	public void run() {
		log.debug("execute run() method in [{}]", this.getClass().getName());
		resultList = new LinkedList<PerfResult>();
		if(Collections.isEmpty(list) == false){
			for (NamedRunnable item : list) {
				Integer runTimes = item.getRunTimes();
				if(null == runTimes){
					runTimes = globalRunTimes;
				}
				
				log.debug("running item[{}] in RunnableList with [{}] times...", item.getName(), runTimes);
				StopWatch sw = StopWatch.startNew();
				for (int i = 0; i < runTimes; i++) {
					item.run();
				}
				sw.stop();
				log.debug("run item[{}] in RunnableList with [{}] times over.", item.getName(), runTimes);
				
				PerfResult child = new PerfResult(sw.getElapsedNanoseconds(), sw.getElapsedMilliseconds())
									.setName(item.getName()).setRunTimes(runTimes);
				resultList.add(child);
            }
		} else {
			log.warn("the runnable list is empty.");
		}
	}
	
	/* getter and setter */
	
	public int getGlobalRunTimes() {
		return globalRunTimes;
	}

	public RunnableList setGlobalRunTimes(int globalRunTimes) {
		this.globalRunTimes = globalRunTimes;
		log.debug("set new global run times as [{}]", globalRunTimes);
		return this;
	}
	
	public RunnableList setAllRunTimes(int allRunTimes){
		this.globalRunTimes = allRunTimes;
		for (NamedRunnable item : list) {
	        item.setRunTimes(allRunTimes);
        }
		log.debug("set all run times as [{}].", allRunTimes);
		return this;
	}

	public List<NamedRunnable> getList() {
    	return list;
    }

	public List<PerfResult> getResultList() {
    	return resultList;
    }
}
