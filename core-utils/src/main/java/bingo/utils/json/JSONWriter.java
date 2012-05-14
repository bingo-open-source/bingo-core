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
package bingo.utils.json;

import java.util.Date;

class JSONWriter {
    
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
	
	private boolean isKeyQuoted;
	
	JSONWriter(JSONSettings settings){
	    this.isKeyQuoted = settings.isKeyQuoted();
	}
	
    public void writeBoolean(Boolean bool, StringBuilder out) {
        out.append(String.valueOf(bool));
    }
    
    public void writeByte(Byte b, StringBuilder out) {
        out.append(HEX_PREFIX);
        out.append(HEX_CHARS[(b >>> 4) & 0x0F]);
        out.append(HEX_CHARS[b & 0x0F]);
    }
    
    public void writeBytes(Byte[] bytes, StringBuilder out) {
        out.append(HEX_PREFIX);
        for(Byte b : bytes){
            out.append(HEX_CHARS[(b >>> 4) & 0x0F]);
            out.append(HEX_CHARS[b & 0x0F]);
        }
    }
    
    public void writeCharacter(Character c, StringBuilder out) {
        writeString(String.valueOf(c),out);
    }
    
    public void writeNumber(Number number, StringBuilder out) {
        out.append(String.valueOf(number));
    }
    
    public void writeDate(Date date, StringBuilder out) {
//        StringBuffer temp = new StringBuffer(RFC_DATE_FORMAT.format(date)); //rfc3339
//        temp.insert(temp.length() - 2, ':');    
//        writeString(temp.toString(),out) ;      
        out.append(String.valueOf(date.getTime()));
    }
	
	public void openArray(StringBuilder out) {
		out.append(OPEN_ARRAY);
    }
	
	public void closeArray(StringBuilder out) {
		out.append(CLOSE_ARRAY);
    }
	
	public void openObject(StringBuilder out) {
		out.append(OPEN_OBJECT);
    }
	
	public void closeObject(StringBuilder out) {
		out.append(CLOSE_OBJECT);
    }
	
	public void openName(StringBuilder out) {
		//do nothing
	}
	
    public void writeName(String name, StringBuilder out) {
        if(isKeyQuoted){
            writeString(name,out);    
        }else{
            out.append(name);
        }
    }
	
	public void closeName(StringBuilder out) {
		out.append(CLOSE_NAME);
    }
	
	public void openValue(String name, StringBuilder out) {
		//do nothing
    }
	public void closeValue(String name, StringBuilder out) {
		//do nothing
    }

	public void writeNull(StringBuilder out) {
		out.append(NULL_STRING);
    }
	
	public void writeArrayValueSeperator(StringBuilder out) {
		out.append(COMMA_CHAR);
    }
	
	public void writePropertyValueSeperator(StringBuilder out) {
		out.append(COMMA_CHAR);
    }
	public void writeString(String string, StringBuilder out) {
        if (string == null) {
        	writeNull(out);
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
    }
}
