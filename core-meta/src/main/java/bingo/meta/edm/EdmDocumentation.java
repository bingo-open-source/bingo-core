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
package bingo.meta.edm;

public class EdmDocumentation extends EdmObject {
	
	//custom attribute ,not defined in edm
	private final String title;

	private final String summary;
	
	private final String longDescription;
	
	public EdmDocumentation(String summary,String longDescription){
		this(null,summary,longDescription);
	}
	
	public EdmDocumentation(String title, String summary,String longDescription){
		this.title   = title;
		this.summary = summary;
		this.longDescription = longDescription;
	}
	
	public String getTitle() {
		return title;
	}

	public String getSummary() {
    	return summary;
    }

	public String getLongDescription() {
    	return longDescription;
    }
}