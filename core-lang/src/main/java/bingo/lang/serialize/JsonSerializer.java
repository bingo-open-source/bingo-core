/**
 * file created at 2013-6-9
 */
package bingo.lang.serialize;

import bingo.lang.Strings;
import bingo.lang.json.JSON;

public class JsonSerializer implements Serializer {

	public String serialize(Object value) {
	    return JSON.encode(value);
    }

	public Object deserialize(String string) {
	    return JSON.decode(string).value();
    }

	public Object tryDeserialize(String string) {
		if(Strings.isEmpty(string)){
			return null;
		}
		
		string = string.trim();
		
		if("null".equals(string)){
			return null;
		}
		
		if((string.startsWith("[") && string.endsWith("]")) || (string.startsWith("{") && string.endsWith("}"))){
			return JSON.decode(string).value();
		}
		
	    return string;
    }
}
