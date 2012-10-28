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
package bingo.lang.testing.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import bingo.lang.Named;
import bingo.lang.Strings;

@SuppressWarnings({"unchecked","rawtypes"})
public class ContextualRule implements TestRule {
	
	private final boolean			 runAnnotatedOnly;
	private final ContextualProvider provider;

	public ContextualRule(ContextualProvider<?> provider) {
	    super();
	    this.provider         = provider;
	    this.runAnnotatedOnly = false;
    }
	
	public ContextualRule(ContextualProvider<?> provider,boolean runAnnotatedOnly) {
	    super();
	    this.provider         = provider;
	    this.runAnnotatedOnly = runAnnotatedOnly;
    }

	public Statement apply(final Statement base,final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
		    	if(description.getAnnotation(ContextualIgnore.class) != null){
		    		base.evaluate();
		    		return ;
		    	}
		    	
		    	Contextual contextual = description.getAnnotation(Contextual.class);
		    	
		    	if(runAnnotatedOnly && contextual == null){
		    		base.evaluate();
		    		return;
		    	}
		    	
		    	if(null != contextual && !Strings.isEmpty(contextual.value())){
		    		String qualifier = contextual.value();
		    		for(Object param : provider.params(description)){
		    			if(param instanceof Named && Strings.equalsIgnoreCase(((Named)param).getName(), qualifier)){
				    		try{
				    			provider.beforeTest(description, param);
				    			base.evaluate();
				    		}finally{
				    			provider.afterTest(description,param);
				    		}
		    			}else if(Strings.equalsIgnoreCase(param.toString(), qualifier)){
				    		try{
				    			provider.beforeTest(description, param);
				    			base.evaluate();
				    		}finally{
				    			provider.afterTest(description,param);
				    		}
		    			}
		    		}
		    	}else{
			    	for(Object param : provider.params(description)){
			    		try{
			    			provider.beforeTest(description, param);
			    			base.evaluate();
			    		}finally{
			    			provider.afterTest(description,param);
			    		}
			    	}
		    	}
		    	
		    	provider.finishTests(description);
			}
		};
    }
}
