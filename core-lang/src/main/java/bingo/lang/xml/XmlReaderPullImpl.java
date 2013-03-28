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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import bingo.lang.Exceptions;
import bingo.lang.Strings;

final class XmlReaderPullImpl extends XmlReaderBase implements XmlReader {
	
	private static final XmlPullParserFactory factory ;
	
	static {
		try {
	        factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
	        throw XmlException.wrap(e);
        }
	}	

    private final XmlPullParser xpp;

    public XmlReaderPullImpl(Reader in) {
        try {
	        this.xpp = factory.newPullParser();
	        this.xpp.setInput(in);
        } catch (XmlPullParserException e) {
        	throw XmlException.wrap(e);
        }
    }
    
    public boolean isEndElement() {
        try {
            return xpp.getEventType() == XmlPullParser.END_TAG;
        } catch (XmlPullParserException e) {
            throw XmlException.wrap(e);
        }
    }
    
    public boolean isStartElement() {
        try {
            return xpp.getEventType() == XmlPullParser.START_TAG;
        } catch (XmlPullParserException e) {
            throw XmlException.wrap(e);
        }
    }

	public boolean isEndElement(QName name) {
        return isEndElement() && nameEquals(name);
    }

	public boolean isStartElement(QName name) {
        return isStartElement() && nameEquals(name);
    }
	
	public boolean isStartElement(String name) {
		return isStartElement() && xpp.getName().equals(name);
	}

	public boolean isEndElement(String name) {
		return isEndElement() && xpp.getName().equals(name);
	}

	public boolean isEndDocument() {
        try {
            return xpp.getEventType() == XmlPullParser.END_DOCUMENT;
        } catch (XmlPullParserException e) {
            throw XmlException.wrap(e);
        }
    }
	
    public QName getElementName() {
	    return new QName(xpp.getNamespace(), xpp.getName());
    }
    
	public String getLocalElementName() {
		return xpp.getName();
	}

	public String getElementText() {
        try {
            return xpp.nextText();
        } catch (Exception e) {
            throw Exceptions.uncheck(e);
        }
    }
    
	public String getAttributeValue(QName name) {
        return xpp.getAttributeValue(name.getNamespaceURI(), name.getLocalPart());
    }
	
    public String getAttributeValue(String name) {
	    return xpp.getAttributeValue(null, name);
    }

	public boolean next() {
    	if(isEndDocument()){
    		return false;
    	}
        try {
            return xpp.next() != XmlPullParser.END_DOCUMENT;
        } catch (Exception e) {
            throw XmlException.wrap(e);
        }
    }
	
	public void close() {
		
    }

	private boolean nameEquals(QName name){
		return Strings.equals(xpp.getName(), name.getLocalPart()) && Strings.equals(xpp.getNamespace(), name.getNamespaceURI());
	}
}