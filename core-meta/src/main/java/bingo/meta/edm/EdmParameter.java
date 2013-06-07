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

public class EdmParameter extends EdmNamedObject {

	private final EdmType type;
	
	private final EdmParameterMode mode;
	
	public EdmParameter(String name,String title,EdmType type,EdmParameterMode mode) {
		super(name,title);
		this.type = type;
		this.mode = mode;
	}
	
	public EdmParameter(String name,String title,EdmType type,EdmParameterMode mode,EdmDocumentation documentation) {
		this(name,title,type,mode);
		this.documentation = documentation;
	}

	public EdmParameterMode getMode() {
    	return mode;
    }

	public EdmType getType() {
    	return type;
    }
}
