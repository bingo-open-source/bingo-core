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
package bingo.lang.plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import bingo.lang.Named;

public class Plugin implements Named{

	private String   			 name;
	private Object 			 	 bean;
	private String   			 summary;
	private String   			 description;
	private Map<String, String> properties = new LinkedHashMap<String, String>();
	
	protected Plugin(){
		
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean() {
		return (T)bean;
	}
	
	public String getSummary() {
    	return summary;
    }

	public String getDescription() {
    	return description;
    }
	
	public String getProperty(String name){
		return properties.get(name);
	}
	
	public Map<String, String> getProperties(){
		return new HashMap<String, String>(properties);
	}
	
	protected void setName(String name) {
    	this.name = name;
    }
	
	protected void setBean(Object instance) {
		this.bean = instance;
	}
	
	public void setSummary(String summary) {
    	this.summary = summary;
    }

	public void setDescription(String description) {
    	this.description = description;
    }
	
	protected void setProperty(String name,String value){
		properties.put(name, value);
	}

	protected void load() throws Throwable {

	}

	protected void unload() throws Throwable {

	}
}