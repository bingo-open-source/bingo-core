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

public class EdmSchemaBuilder extends EdmBuilderWithDocumentation implements Builder<EdmSchema> {
	
	protected String namespace;
	
	protected String alias;
	
	protected List<EdmEntityContainer> entityContainers = new ArrayList<EdmEntityContainer>();
	
	protected List<EdmEntityType> entityTypes = new ArrayList<EdmEntityType>();
	
	protected List<EdmAssociation> associations = new ArrayList<EdmAssociation>();
	
	protected List<EdmComplexType> complexTypes = new ArrayList<EdmComplexType>();
	
	protected List<EdmEnumType> enumTypes = new ArrayList<EdmEnumType>();
	
	protected List<EdmFunction> functions = new ArrayList<EdmFunction>();
	
	public EdmSchemaBuilder(){
		
	}
	
	public EdmSchemaBuilder(String namespace,String alias){
		this.namespace = namespace;
		this.alias     = alias;
	}
	
	public String getNamespace() {
    	return namespace;
    }

	public String getAlias() {
    	return alias;
    }

	public EdmSchemaBuilder setNamespace(String namespace) {
    	this.namespace = namespace;
    	return this;
    }

	public EdmSchemaBuilder setAlias(String alias) {
    	this.alias = alias;
    	return this;
    }
	
	public EdmSchemaBuilder addEntityType(EdmEntityType entityType) {
		entityTypes.add(entityType);
		return this;
	}
	
	public EdmSchemaBuilder addEntityTypes(EdmEntityType... entityTypes) {
		for(EdmEntityType type : entityTypes){
			addEntityType(type);
		}
		return this;
	}
	
	public EdmSchemaBuilder addComplexType(EdmComplexType complexType) {
		complexTypes.add(complexType);
		return this;
	}
	
	public EdmSchemaBuilder addComplexTypes(EdmComplexType... complexTypes) {
		for(EdmComplexType type : complexTypes){
			addComplexType(type);
		}
		return this;
	}
	
	public EdmSchemaBuilder addEnumType(EdmEnumType enumType){
		enumTypes.add(enumType);
		return this;
	}
	
	public EdmSchemaBuilder addEnumTypes(EdmEnumType... enumTypes){
		for(EdmEnumType enumType : enumTypes){
			addEnumType(enumType);
		}
		return this;
	}
	
	public EdmSchemaBuilder addAssociation(EdmAssociation association) {
		associations.add(association);
		return this;
	}
	
	public EdmSchemaBuilder addAssociations(EdmAssociation... associations) {
		for(EdmAssociation assoc : associations){
			addAssociation(assoc);
		}
		return this;
	}
	
	public EdmSchemaBuilder addEntityContainer(EdmEntityContainer container){
		entityContainers.add(container);
		return this;
	}
	
	public EdmSchemaBuilder addEntityContainer(EdmEntityContainer... containers){
		for(EdmEntityContainer container : containers){
			addEntityContainer(container);
		}
		return this;
	}
	
	@Override
    public EdmSchemaBuilder setDocumentation(EdmDocumentation documentation) {
	    super.setDocumentation(documentation);
	    return this;
    }

	@Override
    public EdmSchemaBuilder setDocumentation(String summary, String longDescription) {
	    super.setDocumentation(summary, longDescription);
	    return this;
    }
	
	@Override
    public EdmSchemaBuilder setDocumentation(String title, String summary, String longDescription) {
	    super.setDocumentation(title, summary, longDescription);
	    return this;
    }

	public EdmSchema build() {
	    return new EdmSchema(namespace, alias, entityContainers, entityTypes, associations, complexTypes, enumTypes, functions,documentation);
    }
}