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
package bingo.lang.config;

import java.io.IOException;
import java.io.InputStream;

import bingo.lang.Strings;
import bingo.lang.exceptions.UncheckedIOException;
import bingo.lang.io.IO;
import bingo.lang.resource.Resource;
import bingo.lang.resource.Resources;


public class JavaProperties extends PropertiesEx {
	
	private static final long serialVersionUID = 3509688026283093725L;

	public static JavaProperties loadFromResource(String resourceLocation) throws UncheckedIOException{
		InputStream in = null;
		try{
			if(resourceLocation.indexOf(":") < 0){
				in = JavaProperties.class.getResourceAsStream(resourceLocation);
			}else{
				Resource resource = Resources.getResource(resourceLocation);
				if(null != resource && resource.exists()){
					in = resource.getInputStream();
				}
			}
			
			if(null == in){
				return null;
			}
			
			if(Strings.endsWithIgnoreCase(resourceLocation,".xml")){
				return loadFromXmlInputStream(Resources.getInputStream(resourceLocation));
			}else{
				return loadFromInputStream(Resources.getInputStream(resourceLocation));	
			}
		}catch(IOException e){
			throw new UncheckedIOException(e.getMessage(),e);
		}finally{
			IO.close(in);
		}
	}
	
	public static JavaProperties loadFromInputStream(InputStream in) throws UncheckedIOException{
		if(null == in){
			return null;
		}
		JavaProperties p = new JavaProperties();
		try {
			p.load(in);
		} catch (IOException e) {
			throw new UncheckedIOException(e.getMessage(),e);
		}
		return p;
	}
	
	public static JavaProperties loadFromXmlInputStream(InputStream in) throws UncheckedIOException {
		if(null == in){
			return null;
		}
		JavaProperties p = new JavaProperties();
		try {
			p.loadFromXML(in);
		} catch (IOException e) {
			throw new UncheckedIOException(e.getMessage(),e);
		}
		return p;
	}

	protected JavaProperties(){
		
	}
}
