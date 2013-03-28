package bingo.lang.xml;

import javax.xml.namespace.QName;

public interface XmlReader {
	
    boolean next();
    
    boolean nextToChildElement(QName childElementName);
    
    boolean nextToChildElement(String childElementName);
    
    boolean nextToEndElement();
    
    boolean isStartElement();
    
    boolean isStartElement(QName name);
    
    boolean isStartElement(String name);
    
    boolean isEndElement();
    
    boolean isEndElement(QName name);
    
    boolean isEndElement(String name);
    
    boolean isEndDocument();
    
    QName getElementName();
    
    String getLocalElementName();

    String getElementText();
    
    String getAttributeValue(QName name);
    
    String getAttributeValue(String name);
    
    void close();
}