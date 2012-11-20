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
package bingo.lang;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import bingo.lang.exceptions.ObjectNotFoundException;

public class Methods {

	protected Methods(){
		
	}
	
	public static String getFullQualifiedName(Method m){
		return m.getDeclaringClass().getName() + "#" + m.getName();
	}
	
	public static Method forName(String methodFullName) throws ObjectNotFoundException {
		int index = methodFullName.lastIndexOf("#");
		
		if(index > 0){
			String className  = methodFullName.substring(0,index);
			String methodName = methodFullName.substring(index+1);
			
			Class<?> clazz = Classes.forName(className);

			for(Method m : Reflects.getMethods(clazz)){
				if(m.getName().equals(methodName)){
					return m;
				}
			}
		}else{
			index = methodFullName.lastIndexOf(".");
			
			if(index > 0){
				String className  = methodFullName.substring(0,index);
				String methodName = methodFullName.substring(index+1);
				
				Class<?> clazz = Classes.forName(className);

				for(Method m : Reflects.getMethods(clazz)){
					if(m.getName().equals(methodName)){
						return m;
					}
				}
			}
		}
		
		throw new ObjectNotFoundException("method '{0}' not found",methodFullName);
	}
	
	public static Enumerable<Method> findMany(Class<?> clazz,String name) {
		List<Method> methods = new ArrayList<Method>();
		
		for(Method m : Reflects.getMethods(clazz)){
			if(m.getName().equals(name)){
				methods.add(m);
			}
		}
		
		return Enumerables.of(methods);
	}
}
