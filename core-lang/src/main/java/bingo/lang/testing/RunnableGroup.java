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

import bingo.lang.Collections;
import bingo.lang.StopWatch;
import bingo.lang.Strings;

/**
 * represents a runnable group, which can contains other runnable groups or runnable actions or both.
 * @author Calvin Chen
 */
public class RunnableGroup implements Runnable{
	/**
	 * anonymous name for groups or actions if their name isn't be specified.
	 */
	public static final String ANONYMOUS_NAME = "anonymous name";
	
	/**
	 * the default run times for groups or actions if their run times isn't be specified.
	 */
	public static final int DEFAULT_RUN_TIMES = 1;
	
	/**
	 * contained runnable groups.
	 */
	private List<Runnable> runnableGroup;
	
	/**
	 * if this group only contains a single action.
	 */
	private Runnable singleAction;
	
	/**
	 * name of this runnable group.
	 */
	private String name = ANONYMOUS_NAME;
	
	/**
	 * run times of this runnable group.
	 */
	private int runTimes = DEFAULT_RUN_TIMES;
	
	/**
	 * performance result of this runnable group.
	 */
	private PerfResult perfResult;
	
	/**
	 * mark this runnable group whether has run.
	 * true means Has Run and can get the performance result. 
	 * false means Has Not Run and cannot get the performance result.
	 */
	private boolean hasRun = false;

	/* constructors */
	
	/**
	 * new a runnable group with anonymous name.
	 */
	public RunnableGroup(){
		this(ANONYMOUS_NAME, DEFAULT_RUN_TIMES, null);
	}

	/**
	 * new a runnable group with specified name.
	 * @param name the specified group name.
	 */
	public RunnableGroup(String name){
		this(name, DEFAULT_RUN_TIMES, null);
	}
	
	/**
	 * new a runnable group with anonymous name and add a runnable action or group. 
	 * @param actionOrGroup a runnable action or group to be added.
	 */
	public RunnableGroup(Runnable actionOrGroup){
		this(ANONYMOUS_NAME, DEFAULT_RUN_TIMES, actionOrGroup);
	}	
	
	/**
	 * new a runnable group with anonymous name and specified run times.
	 */
	public RunnableGroup(int runTimes){
		this(ANONYMOUS_NAME, runTimes, null);
	}
	
	/**
	 * new a runnable group with specified name and run times.
	 * @param name the specified group name.
	 * @param runTimes the specified run times.
	 */
	public RunnableGroup(String name, int runTimes){
		this(name, runTimes, null);
	}
	
	/**
	 * new a runnable group with anonymous name and add a runnable action or group
	 * and specify run times. 
	 * @param actionOrGroup a runnable action or group to be added.
	 * @param runTimes the specified run times.
	 */
	public RunnableGroup(int runTimes, Runnable actionOrGroup){
		this(ANONYMOUS_NAME, runTimes, actionOrGroup);
	}
	
	/**
	 * new a runnable group with specified name and add a runnable action or group.
	 * @param name the specified group name;
	 * @param actionOrGroup a runnable action or group to be added.
	 */
	public RunnableGroup(String name, Runnable actionOrGroup){
		this(name, DEFAULT_RUN_TIMES, actionOrGroup);
	}	
	
	/**
	 * new a runnable group with specified name and run times and add a runnable action or group.
	 * @param name the specified group name;
	 * @param actionOrGroup a runnable action or group to be added.
	 * @param runTimes the specified run times.
	 */
	public RunnableGroup(String name, int runTimes, Runnable actionOrGroup){
		this.name = name;
		this.runTimes = runTimes;
		if(null != actionOrGroup){
			if((actionOrGroup instanceof RunnableGroup) == false){
				this.singleAction = actionOrGroup;
			} else {
				if(null == runnableGroup){
					initGroup();
				}
				RunnableGroup group = (RunnableGroup) actionOrGroup;
				group.setName(name);
				group.setRunTimes(runTimes);
				this.runnableGroup.add(group);
			}
		}
	}
	
	/* public methods */

	/**
	 * add a anonymous runnable action to current runnable group.
	 * @param action the runnable action to be added, can be another runnable group.
	 * @return the current runnable group in order to chain call.
	 */
	public RunnableGroup add(Runnable action){
		return add(tryToGetName(action), action);
	}
	
	/**
	 * add a named runnable action to current runnable group.
	 * @param name name of the action.
	 * @param action action to be added.
	 * @return the current runnable group in order to chain call.
	 */
	public RunnableGroup add(String name, Runnable action){
		return add(name, tryToGetRunTimes(action), action);
	}
	
	/**
	 * add an anonymous action with run times to current runnable group.
	 * @param action action to be added.
	 * @param runTimes run times of this action.
	 * @return the current runnable group in order to chain call.
	 */
	public RunnableGroup add(Runnable action, int runTimes){
		return add(tryToGetName(action), runTimes, action);
	}
	
	/**
	 * add a named runnable action to current runnable group with no need to new the collection.
	 * @param action the runnable action to be added, can be another runnable group.
	 * @param name the name of the action.
	 * @return the current runnable group in order to chain call.
	 */
	public RunnableGroup add(String name, int runTimes, Runnable action){
		/* if the element to be added is the first element */
		if(null == runnableGroup){
			initGroup();
		}
		
		/* if it's a instance of runnable group, set the run times and add it.
		 * if not, mark it has a single action.
		 */
		RunnableGroup group = null;
		if(action instanceof RunnableGroup){
			group = (RunnableGroup)action;
		} else {
			group = new RunnableGroup();
			group.setSingleAction(action);
		}
		group.setRunTimes(runTimes);
		group.setName(name);
		runnableGroup.add(group);
		
		return this;
	}
	
	/**
	 * try to get the name of a runnable action, therefore it's no need to use anonymous name.
	 */
	public static String tryToGetName(Runnable action){
		String name = ANONYMOUS_NAME;
		if(action instanceof RunnableGroup){
			String str = ((RunnableGroup)action).getName();
			if(!Strings.isEmpty(str)){
				name = str;
			}
		}
		return name;
	}
	
	/**
	 * try to get the run times of a runnable action, therefore it's no need to use default run times.
	 */
	public static int tryToGetRunTimes(Runnable action){
		int runTimes = DEFAULT_RUN_TIMES;
		if(action instanceof RunnableGroup){
			int i = ((RunnableGroup)action).getRunTimes();
			runTimes = i;
		}
		return runTimes;
	}

	/**
	 * run all the runnable actions in the runnable group.
	 * if the run times was not set before, default run times is one.
	 */
	public void run() {
		perfResult = new PerfResult();
		StopWatch sw = null;
		if(null == singleAction){
			sw = runGroup();
		} else {
			sw = runSingle();
			perfResult.setEnd(true);
		}
		
		perfResult.setName(name);
		perfResult.setRunTimes(runTimes);
		perfResult.setElapsedMilliseconds(sw.getElapsedMilliseconds());
		perfResult.setElapsedNanoseconds(sw.getElapsedNanoseconds());
		
		hasRun = true;
	}
	
	/**
	 * see if the runnable group is empty.
	 * @return true if the runnable group is empty or false if not.
	 */
	public boolean isEmpty(){
		return Collections.isEmpty(runnableGroup);
	}
	
	/* getter and setter */
	
	/**
	 * get the name of the group.
	 * @return the name of the group.
	 */
	public String getName() {
		return name;
	}

	/**
	 * set the name of the group.
	 * @param name the name of the group.
	 * @return the current runnable group to be chain called.
	 */
	public RunnableGroup setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * get run times of this runnable group.
	 * @return run times of this runnable group.
	 */
	public int getRunTimes() {
		return runTimes;
	}

	/**
	 * set run times of this runnable group.
	 * @param runTimes how much does this runnable group run.
	 * @return the current runnable group to be chain called.
	 */
	public RunnableGroup setRunTimes(int runTimes) {
		this.runTimes = runTimes;
		return this;
	}

	/**
	 * get the performance result of this runnable group.
	 * @return the performance result or null if the run method of this class haven't be invoked yet.
	 */
	public PerfResult getPerfResult(){
		if(false == hasRun){
			throw new RuntimeException(
					"RunnableGroup hasn't run yet! please get the performance result after run.");
		}
		return perfResult;
	}

	/**
	 * get the single action of this class.
	 * @return the single action of this class or null if this class contains a runnable group.
	 */
	public Runnable getSingleAction() {
		return singleAction;
	}

	/**
	 * set the single action of this class.
	 * @param singleAction the action which wish to be set.
	 */
	public void setSingleAction(Runnable singleAction) {
		if(singleAction instanceof RunnableGroup){
			throw new RuntimeException("the single action member cannot be set to a RunnableGroup!");
		}
		this.singleAction = singleAction;
	}

	public boolean isHasRun() {
    	return hasRun;
    }

	public void setHasRun(boolean hasRun) {
    	this.hasRun = hasRun;
    }
	
	/* private methods */
	
	/**
	 * init the group list for add elements.
	 */
	private void initGroup(){
		runnableGroup = new ArrayList<Runnable>();
	}
	
	/**
	 * run the runnable group contained in this class in recursion.
	 * @return the counted {@link StopWatch} as a run time result.
	 */
	private StopWatch runGroup(){
		StopWatch sw = StopWatch.startNew();
		for (int i = 0; i < getRunTimes(); i++) {
			for (Runnable action : runnableGroup) {
				action.run();
				perfResult.addChild(((RunnableGroup)action).getPerfResult());
			}
		}
		sw.stop();
		return sw;
	}
	
	/**
	 * run the single runnable action in this class.
	 * @return the counted {@link StopWatch} as a run time result.
	 */
	private StopWatch runSingle(){
		StopWatch sw = StopWatch.startNew();
		for (int i = 0; i < getRunTimes(); i++) {
				singleAction.run();
		}
		sw.stop();
		return sw;
	}
	
}
