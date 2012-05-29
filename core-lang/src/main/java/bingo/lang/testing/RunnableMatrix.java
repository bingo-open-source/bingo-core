package bingo.lang.testing;

import java.util.LinkedList;
import java.util.List;

public class RunnableMatrix implements Runnable{
	private List<Integer> runTimes;
	private List<Runnable> actions;
	private PerfResult[][] resultMatrix;
	
	public RunnableMatrix(){
		initMatrix();
	}
	
	public RunnableMatrix(List<Integer> runTimes, List<Runnable> actions){
		if(runTimes == null || actions == null){
			throw new RuntimeException(
					"use this constructor to init a PerfMatrix " +
					"must use not-null runTimes and actions");
		}
		this.runTimes = runTimes;
		setActions(actions);
	}
	
	public RunnableMatrix addRunTime(int runTime){
		runTimes.add(runTime);
		return this;
	}
	
	public RunnableMatrix addRunTimes(int... runTimes){
		for (int i = 0; i < runTimes.length; i++) {
	        this.runTimes.add(runTimes[i]);
        }
		return this;
	}

	public RunnableMatrix addAction(Runnable action){
		actions.add(wrapGroupIfNot(action));
		return this;
	}
	
	public RunnableMatrix addAction(String name, Runnable action){
		actions.add(wrapGroupIfNot(name, action));
		return this;
	}
	
	public RunnableMatrix addActions(Runnable... actions){
		for (int i = 0; i < actions.length; i++) {
	        this.actions.add(wrapGroupIfNot(actions[i]));
        }
		return this;
	}

	public void run() {
		initResultMatrix();
	    for (int i = 0; i < runTimes.size(); i++) {
            int currentRunTimes = runTimes.get(i);
	        for (int j = 0; j < actions.size(); j++) {
	        	RunnableGroup currentAction = (RunnableGroup) actions.get(j);
	        	currentAction.setRunTimes(currentRunTimes);
	        	currentAction.run();
	        	resultMatrix[i][j] = currentAction.getPerfResult();
            }
        }
    }
	
	/* private methods */
	
	private void initMatrix(){
		runTimes = new LinkedList<Integer>();
		actions = new LinkedList<Runnable>();
	}
	
	private void initResultMatrix(){
		resultMatrix = new PerfResult[runTimes.size()][actions.size()];
	}
	
	/* getter and setter */

	public List<Integer> getRunTimes() {
    	return runTimes;
    }

	public void setRunTimes(List<Integer> runTimes) {
    	this.runTimes = runTimes;
    }

	public List<Runnable> getActions() {
    	return actions;
    }

	public void setActions(List<Runnable> actions) {
		for (int i = 0; i < actions.size(); i++) {
	        actions.set(i, wrapGroupIfNot(actions.get(i)));
        }
    	this.actions = actions;
    }
	
	private Runnable wrapGroupIfNot(Runnable action){
		RunnableGroup group = null;
		if((action instanceof RunnableGroup) == false){
			group = new RunnableGroup();
			group.setSingleAction(action);
			action = group;
		}
		return action;
	}
	
	private Runnable wrapGroupIfNot(String name, Runnable action){
		RunnableGroup group = (RunnableGroup) wrapGroupIfNot(action);
		group.setName(name);
		return group;
	}

	public PerfResult[][] getResultMatrix() {
    	return resultMatrix;
    }

	public void setResultMatrix(PerfResult[][] resultMatrix) {
    	this.resultMatrix = resultMatrix;
    }
}
