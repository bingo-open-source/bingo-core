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
package bingo.lang.xml;

import bingo.lang.Strings;

abstract class XmlWriterBase implements XmlWriter {

	public XmlWriter attributeOptional(String localName, String value) {
		if(!Strings.isEmpty(value)){
			attribute(localName,value);
		}
		return this;
    }

	public XmlWriter attributeOptional(String namespaceURI, String localName, String value) {
		if(!Strings.isEmpty(value)){
			attribute(namespaceURI,localName,value);
		}
		return this;
    }
	
	public XmlWriter attributeOptional(String prefix, String namespaceURI, String localName, String value) {
		if(!Strings.isEmpty(value)){
			attribute(prefix,namespaceURI,localName,value);
		}
		return this;
    }

	public XmlWriter element(String localName, String text) {
	    return emptyElement(localName).text(text).endElement();
    }

	public XmlWriter element(String namespaceURI, String localName, String text) {
	    return emptyElement(namespaceURI, localName).text(text).endElement();
    }
	
	public XmlWriter element(String prefix, String namespaceURI, String localName, String text) {
	    return emptyElement(prefix, namespaceURI, localName).text(text).endElement();
    }

	public XmlWriter elementOptional(String localName, String text) {
		if(!Strings.isEmpty(text)){
			element(localName,text);
		}
		return this; 
	}

	public XmlWriter elementOptional(String namespaceURI, String localName, String text) {
		if(!Strings.isEmpty(text)){
			element(namespaceURI,localName,text);
		}
		return this;
    }

	public XmlWriter elementOptional(String prefix, String namespaceURI, String localName, String text) {
		if(!Strings.isEmpty(text)){
			element(prefix, namespaceURI, localName, text);
		}
		return this;
    }
}
