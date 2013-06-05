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
import bingo.meta.edm.EdmFeedCustomization.SyndicationItemProperty;
import bingo.meta.edm.EdmFeedCustomization.SyndicationTextContentKind;

public class EdmEntityTypeBuilder extends EdmNamedStructualTypeBuilder implements Builder<EdmEntityType> {
	
	private String fullQualifiedName;

	private EdmEntityType baseType;

	private boolean hasStream;
	
	private boolean openType;

	private List<String> keys = new ArrayList<String>();

	private List<EdmNavigationProperty> navigationProperties = new ArrayList<EdmNavigationProperty>();
	
	public EdmEntityTypeBuilder(){

	}	
	
	public EdmEntityTypeBuilder(String name,String fullQualifiedName){
		this.name = name;
		this.fullQualifiedName = fullQualifiedName;
	}
	
	@Override
	public EdmEntityTypeBuilder setAbstract(boolean isAbstract) {
		super.setAbstract(isAbstract);
		return this;
	}

	@Override
	public EdmEntityTypeBuilder setName(String name) {
		super.setName(name);
		return this;
	}
	
	public EdmEntityTypeBuilder setBaseType(EdmEntityType baseType){
		this.baseType = baseType;
		return this;
	}
	
	public EdmEntityTypeBuilder setHasStream(boolean hasStream){
		this.hasStream = hasStream;
		return this;
	}
	
	public EdmEntityTypeBuilder setOpenType(boolean openType) {
		this.openType = openType;
		return this;
	}

	public EdmEntityTypeBuilder addKey(String key){
//		if(!Enumerables.any(properties, Predicates.<EdmProperty>nameEqualsIgnoreCase(key))) {
//			throw new EdmException("no key property '{0}' found in entity type",key);
//		}
//		
//		if(keys.contains(key)){
//			throw new EdmException("key '{0}' aleady exists in entity type",key);
//		}
		
		keys.add(key);
		
		return this;
	}
	
	public EdmEntityTypeBuilder addKeys(String... keys){
		for(String key : keys){
			addKey(key);
		}
		return this;
	}
	
	public EdmEntityTypeBuilder addKeys(Iterable<String> keys){
		for(String key : keys){
			addKey(key);
		}
		return this;
	}
	
	public Enumerable<EdmNavigationProperty> getNavigationProperties(){
		return Enumerables.of(navigationProperties);
	}
	
	public EdmEntityTypeBuilder addNavigationProperty(EdmNavigationProperty property) {
		navigationProperties.add(property);
		return this;
	}
	
	public EdmEntityTypeBuilder addNavigationProperty(String name,EdmAssociation relationship,EdmAssociationEnd fromRole,EdmAssociationEnd toRole) {
		navigationProperties.add(new EdmNavigationProperty(name, relationship, fromRole, toRole));
		return this;
	}
	
	@Override
    public EdmEntityTypeBuilder addProperty(String name, EdmType type, boolean nullable) {
	    super.addProperty(name, type, nullable);
	    return this;
    }
	
    public EdmEntityTypeBuilder addPropertyForSyndicationTitle(String name, EdmType type, boolean nullable) {
	    return addProperty(name,type,nullable,SyndicationItemProperty.Title);
    }
    
    public EdmEntityTypeBuilder addPropertyForSyndicationTitle(String name, EdmType type, boolean nullable,boolean fcKeepInContent) {
	    return addProperty(name,type,nullable,SyndicationItemProperty.Title,SyndicationTextContentKind.Text,fcKeepInContent);
    }
    
    public EdmEntityTypeBuilder addPropertyForSyndicationSummary(String name, EdmType type, boolean nullable) {
	    return addProperty(name,type,nullable,SyndicationItemProperty.Summary);
    }
	
    public EdmEntityTypeBuilder addProperty(String name, EdmType type, boolean nullable,SyndicationItemProperty fcTargetPath) {
    	return addProperty(name, type, nullable, fcTargetPath, SyndicationTextContentKind.Text);
    }
    
    public EdmEntityTypeBuilder addProperty(String name, EdmType type, boolean nullable,SyndicationItemProperty fcTargetPath,SyndicationTextContentKind fcContentKind) {
	    return addProperty(name, type, nullable, fcTargetPath, fcContentKind, false);
    }
    
    public EdmEntityTypeBuilder addProperty(String name, EdmType type, boolean nullable,SyndicationItemProperty fcTargetPath,SyndicationTextContentKind fcContentKind,boolean fcKeepInContent) {
	    super.addProperty(new EdmPropertyBuilder(name, type, nullable)
	    							.setFcTargetPath(fcTargetPath)
	    							.setFcContentKind(fcContentKind)
	    							.setFcKeepInContent(fcKeepInContent)
	    							.build());
	    return this;
    }
	
    public EdmEntityTypeBuilder addKeyProperty(String name, EdmType type) {
	    super.addProperty(name, type, false);
	    return addKey(name);
    }

	@Override
	public EdmEntityTypeBuilder addProperty(EdmProperty property) {
		super.addProperty(property);
		return this;		
	}
	
	public EdmEntityTypeBuilder addKeyProperty(EdmProperty property) {
		super.addProperty(property);
		return addKey(property.getName());		
	}

	@Override
	public EdmEntityTypeBuilder setDocumentation(EdmDocumentation documentation) {
		super.setDocumentation(documentation);
		return this;		
	}

	@Override
	public EdmEntityTypeBuilder setDocumentation(String summary, String longDescription) {
		super.setDocumentation(summary, longDescription);
		return this;
	}
	
	@Override
    public EdmEntityTypeBuilder setDocumentation(String title, String summary, String longDescription) {
	    super.setDocumentation(title, summary, longDescription);
	    return this;
    }

	public EdmEntityType build() {
		return new EdmEntityType(name, fullQualifiedName, properties, navigationProperties, keys, isAbstract, hasStream, openType, baseType,documentation);
	}
	
	public EdmEntityTypeRef buildRef(EdmSchemaBuilder schema){
		return EdmEntityTypeRef.of(schema, this);
	}
}