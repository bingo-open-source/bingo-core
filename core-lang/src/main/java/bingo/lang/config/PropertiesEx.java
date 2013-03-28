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
package bingo.lang.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import bingo.lang.Objects;

public abstract class PropertiesEx extends Properties {
	
	private static final long serialVersionUID = 9124896903570512706L;

	public Map<String, String> toMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		for(Map.Entry<Object, Object> entry : this.entrySet()){
			map.put(entry.getKey().toString(),Objects.toString(entry.getValue()));
		}
		
		return map;
	}
}
