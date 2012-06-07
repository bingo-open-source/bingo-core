package bingo.lang.testing;

import bingo.lang.Named;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

public class NamedRunnable implements Named, Runnable {
	
	private static final Log log = LogFactory.get(NamedRunnable.class);
	
	private String name;
	
	private Runnable runnable;
	
	private Integer runTimes;
	
	public NamedRunnable(String name){
		this.name = name;
	    log.debug("call constructor of NamedRunnable with name [{}].", name);
	}
	
	public NamedRunnable(Runnable runnable){
		this.runnable = runnable;
	    log.debug("call constructor of NamedRunnable with runnable [{}].", runnable);
	}
	
	public NamedRunnable(String name, Runnable runnable){
		this.name = name;
		this.runnable = runnable;
	    log.debug("call constructor of NamedRunnable with name [{}], runnable [{}].", 
    			name, runnable);
	}

	public NamedRunnable(String name, Integer runTimes, Runnable runnable) {
	    this.name = name;
	    this.runnable = runnable;
	    this.runTimes = runTimes;
	    log.debug("call constructor of NamedRunnable with name [{}], runTimes [{}], runnable [{}].", 
	    			name, runTimes, runnable);
    }

	public void run() {
		runnable.run();
	}

	public String getName() {
		return name;
	}
	
	public NamedRunnable setName(String name){
		this.name = name;
		return this;
	}

	public Integer getRunTimes() {
    	return runTimes;
    }

	public NamedRunnable setRunTimes(Integer runTimes) {
    	this.runTimes = runTimes;
    	return this;
    }

	public Runnable getRunnable() {
    	return runnable;
    }

	public NamedRunnable setRunnable(Runnable runnable) {
    	this.runnable = runnable;
    	return this;
    }

}
