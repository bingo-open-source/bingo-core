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

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Builder;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;

public class EdmFunctionImportBuilder extends EdmNamedBuilder implements Builder<EdmFunctionImport> {
	
	protected String entitySet;
	
	protected EdmType returnType;
	
	protected String httpMethod;
	
	protected boolean sideEffecting = true;
	
	protected List<EdmParameter> parameters = new ArrayList<EdmParameter>();
	
	public EdmFunctionImportBuilder() {
	    super();
    }
	
	public EdmFunctionImportBuilder(String name) {
	    super(name);
    }
	
	public Enumerable<EdmParameter> getParameters() {
    	return Enumerables.of(parameters);
    }
	
	public String getEntitySet() {
    	return entitySet;
    }

	public EdmFunctionImportBuilder setEntitySet(String entitySet) {
    	this.entitySet = entitySet;
    	return this;
    }
	
	public EdmFunctionImportBuilder setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}

	public EdmFunctionImportBuilder setSideEffecting(boolean sideEffecting) {
		this.sideEffecting = sideEffecting;
		return this;
	}

	public EdmType getReturnType() {
    	return returnType;
    }

	public EdmFunctionImportBuilder setReturnType(EdmType returnType) {
    	this.returnType = returnType;
    	return this;
    }

	public EdmFunctionImportBuilder addParameter(EdmParameter parameter){
		parameters.add(parameter);
		return this;
	}
	
	public EdmFunctionImportBuilder addParameter(String name,EdmType type,EdmParameterMode mode){
		parameters.add(new EdmParameter(name, type, mode));
		return this;
	}
	
	public EdmFunctionImportBuilder addInParameter(String name,EdmType type){
		parameters.add(new EdmParameter(name, type, EdmParameterMode.In));
		return this;
	}
	
	public EdmFunctionImportBuilder addOutParameter(String name,EdmType type){
		parameters.add(new EdmParameter(name, type, EdmParameterMode.Out));
		return this;
	}
	
	@Override
    public EdmFunctionImportBuilder setDocumentation(EdmDocumentation documentation) {
	    super.setDocumentation(documentation);
	    return this;
    }

	@Override
    public EdmFunctionImportBuilder setDocumentation(String summary, String longDescription) {
	    super.setDocumentation(summary, longDescription);
	    return this;
    }
	
	@Override
    public EdmFunctionImportBuilder setDocumentation(String title, String summary, String longDescription) {
	    super.setDocumentation(title, summary, longDescription);
	    return this;
    }

	public EdmFunctionImport build() {
	    return new EdmFunctionImport(name, entitySet, returnType, parameters,httpMethod,sideEffecting,documentation);
    }
}