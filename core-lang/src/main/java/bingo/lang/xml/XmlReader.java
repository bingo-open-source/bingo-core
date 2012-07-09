package bingo.lang.xml;

import javax.xml.namespace.QName;

public interface XmlReader {
	
    boolean next();
    
    boolean nextToChildElement(QName childElementName);
    
    boolean nextIfElementNotEnd(QName elementName);
    
    boolean isStartElement();
    
    boolean isStartElement(QName name);
    
    boolean isEndElement();
    
    boolean isEndElement(QName name);
    
    boolean isEndDocument();
    
    QName getElementName();

    String getElementText();
    
    String getAttributeValue(QName name);
    
    String getAttributeValue(String name);
}