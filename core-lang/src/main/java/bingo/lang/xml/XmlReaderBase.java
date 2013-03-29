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

import javax.xml.namespace.QName;

abstract class XmlReaderBase implements XmlReader {

	public boolean nextToChildElement(QName name) {
		if(!isStartElement()){
			next();
		}
		if(isStartElement()){
			QName currentName = getElementName();
			
			while(next()){
				if(isEndElement(currentName)){
					break;
				}
				
				if(isStartElement(name)){
					return true;
				}
			}
		}
	    return false;
    }
	
	public boolean nextToChildElement(String childElementName) {
		if(!isStartElement()){
			next();
		}
		while(next()){
			String currentName = getElementLocalName();
			
			if(isEndElement(currentName)){
				break;
			}
			
			if(isStartElement(childElementName)){
				return true;
			}
		}
		return false;
	}
	
	public boolean nextToEndElement() {
		if(isStartElement()){
			String currentName = getElementLocalName();
			while(!isEndElement(currentName)){
				if(!next()){
					return false;
				}
			}
			return true;
		}else{
			while(!isEndElement()){
				if(!next()){
					return false;
				}
			}
			return true;
		}
	}
	
	public boolean nextIfElementNotEnd(QName elementName) {
		if(isEndElement(elementName)){
			return false;
		}
		return next();
    }
}

