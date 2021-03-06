package bingo.lang.json;
/*
 * Copyright 2010 the original author or authors.
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


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import bingo.lang.Enums;
import bingo.lang.Strings;
import bingo.lang.codec.Base64;

public class JSONWriterImpl implements JSONWriter {
    
    static final char[] HEX_CHARS = new char[]{
        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
    }; 
    
    static final String HEX_PREFIX = "0x";
    
	static final String NULL_STRING   = "null";
	static final String EMPTY_STRING  = "\"\"";
	static final char   OPEN_ARRAY    = '[';
	static final char   CLOSE_ARRAY   = ']';
	static final char   OPEN_OBJECT   = '{';
	static final char   CLOSE_OBJECT  = '}';
	static final char   CLOSE_NAME    = ':';
	static final char   DOUBLE_QUOTE  = '"';
	static final char   COMMA_CHAR    = ',';
	
	private boolean   isKeyQuoted;
	private boolean   isIgnoreNull;
	private Appendable out;
	
	JSONWriterImpl(Appendable out,boolean isKeyQuoted,boolean isIgnoreNull){
	    this.out          = out;
	    this.isKeyQuoted  = isKeyQuoted;
	    this.isIgnoreNull = isIgnoreNull;
	}
	
	public JSONWriter startObject() {
        try {
    		out.append(OPEN_OBJECT);
        } catch (IOException e) {
        	wrapAndThrow(e);
        }	
        return this;
    }
	
	public JSONWriter startObject(String name) {
	    return name(name).startObject();
    }

	public JSONWriter property(String name,String stringValue) {
		return isIgnoreNull && null == stringValue ? this : name(name).value(stringValue);
	}
	
	public JSONWriter propertyOptional(String name, String stringValue) {
	    return Strings.isEmpty(stringValue) ? this : name(name).value(stringValue);
    }

	public JSONWriter property(String name,boolean boolValue) {
		return name(name).value(boolValue);
	}
	
	public JSONWriter property(String name,byte byteValue) {
		return name(name).value(byteValue);
	}
	
	public JSONWriter property(String name,short shortValue) {
		return name(name).value(shortValue);
	}
	
	public JSONWriter property(String name,int intValue) {
		return name(name).value(intValue);
	}
	
	public JSONWriter property(String name,long longValue) {
		return name(name).value(longValue);
	}
	
	public JSONWriter property(String name,float floatValue) {
		return name(name).value(floatValue);
	}
	
	public JSONWriter property(String name,double doubleValue) {
		return name(name).value(doubleValue);
	}
	
	public JSONWriter property(String name,BigDecimal decimalValue) {
		return isIgnoreNull && null == decimalValue ? this : name(name).value(decimalValue);
	}
	
	public JSONWriter property(String key, Number numberValue) {
	    return isIgnoreNull && null == numberValue ? this : name(key).value(numberValue) ;
    }

	public JSONWriter property(String name,Date dateValue) {
		return isIgnoreNull && null == dateValue ? this : name(name).value(dateValue);
	}
	
	public JSONWriter endObject() {
        try {
    		out.append(CLOSE_OBJECT);
        } catch (IOException e) {
        	wrapAndThrow(e);
        }		
        return this;
    }
	
	public JSONWriter array(Date... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			if(i > 0){
				separator();
			}
			value(array[i]);
		}
		endArray();
	    return this;
    }

	public JSONWriter array(double... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			if(i > 0){
				separator();
			}
			value(array[i]);
		}
		endArray();
	    return this;
    }

	public JSONWriter array(float... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			if(i > 0){
				separator();
			}
			value(array[i]);
		}
		endArray();
	    return this;
    }

	public JSONWriter array(Number... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			if(i > 0){
				separator();
			}
			value(array[i]);
		}
		endArray();
	    return this;
    }

	public JSONWriter array(short... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			if(i > 0){
				separator();
			}
			value(array[i]);
		}
		endArray();
	    return this;
    }

	public JSONWriter array(int... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			if(i > 0){
				separator();
			}
		}
		endArray();
	    return this;
    }

	public JSONWriter array(long... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			if(i > 0){
				separator();
			}
			value(array[i]);
		}
		endArray();
	    return this;
    }

	public JSONWriter array(String... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			if(i > 0){
				separator();
			}
			value(array[i]);
		}
		endArray();
	    return this;
    }

	public JSONWriter arrayIgnoreEmpty(String... array) {
		int len = array.length;
		startArray();
		for(int i=0;i<len;i++){
			String s = array[i];
			
			if(Strings.isEmpty(s)){
				continue;
			}
			
			if(i > 0){
				separator();
			}
			value(s);
		}
		endArray();
	    return this;
    }

	public JSONWriter startArray() {
        try {
    		out.append(OPEN_ARRAY);
        } catch (IOException e) {
        	wrapAndThrow(e);
        }		
        return this;
    }
	
	public JSONWriter startArray(String name) {
	    return name(name).startArray();
    }

	public JSONWriter endArray() {
        try {
    		out.append(CLOSE_ARRAY);
        } catch (IOException e) {
        	wrapAndThrow(e);
        }	
        return this;
    }
	
	public JSONWriter value(boolean bool) {
        try {
	        out.append(String.valueOf(bool));
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
    
	public JSONWriter value(byte b) {
        try {
        	out.append(String.valueOf(b));
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
	
	public JSONWriter value(char c) {
    	return value(String.valueOf(c));
    }
    
	public JSONWriter value(byte[] bytes) {
        try {
        	if(null == bytes || bytes.length == 0){
        		out.append(EMPTY_STRING);
        	}else{
        		out.append(DOUBLE_QUOTE)
        		   .append(Base64.encode(bytes))
        		   .append(DOUBLE_QUOTE);
        	}
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
	
	public JSONWriter value(short s) {
		return raw(String.valueOf(s));
    }
	
	public JSONWriter value(int i) {
		return raw(String.valueOf(i));
    }
	
	public JSONWriter value(long l) {
		return raw(String.valueOf(l));
    }
	
	public JSONWriter value(float f) {
		return raw(String.valueOf(f));
    }
	
	public JSONWriter value(double d) {
		return raw(String.valueOf(d));
    }
	
	public JSONWriter value(BigDecimal decimal) {
        try {
	        out.append(null == decimal ? NULL_STRING : decimal.toString());
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
    
	public JSONWriter value(Number number) {
        try {
	        out.append(null == number ? NULL_STRING : String.valueOf(number));
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
    
	public JSONWriter value(Date date) {
        try {
        	out.append(null == date ? NULL_STRING : String.valueOf(date.getTime()));
        } catch (IOException e) {
        	wrapAndThrow(e);
        }	
        return this;
    }
	
	public JSONWriter name(String key) {
        try {
        	if(isKeyQuoted){
        		out.append(DOUBLE_QUOTE).append(key).append(DOUBLE_QUOTE);   
        	}else{
        		out.append(key);	
        	}
        	out.append(CLOSE_NAME);
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
	
    public JSONWriter value(String string) {
        try {
            if (string == null) {
            	out.append(NULL_STRING);
            }else if(string.length() == 0){
            	out.append(EMPTY_STRING);
            }else{
                char c   = 0;
                int  len = string.length();

                out.append(DOUBLE_QUOTE);
                for (int i = 0; i < len; i++) {
                    c = string.charAt(i);
                    switch (c) {
                    case '\\':
                        out.append("\\\\");
                        break;
                    case '"':
                    	out.append("\\\"");
                        break;
                    case '\b':
                    	out.append("\\b");
                        break;
                    case '\t':
                    	out.append("\\t");
                        break;
                    case '\n':
                    	out.append("\\n");
                        break;
                    case '\f':
                    	out.append("\\f");
                        break;
                    case '\r':
                    	out.append("\\r");
                        break;
                    default:
                    	out.append(c);
                    }
                }
                out.append(DOUBLE_QUOTE);
            }
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        
        return this;
    }
	
    public JSONWriter nullValue() {
        try {
        	out.append(NULL_STRING);
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
    
    public JSONWriter value(Object value) {
    	if (null == value) {
            return nullValue();
        } else if (value instanceof String) {
            return value((String)value);
        } else if (value instanceof Byte) {
            return value(((Byte) value).byteValue());
        } else if (value instanceof Boolean) {
            return value(((Boolean) value).booleanValue());
        } else if (value instanceof Character) {
            return value(((Character) value).charValue());
        } else if (value instanceof Number) {
            return value((Number) value);
        } else if (value instanceof Date) {
            return value((Date) value);
        } else if (value instanceof Class<?>) {
            return value(((Class<?>) value).getName());
        } else if (value instanceof byte[]){
        	return value((byte[])value);
        } else if(value instanceof Enum<?>){
        	return value(Enums.getValue((Enum<?>)value));
        }
    	
    	throw new IllegalArgumentException("the value '" + value.getClass().getName() + "' must be simple type");
    }

	public JSONWriter separator() {
		try {
	        out.append(COMMA_CHAR);
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
	
    public JSONWriter raw(String string){
    	try {
	        out.append(string);
        } catch (IOException e) {
        	wrapAndThrow(e);
        }
        return this;
    }
    
    @Override
    public String toString() {
	    return out.toString();
    }
    
	private void wrapAndThrow(IOException e){
		throw new JSONException(e.getMessage(),e);
	}
}
