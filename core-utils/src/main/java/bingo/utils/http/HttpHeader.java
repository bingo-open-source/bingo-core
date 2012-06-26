/*
 * Copyright 2012 the original author or authors.
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
package bingo.utils.http;

import java.io.Serializable;
import java.util.List;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.NamedValue;
import bingo.lang.Predicates;
import bingo.lang.Strings;

public class HttpHeader implements Serializable {

    private static final long serialVersionUID = -3825530827173249033L;
    
	private final String name;
    private final String value;
    
    private Enumerable<HeaderElement> elements;
    
    public HttpHeader(String name,String value){
    	this.name  = name;
    	this.value = value;
    }

	public String getName() {
    	return name;
    }

	public String getValue() {
    	return value;
    }
	
	public Enumerable<HeaderElement> getElements(){
		if(null == elements){
			if(Strings.isEmpty(value)){
				elements = Enumerables.empty();
			}else{
				elements = Enumerables.of(HttpHeaderParser.parseElements(value));
			}
		}
		return elements;
	}

	public static final class HeaderElement {
	    private final String 				 name;
	    private final String 				 value;
	    private final Enumerable<NamedValue<String>> parameters;
	    
	    HeaderElement(String name,String value,List<NamedValue<String>> parameters){
	    	this.name       = name;
	    	this.value      = value;
	    	this.parameters = Enumerables.of(parameters);
	    }

		public String getName() {
        	return name;
        }

		public String getValue() {
        	return value;
        }
		
		public String getParameter(String name){
			NamedValue<String> p = parameters.firstOrNull(Predicates.<NamedValue<String>>nameEqualsIgnoreCase(name));
			
			return null == p ? null : p.getValue();
		}

		public Enumerable<NamedValue<String>> getParameters() {
        	return parameters;
        }
	}
}
