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
package bingo.lang.json;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked","rawtypes"})
public class JSONObject {
    
    private Object value;

	protected JSONObject(Object value){
	    this.value = value;
	}
	
	public boolean isNull(){
	    return null == value;
	}
    
	public boolean isArray(){
    	return null != value && (value.getClass().isArray() || value instanceof List);
    }
    
    public boolean isMap(){
        return null != value && value instanceof Map;
    }
    
    public Object value(){
        return value;
    }
    
    public Object[] array(){
    	return ((List)value).toArray();
    }
    
    public List<Object> arraylist(){
        return (List)value;
    }
    
    public Map<String, Object> map(){
        return (Map<String,Object>)value;
    }
}