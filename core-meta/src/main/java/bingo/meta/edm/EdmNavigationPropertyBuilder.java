/*
 * Copyright 2013 the original author or authors.
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

public class EdmNavigationPropertyBuilder extends EdmNamedBuilder implements Builder<EdmNavigationProperty>{

	private EdmAssociation 	  relationship;
	private EdmAssociationEnd fromRole;
	private EdmAssociationEnd toRole;
	
	public EdmNavigationPropertyBuilder() {
	    super();
    }

	public EdmNavigationPropertyBuilder(String name) {
	    super(name);
    }
	
	public EdmNavigationPropertyBuilder setRelationship(EdmAssociation relationship) {
		this.relationship = relationship;
		return this;
	}

	public EdmNavigationPropertyBuilder setFromRole(EdmAssociationEnd fromRole) {
		this.fromRole = fromRole;
		return this;
	}

	public EdmNavigationPropertyBuilder setToRole(EdmAssociationEnd toRole) {
		this.toRole = toRole;
		return this;
	}

	public EdmAssociation getRelationship() {
		return relationship;
	}

	public EdmAssociationEnd getFromRole() {
		return fromRole;
	}

	public EdmAssociationEnd getToRole() {
		return toRole;
	}
	
	@Override
    public EdmNavigationPropertyBuilder setDocumentation(EdmDocumentation documentation) {
	    super.setDocumentation(documentation);
	    return this;
    }

	@Override
    public EdmNavigationPropertyBuilder setDocumentation(String summary, String longDescription) {
	    super.setDocumentation(summary, longDescription);
	    return this;
    }
	
	@Override
    public EdmNavigationPropertyBuilder setDocumentation(String title, String summary, String longDescription) {
	    super.setDocumentation(title, summary, longDescription);
	    return this;
    }

	public EdmNavigationProperty build() {
	    return new EdmNavigationProperty(name, relationship, fromRole, toRole,documentation);
    }
}
