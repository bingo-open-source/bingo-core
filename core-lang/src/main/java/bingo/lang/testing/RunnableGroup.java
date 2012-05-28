package bingo.lang.testing;

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Collections;
import bingo.lang.StopWatch;
import bingo.lang.Strings;

public class RunnableGroup implements Runnable{
	public static final String ANONYMOUS_NAME = "anonymous name";
	public static final int DEFAULT_RUN_TIMES = 1;
	
	private List<Runnable> runnableGroup;
	private Runnable singleAction;
	
	private String name = ANONYMOUS_NAME;
	private int runTimes = DEFAULT_RUN_TIMES;
	
	private PerfResult perfResult;

	/**
	 * new a runnable group with anonymous name.
	 */
	public RunnableGroup(){
		this(ANONYMOUS_NAME, null, DEFAULT_RUN_TIMES);
	}

	/**
	 * new a runnable group with specified name.
	 * @param name the specified group name.
	 */
	public RunnableGroup(String name){
		this(name, null, DEFAULT_RUN_TIMES);
	}
	
	/**
	 * new a runnable group with anonymous name and add a runnable action or group. 
	 * @param actionOrGroup a runnable action or group to be added.
	 */
	public RunnableGroup(Runnable actionOrGroup){
		this(ANONYMOUS_NAME, actionOrGroup, DEFAULT_RUN_TIMES);
	}	
	
	/**
	 * new a runnable group with anonymous name and specified run times.
	 */
	public RunnableGroup(int runTimes){
		this(ANONYMOUS_NAME, null, runTimes);
	}
	
	/**
	 * new a runnable group with specified name and run times.
	 * @param name the specified group name.
	 * @param runTimes the specified run times.
	 */
	public RunnableGroup(String name, int runTimes){
		this(name, null, runTimes);
	}
	
	/**
	 * new a runnable group with anonymous name and add a runnable action or group
	 * and specify run times. 
	 * @param actionOrGroup a runnable action or group to be added.
	 * @param runTimes the specified run times.
	 */
	public RunnableGroup(Runnable actionOrGroup, int runTimes){
		this(ANONYMOUS_NAME, actionOrGroup, runTimes);
	}
	
	/**
	 * new a runnable group with specified name and add a runnable action or group.
	 * @param name the specified group name;
	 * @param actionOrGroup a runnable action or group to be added.
	 */
	public RunnableGroup(String name, Runnable actionOrGroup){
		this(name, actionOrGroup, DEFAULT_RUN_TIMES);
	}	
	
	/**
	 * new a runnable group with specified name and run times and add a runnable action or group.
	 * @param name the specified group name;
	 * @param actionOrGroup a runnable action or group to be added.
	 * @param runTimes the specified run times.
	 */
	public RunnableGroup(String name, Runnable actionOrGroup, int runTimes){
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
	
	private void initGroup(){
		runnableGroup = new ArrayList<Runnable>();
	}

	/**
	 * add a anonymous runnable action to current runnable group with no need to new the collection.
	 * @param action the runnable action to be added, can be another runnable group.
	 * @return the current runnable group in order to chain call.
	 */
	public RunnableGroup add(Runnable action){
		return add(tryToGetName(action), action);
	}
	
	public RunnableGroup add(String name, Runnable action){
		return add(name, action, tryToGetRunTimes(action));
	}
	
	public RunnableGroup add(Runnable action, int runTimes){
		return add(tryToGetName(action), action, runTimes);
	}

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
	
	public static int tryToGetRunTimes(Runnable action){
		int runTimes = DEFAULT_RUN_TIMES;
		if(action instanceof RunnableGroup){
			int i = ((RunnableGroup)action).getRunTimes();
			runTimes = i;
		}
		return runTimes;
	}
	
	/**
	 * add a named runnable action to current runnable group with no need to new the collection.
	 * @param action the runnable action to be added, can be another runnable group.
	 * @param name the name of the action.
	 * @return the current runnable group in order to chain call.
	 */
	public RunnableGroup add(String name, Runnable action, int runTimes){
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
	}
	
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
	
	private StopWatch runSingle(){
		StopWatch sw = StopWatch.startNew();
		for (int i = 0; i < getRunTimes(); i++) {
				singleAction.run();
		}
		sw.stop();
		return sw;
	}
	
	/**
	 * see if the runnable group is empty.
	 * @return true if the runnable group is empty or false if not.
	 */
	public boolean isEmpty(){
		return Collections.isEmpty(runnableGroup);
	}
	
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

	public PerfResult getPerfResult(){
		return this.perfResult;
	}

	public Runnable getSingleAction() {
		return singleAction;
	}

	public void setSingleAction(Runnable singleAction) {
		this.singleAction = singleAction;
	}
	
}
