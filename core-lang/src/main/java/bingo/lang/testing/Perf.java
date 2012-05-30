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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bingo.lang.xml.XmlElement;

/**
 * Performance measurements utility for unit test.
 */
public final class Perf {
	
	public static final int TO_CONSOLE = 0;
	public static final int TO_HTML = 1;
	public static final int TO_CONSOLE_AND_HTML = 2;
	
	public static final String DEFAULT_PROJECT_NAME = "anonymous";
	public static final String DEFAULT_FILE_NAME = "testing.html";
	public static final String DEFAULT_DIR_NAME = "perf";
	
	private String projectName = DEFAULT_PROJECT_NAME;
	private Runnable runnableThing;
	private int showResultTo = TO_CONSOLE;
	private String fileName;
	
	private PerfResult perfResult; 
	
	private int runnableType = IS_SIMPLE_RUNNABLE;
	public static final int IS_SIMPLE_RUNNABLE = 0;
	public static final int IS_RUNNABLE_GROUP = 1;
	public static final int IS_RUNNABLE_MATRIX = 2;

	public Perf(){
		this(DEFAULT_PROJECT_NAME, null, TO_CONSOLE, null);
	}
	
	public Perf(Runnable runnable){
		this(DEFAULT_PROJECT_NAME, runnable, TO_CONSOLE, null);
	}
	
	public Perf(String projectName, Runnable runnable, int showResultTo, String fileName){
		this.projectName = projectName;
		checkAndSetRunnableType(runnable);
		this.runnableThing = runnable;
		this.showResultTo = showResultTo;
		this.fileName = fileName;
	}
	
	public void run(){
		if(null == runnableThing){
			throw new RuntimeException("the runnable thing could not be null.");
		}
		if(runnableType == IS_RUNNABLE_GROUP || runnableType == IS_SIMPLE_RUNNABLE){
			runGroup();
		}
		if(runnableType == IS_RUNNABLE_MATRIX){
			runMatrix();
		}
	}
	
	public static void run(String name, Runnable action, int runTimes){
		Perf perf = new Perf(action);
		perf.setFileName(name);
		perf.run();
	}
	
    /**
     * run a runnable group.
     */    
    private void runGroup(){
    	/* wrapped with runnable group */
    	String name = RunnableGroup.tryToGetName(runnableThing);
    	int times = RunnableGroup.tryToGetRunTimes(runnableThing);
    	
    	RunnableGroup runnableGroup = new RunnableGroup(name, times, runnableThing);
    	
    	/* warm up */
    	runnableGroup.run();
    	
    	runnableGroup.run();
    	
    	perfResult = runnableGroup.getPerfResult();
    	
    	if(showResultTo == TO_CONSOLE){
        	groupToConsole();	
    	}
    	
    	if(showResultTo == TO_HTML){
    		groupToHtml();
    	}
    	
    	if(showResultTo == TO_CONSOLE_AND_HTML){
        	groupToConsole();	
    		groupToHtml();
    	}
    }
    
    private void runMatrix(){
    	RunnableMatrix matrix = (RunnableMatrix) runnableThing;
    	
    	matrix.run();
    	
    	PerfResult[][] resultMatrix = matrix.getResultMatrix();
    	
    	if(showResultTo == TO_CONSOLE){
        	matrixToConsole(resultMatrix);	
    	}
    	
    	if(showResultTo == TO_HTML){
    		matrixToHtml(resultMatrix);
    	}
    	
    	if(showResultTo == TO_CONSOLE_AND_HTML){
        	matrixToConsole(resultMatrix);	
        	matrixToHtml(resultMatrix);
    	}
    }
    
    private void matrixToHtml(PerfResult[][] resultMatrix) {
	    XmlElement doc = PerfHtmlBuilder.buildUpMatrixHtml(projectName, resultMatrix);
	    writeToHtml(doc);
    }



	private void matrixToConsole(PerfResult[][] resultMatrix) {
		System.out.println("------------------------------------------------");
	   for (int i = -1; i < resultMatrix.length; i++) {
		   if(-1 == i){
			   System.out.print("\t");
			   for (int k = 0; k < resultMatrix[0].length; k++) {
				   System.out.print(resultMatrix[0][k].getName() + "\t");
			   }
			   System.out.println();
			   continue;
		   } else {
			   System.out.print(resultMatrix[i][0].getRunTimes() + "\t");
		   }
		   for (int j = 0; j < resultMatrix[i].length; j++) {
			   System.out.print(resultMatrix[i][j].getElapsedNanoseconds() + "\t\t");
		   }
		   System.out.println();
	   }
    }

	/**
     * show the specified named {@link PerfResult} to console.
     * @param projectName name of this {@link PerfResult}.
     * @param perfResult the specified {@link PerfResult}.
     */
    public void groupToConsole(){
    	System.out.println("PROJECT: " + projectName);
    	toConsole(perfResult.getChildren().get(0), "");
    }
    
    public void groupToHtml(){
    	XmlElement doc = PerfHtmlBuilder.buildUpGroupHtml(projectName, perfResult);
	    writeToHtml(doc);
    }
    
    private void checkAndSetRunnableType(Runnable runnable){
    	if(runnable instanceof RunnableMatrix){
    		this.runnableType = IS_RUNNABLE_MATRIX;
    		return;
    	}
    	if(runnable instanceof RunnableGroup){
    		this.runnableType = IS_RUNNABLE_GROUP;
    		return;
    	}
    	this.runnableType = IS_SIMPLE_RUNNABLE;
    }
    
    private void toConsole(PerfResult perfResult, String prefix){
    	System.out.println(prefix + perfResult.toString());
    	prefix += "  ";
    	if(perfResult.isEnd() == false){
        	for (PerfResult perfResultChild : perfResult.getChildren()) {
    			toConsole(perfResultChild, prefix);
    		}
    	}
    }
    
    private void writeToHtml(XmlElement ele){
    	FileWriter writer = null;
    	try{
        	File dir = new File(DEFAULT_DIR_NAME);
        	File file = null;
        	dir.mkdir();
        	if(DEFAULT_PROJECT_NAME.equals(projectName)){
        		file = new File(dir, DEFAULT_FILE_NAME);
        	} else {
        		file = new File(dir, projectName + ".html");
        	}
        	file.createNewFile();
        	writer = new FileWriter(file);
        	writer.write(ele.toString());
		} catch (IOException e) {
	        throw new RuntimeException("error when generating HTML file");
        } finally {
			if(null != writer){
				try {
	                writer.close();
                } catch (IOException e) {
	                throw new RuntimeException("error when closing HTML file.");
                }
			}
		}
    }
    
    /* getter and setter */

	public String getProjectName() {
    	return projectName;
    }

	public void setProjectName(String projectName) {
    	this.projectName = projectName;
    }

	public Runnable getRunnableThing() {
    	return runnableThing;
    }

	public void setRunnableThing(Runnable runnableThing) {
    	this.runnableThing = runnableThing;
    }

	public int getShowResultTo() {
    	return showResultTo;
    }

	public void setShowResultTo(int showResultTo) {
    	this.showResultTo = showResultTo;
    }

	public String getFileName() {
    	return fileName;
    }

	public void setFileName(String fileName) {
    	this.fileName = fileName;
    }

	public int getRunnableType() {
    	return runnableType;
    }

	public void setRunnableType(int runnableType) {
    	this.runnableType = runnableType;
    }
    
}
