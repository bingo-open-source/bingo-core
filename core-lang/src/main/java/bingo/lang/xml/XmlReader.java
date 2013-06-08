package bingo.lang.xml;

import javax.xml.namespace.QName;

public interface XmlReader {
	
    boolean next();
    
    boolean nextToChildElement(QName childElementName);
    
    boolean nextToChildElement(String childElementName);
    
    boolean nextToEndElement();
    
    boolean nextIfElementNotEnd(QName elementName);
    
    boolean nextIfElementNotEnd(String elementName);
    
    boolean isStartElement();
    
    boolean isStartElement(QName name);
    
    boolean isStartElement(String name);
    
    boolean isEndElement();
    
    boolean isEndElement(QName name);
    
    boolean isEndElement(String name);
    
    boolean isEndDocument();
    
    QName getElementName();
    
    String getElementLocalName();

    String getElementText();
    
    String getAttributeValue(QName name);
    
    String getAttributeValue(String name);
    
    String requiredGetAttributeValue(String name);
    
    Boolean getAttributeValueForBool(QName name);
    
    Boolean getAttributeValueForBool(String name);
    
    boolean getAttributeValueForBool(QName name,boolean defaultValue);
    
    boolean getAttributeValueForBool(String name,boolean defaultValue);
    
    Integer getAttributeValueForInt(QName name);
    
    Integer getAttributeValueForInt(String name);
    
    int getAttributeValueForInt(QName name,int defaultValue);
    
    int getAttributeValueForInt(String name,int defaultValue);
    
    void close();
}