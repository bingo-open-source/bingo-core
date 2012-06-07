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
import java.util.List;

import bingo.lang.Strings;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.lang.xml.XmlElement;

/**
 * Performance measurements utility for unit test.
 */
public final class Perf {

	private static final Log	log	= LogFactory.get(Perf.class);

	public enum OutputType {
		CONSOLE, HTML, ALL
	}

	public enum RunnableType {
		ITEM, LIST, MATRIX
	}

	public static final String	DEFAULT_PROJECT_NAME	= "anonymous";
	public static final String	DEFAULT_DIR_NAME	 = "perf";

	private String	           projectName	         = DEFAULT_PROJECT_NAME;
	private RunnableType	   runnableType	         = RunnableType.ITEM;
	private Runnable	       runnableThing;
	private OutputType	       outputType	         = OutputType.CONSOLE;
	private int 			   runTimeForSingle;

	private List<PerfResult>   resultList;

	public Perf() {
		this(DEFAULT_PROJECT_NAME, null, OutputType.CONSOLE);
	}

	public Perf(Runnable runnable) {
		this(DEFAULT_PROJECT_NAME, runnable, OutputType.CONSOLE);
	}

	public Perf(String projectName, Runnable runnable, OutputType outputType) {
		log.debug("execute construtor method with projectName [{}], runnable [{}], outputType [{}].",
				projectName, runnable, outputType);
		this.projectName = projectName;
		checkAndSetRunnableType(runnable);
		this.runnableThing = runnable;
		this.outputType = outputType;
	}

	public Perf run() {
		log.debug("Perf Project [{}] starts running...", projectName);
		if (null == runnableThing) {
			log.error("runnable thing is null when run() method is invoked.");
			throw new RuntimeException("the runnable thing could not be null.");
		}

		if (runnableType == RunnableType.LIST || runnableType == RunnableType.ITEM) {
			log.debug("Runnable Type is ITEM or LIST.");
			runList();
		}

		if (runnableType == RunnableType.MATRIX) {
			log.debug("Runnable Type is MATRIX.");
			runMatrix();
		}
		log.debug("Perf Project [{}] end running.\n\n", projectName);
		return this;
	}

	public static void run(String projectName, Runnable runnableThing, int runTimeForSingle) {
		log.debug("static run method is called.");
		Perf perf = new Perf(runnableThing);
		perf.setProjectName(projectName);
		perf.setRunTimeForSingle(runTimeForSingle);
		perf.run();
	}

	private void runList() {
		/* wrapped with runnable list */
		RunnableList runnableList = null;
		if (runnableThing instanceof RunnableList == false) {
			log.debug("Runnable Type is a simple runnable.");
			NamedRunnable item = new NamedRunnable(runnableThing);
			runnableList = new RunnableList().setGlobalRunTimes(runTimeForSingle).add(item);
		} else {
			runnableList = (RunnableList) runnableThing;
		}

		log.debug("Warming up...");
		runnableList.run();
		log.debug("End warming up and execute run() method inside the Runnable Thing.");
		runnableList.run();

		resultList = runnableList.getResultList();
		log.debug("get the performance result and output.");
		switch (outputType) {
			case CONSOLE:
				listToConsole();
				break;
			case HTML:
				listToHtml();
				break;
			case ALL:
				listToConsole();
				listToHtml();
				break;
			default:
				log.error("invalid enum value of OutputType: [{}].", outputType);
		}
	}

	private void runMatrix() {
		RunnableMatrix matrix = (RunnableMatrix) runnableThing;

		log.debug("Warming up...");
		matrix.run();
		log.debug("End warming up and execute run() method inside the Runnable Thing.");
		matrix.run();

		PerfResult[][] resultMatrix = matrix.getResultMatrix();
		log.debug("get the performance result and output.");
		switch (outputType) {
			case CONSOLE:
				matrixToConsole(resultMatrix);
				break;
			case HTML:
				matrixToHtml(resultMatrix);
				break;
			case ALL:
				matrixToConsole(resultMatrix);
				matrixToHtml(resultMatrix);
				break;
			default:
				log.error("invalid enum value of OutputType: [{}].", outputType);
		}
	}

	private void matrixToHtml(PerfResult[][] resultMatrix) {
		XmlElement doc = PerfHtmlBuilder.buildUpMatrixHtml(projectName, resultMatrix);
		writeToHtml(doc);
	}

	private void matrixToConsole(PerfResult[][] resultMatrix) {
		System.out.println("------------------------------------------------");
		String temp = null;
		for (int i = -1; i < resultMatrix.length; i++) {
			if (-1 == i) {
				temp = "RunTimes\\Code";
				System.out.print(temp + getTabByLength(temp.length()));
				for (int k = 0; k < resultMatrix[0].length; k++) {
					temp = "[" + resultMatrix[0][k].getName() + "]";
					System.out.print(temp + getTabByLength(temp.length()));
				}
				System.out.println();
				continue;
			} else {
				temp = "[" + resultMatrix[i][0].getRunTimes() + "]";
				System.out.print(temp + getTabByLength(temp.length()));
			}
			
			for (int j = 0; j < resultMatrix[i].length; j++) {
				temp = resultMatrix[i][j].getElapsedNanoseconds() + "";
				System.out.print(temp + getTabByLength(temp.length()));
			}
			System.out.println();
		}
		System.out.println("------------------------------------------------");
	}
	
	private String getTabByLength(int length){
		if(length < 8) return "\t\t\t";
		if(length < 16) return "\t\t";
		if(length < 24) return "\t";
		log.warn("the length of runnable name is too long to display well: [{}].", length);
		return "";
	}

	private void listToConsole() {
		log.debug("starting to output result to console...");
		System.out.println("------------------------------------------------");
		System.out.println(Strings.format("Performance Project [{0}]",
								projectName));
		for (PerfResult result : resultList) {
	        System.out.println("    " + result.toString());
        }
		System.out.println("------------------------------------------------");
		log.debug("finished to output result to console.");
	}
	
	private void listToHtml() {
		XmlElement doc = PerfHtmlBuilder.buildUpGroupHtml(projectName, resultList);
		writeToHtml(doc);
	}

	private void checkAndSetRunnableType(Runnable runnable) {
		if (runnable instanceof RunnableMatrix) {
			this.runnableType = RunnableType.MATRIX;
			log.debug("set runnable type is MATRIX");
			return;
		}
		if (runnable instanceof RunnableList) {
			this.runnableType = RunnableType.LIST;
			log.debug("set runnable type is LIST");
			return;
		}
		this.runnableType = RunnableType.ITEM;
		log.debug("set runnable type is ITEM");
	}

	private void writeToHtml(XmlElement ele) {
		FileWriter writer = null;
		try {
			File dir = new File(DEFAULT_DIR_NAME);
			dir.mkdir();
			File file = new File(dir, projectName + ".html");
			file.createNewFile();
			writer = new FileWriter(file);
			writer.write(ele.toString());
			log.info("successfully generated HTML file for project [{}].", projectName);
			log.info("file location is [{}].", file.getAbsolutePath());
		} catch (IOException e) {
			log.error("error when generating HTML file for project [{}].", projectName);
			throw new RuntimeException("error when generating HTML file");
		} finally {
			if (null != writer) {
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

	public Perf setProjectName(String projectName) {
		this.projectName = projectName;
		log.debug("set project name as [{}]", projectName);
		return this;
	}

	public Runnable getRunnableThing() {
		return runnableThing;
	}

	public Perf setRunnableThing(Runnable runnableThing) {
		this.runnableThing = runnableThing;
		log.debug("set runnable thing as [{}]", runnableThing);
		return this;
	}

	public OutputType getOutputType() {
		return outputType;
	}

	public Perf setOutputType(OutputType outputType) {
		this.outputType = outputType;
		log.debug("set output type as [{}]", outputType);
		return this;
	}

	public int getRunTimeForSingle() {
    	return runTimeForSingle;
    }

	public void setRunTimeForSingle(int runTimeForSingle) {
    	this.runTimeForSingle = runTimeForSingle;
    }

}
