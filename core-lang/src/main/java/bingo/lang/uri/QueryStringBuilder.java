/*
 * Copyright 2013 the original author or authors.
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
package bingo.lang.uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.Builder;
import bingo.lang.Encoding;
import bingo.lang.Strings;
import bingo.lang.tuple.ImmutableEntry;

public class QueryStringBuilder implements Builder<String>{
	
	private final String			         encoding;
	private final List<Entry<String,String>> params = new ArrayList<Map.Entry<String,String>>();
	
	public QueryStringBuilder(){
		this.encoding = Encoding.UTF_8.name();
	}
	
	public QueryStringBuilder(String encoding){
		this.encoding = encoding;
	}
	
	public QueryStringBuilder addAll(Map<String,String> queryParameters){
		for(Entry<String,String> entry : queryParameters.entrySet()){
			params.add(entry);
		}
		return this;
	}
	
	public QueryStringBuilder add(String name,String value){
		params.add(ImmutableEntry.of(name, value));
		return this;
	}
	
	public QueryStringBuilder remove(String name){
		for(Entry<String,String> entry : params){
			if(entry.getKey().equals(name)){
				params.remove(entry);
			}
		}
		return this;
	}
	
	public String build(){
		if(params.isEmpty()){
			return Strings.EMPTY;
		}
		
		StringBuilder sb = new StringBuilder();
		
		int i=0;
		for(Entry<String,String> entry : params){
			String name  = entry.getKey();
			String value = entry.getValue();
			
			if(!Strings.isEmpty(value)){
				
				if(i>0){
					sb.append("&");
				}
				
				try {
	                sb.append(name).append("=").append(URLEncoder.encode(value,encoding));
                } catch (UnsupportedEncodingException e) {
                	throw new UnsupportedOperationException("unsupported encoding : '" + encoding + "'");
                }	
				
				i++;
			}
		}
		
		return sb.toString();
	}

}
