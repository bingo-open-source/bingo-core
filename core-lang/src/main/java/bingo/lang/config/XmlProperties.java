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
package bingo.lang.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import bingo.lang.Objects;
import bingo.lang.xml.XmlDocument;
import bingo.lang.xml.XmlElement;

/**
 * <pre>
 * &lt;properties&gt;
 * 	&lt;property name="name" value="value"/&gt;
 * 	&lt;property name="name"&gt;value&lt;/property&gt;
 * 	&lt;property name="name"&gt;&lt[CDATA[...text...]]&gt;&lt;/property&gt;
 * &lt;/properties&gt;
 * </pre>
 */
public class XmlProperties extends Properties {

	private static final long serialVersionUID = -8603201912690589213L;
	
	public static XmlProperties load(XmlDocument properties) {
		return load(properties.root());
	}
	
	public static XmlProperties load(String xmlResourceLocation) {
		return load(XmlDocument.load(xmlResourceLocation));
	}
	
	public static XmlProperties load(XmlElement properties) {
		XmlProperties props = new XmlProperties();

		for(XmlElement e : properties.childElements()) {
			String name  = e.requiredAttributeValue("name");
			String value = e.attributeValueOrText("value");
			
			props.setProperty(name, value);
		}
		
		return props;
	}
	
	protected XmlProperties(){
		
	}
	
	public Map<String, String> toMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		for(Map.Entry<Object, Object> entry : this.entrySet()){
			map.put(entry.getKey().toString(),Objects.toString(entry.getValue()));
		}
		
		return map;
	}
}