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
import java.util.concurrent.CopyOnWriteArrayList;

import bingo.lang.Assert;
import bingo.lang.Classes;
import bingo.lang.Collections;
import bingo.lang.Enumerables;
import bingo.lang.Predicates;
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
import bingo.lang.xml.XmlValidator;

public class PluginManager {
	
	private static final Log log = LogFactory.get(PluginManager.class);
	
	private static final XmlValidator validator = XmlValidator.of(Resources.getInputStream(PluginManager.class,"plugin.xsd"));
	
	public static final String[] DEFAULT_PLUGIN_SEARCH_PATHS = new String[]{"/META-INF/plugins","/plugins"};
	
	private Class<?> 		 				 beanType;
	private ReflectClass<? extends Plugin> reflectPlugin;
	
	private String		   systemConfigLocation;
	private String[] 	   locations;
	private LoadContext	   context = new LoadContext();
	private List<Plugin>  plugins = new CopyOnWriteArrayList<Plugin>();
	
	protected PluginManager(){

	}
	
	public PluginManager(Class<?> beanType){
		this(beanType,Plugin.class);
	}
	
	public PluginManager(Class<?> beanType,Class<? extends Plugin> pluginType){
		this.beanType      		   = beanType;
		this.reflectPlugin 		   = ReflectClass.get(pluginType);
		this.systemConfigLocation = Resources.CLASSPATH_URL_PREFIX + "/" + beanType.getName().replace('.', '/') + ".xml";
	}
	
	public Plugin[] getPlugins(){
		return Collections.toArray(plugins, Plugin.class);
	}
	
	public Plugin getPlugin(String name){
		return Enumerables.firstOrNull(plugins,Predicates.<Plugin>nameEqualsIgnoreCase(name));
	}
	
	public synchronized Plugin[] load(){
		if(null == locations){
			locations = DEFAULT_PLUGIN_SEARCH_PATHS;
		}

		if(null != systemConfigLocation){
			load(systemConfigLocation);
		}
		
		for(String location : locations){
			load(Resources.CLASSPATH_ALL_URL_PREFIX + location + "/" + beanType.getName() + ".xml");
		}
		
		try {
	        for(XmlElement e : context.adds){
	        	loadNewPlugin(e);
	        }
	        
	        for(XmlElement e : context.sets){
	        	loadExistPlugin(e);
	        }
        } catch (Throwable e) {
        	throw new PluginException("Loading plugin failed : {0}",e.getMessage(),e);
        }
		
		return getPlugins();
	}
	
	public synchronized void unload(){
		try{
			for(Plugin p : plugins){
				try {
		            p.unload();
	            } catch (Throwable e) {
	            	log.warn("Unload plugin '{}' of bean '{}' error",p.getBean().getClass().getName(),e);
	            }
			}
		}finally{
			plugins.clear();
		}
	}
	
	protected void load(String location) {
		Resource[] resources = Resources.scan(location);
		
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
	
	protected void loadNewPlugin(XmlElement e) throws Throwable{
		String   name        = e.requiredAttributeValue("name");
		String   clazzName   = e.requiredAttributeValue("class");
		Class<?> clazzObject = Classes.forName(clazzName);
		
		Assert.isFalse(getPlugin(name) != null,"plugin name '{0}' aleady exists, please check the xml : {1}",name,e.documentUrl());
		
		Assert.isTrue(beanType.isAssignableFrom(clazzObject),"'class' value of plugin '" + name + "' is invalid in xml : " + e.documentUrl());
		
		Plugin plugin = reflectPlugin.newInstance();
		
		plugin.setName(name);
		plugin.setBean(Reflects.newInstance(clazzObject));
		
		plugins.add(plugin);
		
		loadPlugin(e, plugin);
	}
	
	protected void loadExistPlugin(XmlElement e) throws Throwable{
		String name  = e.requiredAttributeValue("name");
		String clazz = e.attributeValue("class");
		
		Plugin plugin = getPlugin(name);

		if(null == plugin){
			loadNewPlugin(e);
			return ;
		}else if(!Strings.isEmpty(clazz)){
			plugins.remove(name.toLowerCase());
			loadNewPlugin(e);
			return;
		}
		
		loadPlugin(e, plugin);
	}
	
	protected void loadPlugin(XmlElement e,Plugin plugin) throws Throwable{
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
			Object		 bean 	   = plugin.getBean();
			BeanClass<?> beanClass = BeanClass.get(bean.getClass());
			
			for(XmlElement prop : properties.childElements()){
				String propName  = prop.requiredAttributeValue("name");
				String propValue = Strings.trimToNull(prop.attributeValueOrText("value"));
				
				plugin.setProperty(propName, propValue);

				BeanProperty beanProp = beanClass.getProperty(propName);
				
				if(null != beanProp && beanProp.isWritable()){
					beanProp.setValue(bean, propValue);
				}
			}
		}
		
		plugin.load();
	}
	
	private static final class LoadContext {
		List<XmlElement> adds = new ArrayList<XmlElement>();
		List<XmlElement> sets = new ArrayList<XmlElement>();
	}
}