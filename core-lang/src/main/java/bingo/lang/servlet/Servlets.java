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
package bingo.lang.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import bingo.lang.New;
import bingo.lang.Strings;
import bingo.lang.enumerable.EmptyEnumeration;

public class Servlets {
	
	public static final FilterChain NOT_FOUND_FILTER_CHAIN = new NotFoundFilterChain(); 

	protected Servlets(){
		
	}
	
	@SuppressWarnings("rawtypes")
    public static Map<String,String> getInitParametersMap(ServletConfig sc) {
		Map<String,String> map = New.map();
		
		Enumeration e = sc.getInitParameterNames();
		
		while(e.hasMoreElements()){
			String name = (String)e.nextElement();
			map.put(name, sc.getInitParameter(name));
		}
		
		return map;
	}
	
	@SuppressWarnings("rawtypes")
    public static Map<String,String> getInitParametersMap(FilterConfig fc) {
		Map<String,String> map = New.map();
		
		Enumeration e = fc.getInitParameterNames();
		
		while(e.hasMoreElements()){
			String name = (String)e.nextElement();
			map.put(name, fc.getInitParameter(name));
		}
		
		return map;
	}
	
	public static class EmptyServletConfig implements ServletConfig {
		
		protected String         name = Strings.EMPTY;
		protected ServletContext sc   = null;
		
		public EmptyServletConfig(){
			
		}
		
		public EmptyServletConfig(ServletContext sc){
			this.sc = sc;
		}
		
		public String getServletName() {
	        return name;
        }

		public ServletContext getServletContext() {
	        return sc;
        }

		public String getInitParameter(String name) {
	        return null;
        }

		@SuppressWarnings("rawtypes")
        public Enumeration getInitParameterNames() {
			return EmptyEnumeration.INSTANCE;
        }
	}
	
	public static class EmptyFilterConfig implements FilterConfig {
		protected String name = Strings.EMPTY;
		protected ServletContext sc = null;
		
		public EmptyFilterConfig(){
			
		}
		
		public EmptyFilterConfig(ServletContext sc){
			this.sc = sc;
		}

		public String getFilterName() {
	        return name;
        }

		public ServletContext getServletContext() {
	        return sc;
        }

		public String getInitParameter(String name) {
	        return null;
        }

		@SuppressWarnings("rawtypes")
        public Enumeration getInitParameterNames() {
	        return EmptyEnumeration.INSTANCE;
        }
	}
	
	public static final class NotFoundFilterChain implements FilterChain{
		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
	        ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
	}
}
