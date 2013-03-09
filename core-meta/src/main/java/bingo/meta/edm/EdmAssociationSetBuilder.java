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

import bingo.lang.Builder;

public class EdmAssociationSetBuilder extends EdmNamedBuilder implements Builder<EdmAssociationSet> {
	
	protected EdmAssociation association;
	
	protected EdmAssociationSetEnd end1;
	
	protected EdmAssociationSetEnd end2;
	
	public EdmAssociationSetBuilder() {
		super();
    }
	
	public EdmAssociationSetBuilder(String name) {
		super();
		this.name = name;
    }
	
	public EdmAssociation getAssociation() {
    	return association;
    }

	public EdmAssociationSetBuilder setAssociation(EdmAssociation association) {
    	this.association = association;
    	return this;
    }

	public EdmAssociationSetEnd getEnd1() {
    	return end1;
    }

	public EdmAssociationSetBuilder setEnd1(EdmAssociationSetEnd end1) {
    	this.end1 = end1;
    	return this;
    }

	public EdmAssociationSetEnd getEnd2() {
    	return end2;
    }

	public EdmAssociationSetBuilder setEnd2(EdmAssociationSetEnd end2) {
    	this.end2 = end2;
    	return this;
    }
	
	public EdmAssociationSetBuilder setEnd1(String role,String entitySet) {
    	this.end1 = new EdmAssociationSetEnd(role, entitySet);
    	return this;
    }

	public EdmAssociationSetBuilder setEnd2(String role,String entitySet) {
    	this.end2 = new EdmAssociationSetEnd(role, entitySet);
    	return this;
    }
	
	@Override
    public EdmAssociationSetBuilder setDocumentation(EdmDocumentation documentation) {
	    super.setDocumentation(documentation);
	    return this;
    }

	@Override
    public EdmAssociationSetBuilder setDocumentation(String summary, String longDescription) {
	    super.setDocumentation(summary, longDescription);
	    return this;
    }
	
	@Override
    public EdmAssociationSetBuilder setDocumentation(String title, String summary, String longDescription) {
	    super.setDocumentation(title, summary, longDescription);
	    return this;
    }

	public EdmAssociationSet build() {
	    return  new EdmAssociationSet(name, association, end1, end2, documentation);
    }
}
