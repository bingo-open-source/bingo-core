package bingo.lang.testing;

import java.util.LinkedList;
import java.util.List;

import bingo.lang.Strings;

public class PerfResult {
	private List<PerfResult> children;
	private boolean isEnd = false;

	private String name;
	private int runTimes;
	private long elapsedNanoseconds;
	private long elapsedMilliseconds;
	
	public PerfResult(){
	}
	
	public PerfResult(long elapsedNanoseconds, long elapsedMilliseconds) {
		this.elapsedNanoseconds = elapsedNanoseconds;
		this.elapsedMilliseconds = elapsedMilliseconds;
	}

	public void addChild(PerfResult child){
		if(null == children){
			children = new LinkedList<PerfResult>();
		}
		children.add(child);
	}
	
	public String toString(){
		return Strings.format("[{0}] -> run {3} times duration : {1}ms, {2}ns",
				getName(), getElapsedMilliseconds(), getElapsedNanoseconds(),
				getRunTimes());
	}

	public List<PerfResult> getChildren() {
		return children;
	}

	public void setChildren(List<PerfResult> children) {
		this.children = children;
	}

	public long getElapsedNanoseconds() {
		return elapsedNanoseconds;
	}

	public void setElapsedNanoseconds(long elapsedNanoseconds) {
		this.elapsedNanoseconds = elapsedNanoseconds;
	}

	public long getElapsedMilliseconds() {
		return elapsedMilliseconds;
	}

	public void setElapsedMilliseconds(long elapsedMilliseconds) {
		this.elapsedMilliseconds = elapsedMilliseconds;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRunTimes() {
		return runTimes;
	}

	public void setRunTimes(int runTimes) {
		this.runTimes = runTimes;
	}
	
}