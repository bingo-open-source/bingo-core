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

import java.io.Reader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

final class XmlReaderStaxImpl extends XmlReaderBase implements XmlReader {
	private static final XMLInputFactory factory = XMLInputFactory.newInstance();
	
    private final XMLEventReader real;
    
    private XMLEvent event;
    
    public XmlReaderStaxImpl(Reader in) {
        try {
            this.real = factory.createXMLEventReader(in);
        } catch (XMLStreamException e) {
            throw XmlException.wrap(e);
        }
    }
    
	public boolean isEndElement() {
	    return null != event && event.isEndElement();
    }

	public boolean isEndElement(QName name) {
	    return isEndElement() && event.asEndElement().getName().equals(name);
    }

	public boolean isStartElement() {
	    return null != event && event.isStartElement();
    }

	public boolean isStartElement(QName name) {
	    return isStartElement() && event.asStartElement().getName().equals(name);
    }
	
	public boolean isStartElement(String name) {
		return isStartElement() && event.asStartElement().getName().getLocalPart().equals(name);
	}

	public boolean isEndElement(String name) {
		return isEndElement() && event.asEndElement().getName().getLocalPart().equals(name);
	}

	public boolean isEndDocument() {
	    return null != event && event.isEndDocument();
    }
	
    public QName getElementName() {
	    return event.asStartElement().getName();
    }
    
	public String getElementLocalName() {
		return event.asStartElement().getName().getLocalPart();
	}

	public String getElementText() {
        try {
            return real.getElementText();
        } catch (XMLStreamException e) {
        	throw XmlException.wrap(e);
        }
    }
    
	public String getAttributeValue(QName name) {
        Attribute attr = attr(name);
        
        return attr == null ? null : attr.getValue();
    }
	
    public String getAttributeValue(String name) {
	    return getAttributeValue(new QName(name));
    }
    
	public boolean next() {
    	try {
            if(real.hasNext()){
            	event = real.nextEvent();
            	return true;
            }
            return false;
        } catch (XMLStreamException e) {
        	throw XmlException.wrap(e);
        }
    }
	
	public void close() {
		try {
	        real.close();
        } catch (XMLStreamException e) {
        	throw XmlException.wrap(e);
        }
    }

	private Attribute attr(QName name){
		return null == event ? null : event.asStartElement().getAttributeByName(name);
	}
}