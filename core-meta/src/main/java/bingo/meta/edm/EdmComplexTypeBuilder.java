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

public class EdmComplexTypeBuilder extends EdmNamedStructualTypeBuilder implements Builder<EdmComplexType> {

	public EdmComplexTypeBuilder() {
	    super();
    }

	public EdmComplexTypeBuilder(String name) {
	    super(name);
    }

	@Override
    public EdmComplexTypeBuilder setAbstract(boolean isAbstract) {
	    super.setAbstract(isAbstract);
	    return this;
    }

	@Override
    public EdmComplexTypeBuilder setName(String name) {
	    super.setName(name);
	    return this;
    }
	
	@Override
    public EdmComplexTypeBuilder addProperty(EdmProperty property) {
	    super.addProperty(property);
	    return this;
    }

	@Override
    public EdmComplexTypeBuilder addProperty(String name, EdmType type, boolean nullable) {
	    super.addProperty(name, type, nullable);
	    return this;
    }

	@Override
    public EdmComplexTypeBuilder setDocumentation(EdmDocumentation documentation) {
	    super.setDocumentation(documentation);
	    return this;
    }

	@Override
    public EdmComplexTypeBuilder setDocumentation(String summary, String longDescription) {
	    super.setDocumentation(summary, longDescription);
	    return this;
    }
	
	@Override
    public EdmComplexTypeBuilder setDocumentation(String title, String summary, String longDescription) {
	    super.setDocumentation(title, summary, longDescription);
	    return this;
    }

	public EdmComplexType build() {
	    return new EdmComplexType(name, properties, isAbstract);
    }
}