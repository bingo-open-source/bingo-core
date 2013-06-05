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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bingo.lang.Strings;

final class XmlReaderDomImpl extends XmlReaderBase implements XmlReader {
	
	private static final Integer START_ELEMENT = 1;
	private static final Integer END_ELEMENT   = 2;
	private static final Integer END_DOCUMENT  = 100;
	
    private final Document document;
    
    private Element current;
    private boolean down  = true;
    private Integer event = null;
    
    public XmlReaderDomImpl(Reader in){
    	this.document = XmlDocument.parse(in).domDocument();
    }
    
	public boolean isEndElement() {
	    return event == END_ELEMENT;
    }

	public boolean isEndElement(QName name) {
		 return event == END_ELEMENT && Strings.equals(current.getNamespaceURI(),name.getNamespaceURI()) && current.getLocalName().equals(name.getLocalPart());
    }

	public boolean isStartElement() {
	    return event == START_ELEMENT;
    }

	public boolean isStartElement(QName name) {
	    return event == START_ELEMENT && Strings.equals(current.getNamespaceURI(),name.getNamespaceURI()) && current.getLocalName().equals(name.getLocalPart());
    }
	
	public boolean isStartElement(String name) {
		return event == START_ELEMENT && current.getLocalName().equals(name);
	}

	public boolean isEndElement(String name) {
		return event == END_ELEMENT && current.getLocalName().equals(name);
	}

	public boolean isEndDocument() {
	    return event == END_DOCUMENT;
    }
	
    public QName getElementName() {
	    return null != current ? new QName(current.getNamespaceURI(),current.getLocalName()) : null;
    }

	public String getElementLocalName() {
		return null != current ? current.getLocalName() : null;
	}

	public String getElementText() {
        return XmlUtils.getElementText(current);
    }
    
    public String getAttributeValue(QName name) {
        return null != current ? current.getAttributeNS(name.getNamespaceURI(), name.getLocalPart()) : null;
    }
	
    public String getAttributeValue(String name) {
    	if(null == current){
    		return null;
    	}
    	
    	NamedNodeMap attrs = current.getAttributes();
    	
    	for(int i=0;i<attrs.getLength();i++){
    		Node attr = attrs.item(i);
    	
    		if(attr.getLocalName().equals(name)){
    			return attr.getNodeValue();
    		}
    	}
    	
	    return null;
    }

	public boolean next() {
    	if(isEndDocument()){
    		return false;
    	}
    	
        if (current == null) {
            return startElement(document.getDocumentElement());
        } else {
            if (down) {
                // traverse down
                NodeList children = current.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node childNode = children.item(i);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        return startElement((Element) childNode);
                    }
                }
                down = false;
                return endElement();
            } else {
                // traverse up
                Node parentNode = current.getParentNode();
                
                if (parentNode.getNodeType() == Node.DOCUMENT_NODE) {
                	event = END_DOCUMENT;
                    return false;
                }
                
                Element parentElement = (Element) parentNode;
                NodeList children = parentElement.getChildNodes();
                boolean found = false;
                for (int i = 0; i < children.getLength(); i++) {
                    Node childNode = children.item(i);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {

                        if (!found) {
                            if (childNode == current)
                                found = true;
                            continue;
                        }
                        
                        down = true;
                        return startElement((Element) childNode);
                    }
                }
                current = parentElement;
                return endElement();
            }
        }
    }
    
	public void close() {
		
    }

	private boolean startElement(Element element){
		this.current = element;
		this.event   = START_ELEMENT;
		return true;
	}
	
	private boolean endElement(){
		this.event = END_ELEMENT;
		return true;
	}
}