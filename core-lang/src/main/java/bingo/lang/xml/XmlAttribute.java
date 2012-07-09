package bingo.lang.xml;

import bingo.lang.Converts;
import bingo.lang.Strings;

public class XmlAttribute extends XmlObject implements XmlNamed {

	private String prefix;
	private String name;
	private String value;

	public XmlAttribute(String name, Object value) {
		this(null,name,value);
	}

	public XmlAttribute(String prefix, String name, Object value) {
		this.prefix = prefix;
		this.name   = name;
		this.value = value.toString();
	}
	
	public String name() {
	    return name;
    }

	public String prefix() {
	    return prefix;
    }

	public String value() {
		return value;
	}
	
	public XmlAttribute required() throws XmlValidationException {
		if(Strings.isEmpty(value)) {
			throw new XmlValidationException("the value of attribute '{0}' is requried",name);
		}
		return this;
	}
	
	public int intValue(){
		return Strings.isEmpty(value) ? 0 : Converts.convert(Strings.trimToNull(value), Integer.TYPE);
	}

	public int intValue(int defaultValue){
		return Strings.isEmpty(value) ? defaultValue : Converts.convert(Strings.trimToNull(value), Integer.TYPE);
	}
	
	public boolean boolValue(){
		return Strings.isEmpty(value) ? false : Converts.convert(Strings.trimToNull(value), Boolean.TYPE);
	}
	
	public boolean boolValue(boolean defaultValue){
		return Strings.isEmpty(value) ? defaultValue : Converts.convert(Strings.trimToNull(value), Boolean.TYPE);
	}

	@Override
	public XmlNodeType nodeType() {
		return XmlNodeType.ATTRIBUTE;
	}

	public void setValue(String value) {
		this.value = value;
	}
}