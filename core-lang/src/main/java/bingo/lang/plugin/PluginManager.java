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
package bingo.lang.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bingo.lang.Assert;
import bingo.lang.Classes;
import bingo.lang.Collections;
import bingo.lang.Reflects;
import bingo.lang.Strings;
import bingo.lang.beans.BeanClass;
import bingo.lang.beans.BeanProperty;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.lang.reflect.ReflectClass;
import bingo.lang.resource.Resource;
import bingo.lang.resource.Resources;
import bingo.lang.xml.XmlDocument;
import bingo.lang.xml.XmlElement;
import bingo.lang.xml.XmlValidationException;
import bingo.lang.xml.XmlValidator;

@SuppressWarnings("unchecked")
public class PluginManager<T,P extends Plugin<T>> {
	
	private static final Log log = LogFactory.get(PluginManager.class);
	
	private static final XmlValidator validator = XmlValidator.of(Resources.getInputStream(PluginManager.class,"plugin.xsd"));
	
	public static final String DEFAULT_PLUGIN_SEARCH_PATH = "/META-INF/plugins";
	
	private String          configName;
	private Class<P> 		 pluginType;
	private Class<T> 		 beanType;
	private ReflectClass<P> reflectPlugin;
	private String[] 		 locations;
	private LoadContext		 context = new LoadContext();
	private Map<String, P>  plugins = new ConcurrentHashMap<String, P>();
	
	protected PluginManager(){

	}
	
	public PluginManager(Class<T> beanType){
		this(beanType,(Class<P>)Plugin.class);
	}
	
	public PluginManager(Class<T> beanType,String configName){
		this(beanType,(Class<P>)Plugin.class,configName);
	}
	
	public PluginManager(Class<T> beanType,Class<P> pluginType){
		this(beanType,pluginType,beanType.getName());
	}
	
	public PluginManager(Class<T> beanType,Class<P> pluginType,String configName){
		this.beanType      = beanType;
		this.pluginType    = pluginType;
		this.configName    = configName;  
		this.reflectPlugin = ReflectClass.get(pluginType);
	}
	
	public P[] plugins(){
		return Collections.toArray(plugins.values(),pluginType);
	}
	
	public synchronized P[] load(){
		if(null == locations){
			locations = new String[]{DEFAULT_PLUGIN_SEARCH_PATH};
		}
		
		for(String location : locations){
			load(location);
		}
		
		for(XmlElement e : context.adds){
			loadNewPlugin(e);
		}
		
		for(XmlElement e : context.sets){
			loadExistPlugin(e);
		}
		
		return plugins();
	}
	
	public synchronized void unload(){
		try{
			for(P p : plugins.values()){
				try {
		            p.unload(this);
	            } catch (Throwable e) {
	            	log.warn("Unload plugin '{}' of bean '{}' error",p.getBean().getClass().getName(),e);
	            }
			}
		}finally{
			plugins = null;
			plugins = new ConcurrentHashMap<String, P>();
		}
	}
	
	protected void load(String location) {
		String path = Resources.CLASSPATH_ALL_URL_PREFIX + location + "/" + configName + ".xml";
		
		Resource[] resources = Resources.scan(path);
		
		for(Resource resource : resources){
			load(XmlDocument.load(resource));
		}
	}
	
	protected void load(XmlDocument doc) {
		validator.validate(doc);
		
		for(XmlElement e : doc.root().childElements()){
			if(e.name().equals("add")) {
				context.adds.add(e);
			}else if(e.name().equals("set")){
				context.sets.add(e);
			}else{
				throw new IllegalStateException("found unknow xml element '" + e.name() + "' in : " + doc.url());
			}
		}
	}
	
	protected void loadNewPlugin(XmlElement e){
		String   name        = e.requiredAttributeValue("name");
		String   clazzName   = e.requiredAttributeValue("class");
		Class<?> clazzObject = Classes.forName(clazzName);
		
		Assert.isFalse(plugins.containsKey(name.toLowerCase()),"plugin name '{0}' aleady exists, please check the xml : {1}",name,e.documentUrl());
		
		Assert.isTrue(beanType.isAssignableFrom(clazzObject),"'class' value of plugin '" + name + "' is invalid in xml : " + e.documentUrl());
		
		P plugin = reflectPlugin.newInstance();
		
		plugin.setBean((T)Reflects.newInstance(clazzObject));

		plugins.put(name.toLowerCase(), plugin);
		
		loadPlugin(e, plugin);
	}
	
	protected void loadExistPlugin(XmlElement e){
		String name  = e.requiredAttributeValue("name");
		String clazz = e.attributeValue("class");

		if(Strings.isEmpty(clazz)){
			throw new XmlValidationException("cannot use 'class' attribute in <set ...> element in xml : {0}",e.documentUrl());
		}

		P plugin = plugins.get(name.toLowerCase());

		Assert.notNull(plugin,"plugin '{0}' not exists,please check the xml : {1}",e.documentUrl());
		
		loadPlugin(e, plugin);
	}
	
	protected void loadPlugin(XmlElement e,P plugin){
		XmlElement document = e.childElement("document");

		if(null != document){
			String summary     = Strings.trim(document.childElementText("summary"));
			String description = Strings.trim(document.childElementText("description"));
			
			if(!Strings.isEmpty(summary)){
				plugin.setSummary(summary);
			}
			
			if(!Strings.isEmpty(description)){
				plugin.setDescription(description);
			}
		}
		
		XmlElement properties = e.childElement("properties");
		
		if(null != properties){
			T 			 bean 	   = plugin.getBean();
			BeanClass<?> beanClass = BeanClass.get(bean.getClass());
			
			for(XmlElement prop : properties.childElements()){
				String propName  = prop.requiredAttributeValue("name");
				String propValue = Strings.trimToNull(prop.attributeOrText("value"));
				
				plugin.setProperty(propName, propValue);

				BeanProperty beanProp = beanClass.getProperty(propName);
				
				if(null != beanProp && beanProp.isWritable()){
					beanProp.setValue(bean, propValue);
				}
			}
		}
	}
	
	private static final class LoadContext {
		List<XmlElement> adds = new ArrayList<XmlElement>();
		List<XmlElement> sets = new ArrayList<XmlElement>();
	}
}