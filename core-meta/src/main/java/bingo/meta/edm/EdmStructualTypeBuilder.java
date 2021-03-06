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

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;

public abstract class EdmStructualTypeBuilder extends EdmBuilderWithDocumentation {

	protected List<EdmProperty> properties = new ArrayList<EdmProperty>();

	public Enumerable<EdmProperty> getProperties(){
		return Enumerables.of(properties);
	}

	public EdmStructualTypeBuilder addProperty(EdmProperty property) {
		properties.add(property);
		return this;
	}
	
	public EdmStructualTypeBuilder addProperty(String name,EdmType type,boolean nullable) {
		return addProperty(new EdmPropertyBuilder(name, type, nullable).build());
	}
}