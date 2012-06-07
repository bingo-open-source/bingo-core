package bingo.lang.testing;

import java.util.LinkedList;
import java.util.List;

import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

public class RunnableMatrix implements Runnable{
	
	private static final Log log = LogFactory.get(RunnableMatrix.class);
	
	private List<Integer> runTimeRows;
	private RunnableList runnableColumns;
	private PerfResult[][] resultMatrix;
	
	public RunnableMatrix(){
		this(new LinkedList<Integer>(), new RunnableList());
	}
	
	public RunnableMatrix(List<Integer> runTimeRows, RunnableList runnableColumns){
		if(runTimeRows == null || runnableColumns == null){
			log.error("run time rows or runnable columns is null when constucting RunnableMatrix.");
			throw new RuntimeException(
					"use this constructor to init a PerfMatrix " +
					"must use not-null runTimes and actions");
		}
		this.runTimeRows = runTimeRows;
		this.runnableColumns = runnableColumns;
	}
	
	public RunnableMatrix addRunTimeRow(int runTime){
		runTimeRows.add(runTime);
		log.debug("add run time row as [{}]", runTime);
		return this;
	}
	
	public RunnableMatrix addRunTimeRows(int... runTimes){
		for (int i = 0; i < runTimes.length; i++) {
			addRunTimeRow(runTimes[i]);
        }
		return this;
	}
	
    public RunnableMatrix setDefaultRunTimeFrom1to1000000(){
		runTimeRows = new LinkedList<Integer>();
		addRunTimeRows(1, 10, 100, 1000, 10000, 100000, 1000000);
		return this;
	}

	public RunnableMatrix addRunnableColumn(NamedRunnable item){
		runnableColumns.add(item);
		log.debug("add runnable column named [{}]", item.getName());
		return this;
	}
	
	public RunnableMatrix addRunnableColumn(String name, Runnable runnable){
		addRunnableColumn(new NamedRunnable(name, runnable));
		return this;
	}
	
	public RunnableMatrix addRunnbaleColumns(NamedRunnable... items){
		for (int i = 0; i < items.length; i++) {
			addRunnableColumn(items[i]);
        }
		return this;
	}

	public void run() {
		resultMatrix = new PerfResult[runTimeRows.size()][runnableColumns.getList().size()];
		log.debug("set up a [{}][{}] result matrix", runTimeRows.size(), runnableColumns.getList().size());
		
	    for (int i = 0; i < runTimeRows.size(); i++) {
            int runTime = runTimeRows.get(i);
            runnableColumns.setAllRunTimes(runTime);
            runnableColumns.run();
	        for (int j = 0; j < runnableColumns.getResultList().size(); j++) {
	        	resultMatrix[i][j] = runnableColumns.getResultList().get(j);
            }
        }
    }
	
	/* getter and setter */

	public List<Integer> getRunTimes() {
    	return runTimeRows;
    }

	public void setRunTimes(List<Integer> runTimes) {
    	this.runTimeRows = runTimes;
    }

	public RunnableList getRunnableColumns() {
    	return runnableColumns;
    }

	public void setRunnableColumns(RunnableList runnableColumns) {
    	this.runnableColumns = runnableColumns;
    }

	public PerfResult[][] getResultMatrix() {
    	return resultMatrix;
    }

	public void setResultMatrix(PerfResult[][] resultMatrix) {
    	this.resultMatrix = resultMatrix;
    }
}
