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

import java.io.Reader;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import bingo.lang.Converts;
import bingo.lang.Strings;

public class JSON {
    
    private static int ENCODE_MAXIMIZED = 0;
    private static int ENCODE_MINIMIZED = JSONSettings.IGNORE_NULL | JSONSettings.IGNORE_EMPTY | JSONSettings.KEY_NON_QUOTE;
    
//    private static final JSONEncoder encoderMinimized  = new JSONEncoder(new JSONSettings(ENCODE_MINIMIZED));
//    private static final JSONEncoder encoderMaximized  = new JSONEncoder(new JSONSettings(ENCODE_MAXIMIZED));
    private static final JSONDecoder decoderPermissive = new JSONDecoder();
    
	public static String encode(Object object){
	    return new JSONEncoder(new JSONSettings(ENCODE_MINIMIZED)).encode(object);
	}

	public static String encode(Object object,boolean minimized){
	    if(minimized){
	        return new JSONEncoder(new JSONSettings(ENCODE_MINIMIZED)).encode(object);
	    }
	    return new JSONEncoder(new JSONSettings(ENCODE_MAXIMIZED)).encode(object);
	}
	
	public static JSONObject decode(Reader reader) {
	    return new JSONObject(decoderPermissive.decode(reader));
	}

	public static JSONObject decode(String string) {
		return new JSONObject(decoderPermissive.decode(string));
	}
	
	public static <T> T decode(String string,Class<? extends T> type){
	    return Converts.convert(decoderPermissive.decode(string),type);
	}
	
    public static <T> T decode(Reader reader,Class<? extends T> type){
        return Converts.convert(decoderPermissive.decode(reader),type);
    }
	
    public static Map<String,Object> decodeToMap(String json){
        if(null == json || json.trim().equals("")){
            return new HashMap<String, Object>();
        }
        
        if(!json.startsWith("{")){
            json = "{" + json;
        }
        
        if(!json.endsWith("}")){
            json = json + "}";
        }
        
        return decode(json).map();
    }
    
    public static Object[] decodeToArray(String json){
        if(Strings.isBlank(json)){
            return new Object[]{};
        }
        
        if(!json.startsWith("[") && !json.endsWith("]")){
            json = "[" + json + "]";
        }
        
        return decode(json).array();
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] decodeToArray(String json,Class<T> componentType){
        T[] a = (T[])Array.newInstance(componentType, 0);
        
        if(Strings.isBlank(json)){
            return a;
        }
        
        if(!json.startsWith("[") && !json.endsWith("]")){
            json = "[" + json + "]";
        }
        
        return (T[])Converts.convert(decode(json).arraylist(),a.getClass());
    }
}