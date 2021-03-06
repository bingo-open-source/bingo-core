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
import java.util.Map.Entry;

import bingo.lang.annotations.NotEmpty;
import bingo.lang.annotations.NotNull;
import bingo.lang.annotations.Validatable;
import bingo.lang.beans.BeanModel;
import bingo.lang.beans.BeanProperty;
import bingo.lang.exceptions.BeanValidateException;
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
	
	public static void set(Object bean,Map<String, Object> properties){
		if(null == properties || properties.isEmpty()){
			return ;
		}
		
		BeanModel<?> model = BeanModel.get(bean.getClass());
		
		for(Entry<String, Object> entry : properties.entrySet()){
			model.set(bean, entry.getKey(), entry.getValue());
		}
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
	
	public static void validate(Object bean) throws BeanValidateException {
		if(null != bean && bean.getClass().isAnnotationPresent(Validatable.class)){
			BeanModel<?> model = BeanModel.get(bean.getClass());
			
			for(BeanProperty prop : model.getProperties()){
				checkNotNull(bean,prop);
				checkNotEmpty(bean,prop);
			}
		}
	}
	
	private static void checkNotNull(Object bean,BeanProperty prop) {
		if(prop.isAnnotationPresent(NotNull.class)){
			Object value = prop.getValue(bean);
			
			if(null != value){
				return;
			}
			
			throw new BeanValidateException("property '{0}' in bean type '{1}' must not be null",prop.getName(),bean.getClass().getName());
		}
	}
	
	private static void checkNotEmpty(Object bean,BeanProperty prop) {
		if(prop.isAnnotationPresent(NotEmpty.class)){
			Object value = prop.getValue(bean);
			
			if(!Objects.isEmpty(value)){
				return;
			}
			
			throw new BeanValidateException("property '{0}' in bean type '{1}' must not be empty",prop.getName(),bean.getClass().getName());
		}
	}	
}
