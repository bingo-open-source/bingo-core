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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriPattern {
	private String   regex;
	private Pattern	 pattern;
	private String[] params;
	
	public static UriPattern compile(String regex){
		UriPattern pattern = new UriPattern(regex);

		pattern.compile();

		return pattern;
	}
	
	protected UriPattern(String regex){
		this.regex = regex;
	}
	
	public boolean matches(String uri){
		return matches(uri,null);
	}
	
	public boolean matches(String uri,Map<String, String> variables){
		compile();
		
		if(this.regex.equals("*")){
			return true;
		}else{
			Matcher matcher = pattern.matcher(uri);
			
			if(null == variables){
				return matcher.matches();
			}
			
            if (matcher.matches()) {
            	
            	for(int i=0;i<this.params.length;i++){
            		variables.put(this.params[i], matcher.group(i+1));
            	}
            	
            	return true;
            }					
		}
		
		return false;
	}
	
	protected void compile() {
		if(null == regex || "".equals(regex = regex.trim())){
			regex = "*";
		}else{
			StringBuilder buf = new StringBuilder();
			char[] chars = regex.toCharArray();

			List<String> groups = new ArrayList<String>();
			
			for(int i=0;i<chars.length;i++){
				char c = chars[i];

				if(c == '{'){
					for(int j=i+1;j<chars.length;j++){
						char c1 = chars[j];
						
						if(c1 == '}'){
							String group = regex.substring(i+1,j);
							
							int index = group.indexOf(":");
							
							if(index > 1){
								String name  = group.substring(0,index);
								String regex = group.substring(index+1);
								
								buf.append("(" + regex.trim() + ")");
								groups.add(name.trim());
							}else{
								buf.append("([^/]+)");
								groups.add(group.trim());
							}
							
							i = j;
							break;
						}
					}
				}else{
					buf.append(c);
				}
			}
			
			this.pattern = Pattern.compile(buf.toString());
			this.params  = groups.toArray(new String[]{});
		}
	}
}
