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

import java.util.Map;

import bingo.lang.beans.BeanModel;
import bingo.lang.beans.BeanProperty;
import bingo.lang.exceptions.ObjectNotFoundException;

public class Beans {

	protected Beans(){
		
	}
	
	public static <T> BeanModel<T> forType(Class<T> classType) {
		return BeanModel.get(classType);
	}
	
	public static BeanModel<?> forName(String className) throws ObjectNotFoundException {
		return BeanModel.get(Classes.forName(className));
	}
	
	public static BeanProperty[] getProperties(Class<?> beanType) {
		return BeanModel.get(beanType).getProperties();
	}
	
	public static <T> T newInstance(Class<T> beanType){
		return BeanModel.get(beanType).newInstance();
	}
	
	public static boolean set(Object bean,String property,Object value){
		return BeanModel.get(bean.getClass()).set(bean, property, value);
	}
	
	public static Object get(Object bean,String property){
		return BeanModel.get(bean.getClass()).get(bean, property);
	}
	
	public static Map<String, Object> toMap(Object bean) {
		if(null == bean){
			return null;
		}
		return BeanModel.get(bean.getClass()).toMap(bean);
	}
}
