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

public class EdmParameterBuilder extends EdmBuilderWithDocumentation implements Builder<EdmParameter> {
	
	protected String           name;
	protected EdmParameterMode mode;
	protected EdmType          type;
	
	
	public EdmParameterBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public EdmParameterBuilder setMode(EdmParameterMode mode) {
		this.mode = mode;
		return this;
	}

	public EdmParameterBuilder setType(EdmType type) {
		this.type = type;
		return this;
	}

	@Override
    public EdmParameterBuilder setDocumentation(EdmDocumentation documentation) {
	    super.setDocumentation(documentation);
	    return this;
    }

	@Override
    public EdmParameterBuilder setDocumentation(String summary, String longDescription) {
	    super.setDocumentation(summary, longDescription);
	    return this;
    }

	@Override
    public EdmParameterBuilder setDocumentation(String title, String summary, String longDescription) {
	    super.setDocumentation(title, summary, longDescription);
	    return this;
    }

	public EdmParameter build() {
	    return new EdmParameter(name, type, mode, documentation);
    }
}
