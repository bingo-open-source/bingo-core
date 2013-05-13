package bingo.lang.xml;

import java.math.BigDecimal;

import bingo.lang.Converts;
import bingo.lang.Strings;

public class XmlAttribute extends XmlObject implements XmlNamed {

	private String prefix;
	private String name;
	private String value;
	private String qname;

	public XmlAttribute(String name, Object value) {
		this(null,name,value);
	}

	public XmlAttribute(String prefix, String name,Object value) {
		this.prefix = prefix;
		this.name   = name;
		this.qname  = Strings.isEmpty(prefix) ? name : (prefix + ":" + name);
		this.value = value.toString();
	}
	
	public String qname(){
		return qname;
 	}
	
	public String prefix() {
	    return prefix;
    }
	
	public String name() {
	    return name;
    }
	
	public String value() {
		return value;
	}
	
	public String valueTrimmed(){
		return Strings.trim(value);
	}
	
	public boolean isEmptyValue(){
		return Strings.isEmpty(value);
	}
	
	public XmlAttribute required() throws XmlValidationException {
		if(Strings.isEmpty(value)) {
			throw new XmlValidationException("the value of attribute '{0}' is requried",name);
		}
		return this;
	}
	
	public Integer intValue(){
		return Strings.isEmpty(value) ? null : Converts.convert(Strings.trim(value), Integer.class);
	}

	public int intValue(int defaultValue){
		Integer intValue = intValue();
		return null == intValue ? defaultValue : intValue; 
	}
	
	public Boolean boolValue(){
		return isEmptyValue() ? null : Converts.convert(Strings.trim(value), Boolean.class);
	}
	
	public boolean boolValue(boolean defaultValue){
		Boolean boolValue = boolValue();
		return null == boolValue ? defaultValue : boolValue;
	}
	
	public Float floatValue(){
		return isEmptyValue() ? null : Converts.convert(Strings.trim(value), Float.class);
	}
	
	public float floatValue(float defaultValue){
		Float floatValue = floatValue(); 
		return null == floatValue ? defaultValue : floatValue;
	}
	
	public Double doubleValue(){
		return isEmptyValue() ? null : Converts.convert(Strings.trim(value), Double.class);
	}
	
	public double doubleValue(double defaultValue){
		Double doubleValue = doubleValue(); 
		return null == doubleValue ? defaultValue : doubleValue;
	}
	
	public BigDecimal decimalValue(){
		return isEmptyValue() ? null : Converts.convert(Strings.trim(value), BigDecimal.class);
	}

	@Override
	public XmlNodeType nodeType() {
		return XmlNodeType.ATTRIBUTE;
	}

	public void setValue(String value) {
		this.value = value;
	}
}