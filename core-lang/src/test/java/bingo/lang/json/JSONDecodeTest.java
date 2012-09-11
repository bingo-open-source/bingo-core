/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.lang.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import bingo.lang.io.IO;
import bingo.lang.resource.Resource;
import bingo.lang.resource.Resources;
import bingo.lang.testing.junit.ConcurrentTestCase;

@SuppressWarnings("unchecked")
public class JSONDecodeTest extends ConcurrentTestCase {
    
    @Test
    public void testDecodeLarge() throws Exception {
        InputStream       stream = Resources.getInputStream("classpath:json/json.json");
        InputStreamReader reader = null;
        try{
            reader = new InputStreamReader(stream);
            JSONObject json = JSON.decode(reader);
            
            assertTrue(json.isArray());
            
            Object[] array = json.array();
            
            assertEquals(2, array.length);
            
            Map<String, Object> map  = (Map<String,Object>)array[1];
            List<Object>        list = null;
            
            assertNotNull(map);
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(list = (List<Object>)map.get("children"));
            assertNotNull(map  = (Map<String,Object>)list.get(0));
            
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(map  = (Map<String,Object>)map.get("replies"));
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(list = (List<Object>)map.get("children"));
            assertNotNull(map  = (Map<String,Object>)list.get(0));    
            
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(map  = (Map<String,Object>)map.get("replies"));
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(list = (List<Object>)map.get("children"));
            assertNotNull(map  = (Map<String,Object>)list.get(0));      
            
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(map  = (Map<String,Object>)map.get("replies"));
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(list = (List<Object>)map.get("children"));
            assertNotNull(map  = (Map<String,Object>)list.get(0)); 
            
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(map  = (Map<String,Object>)map.get("replies"));
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(list = (List<Object>)map.get("children"));
            assertNotNull(map  = (Map<String,Object>)list.get(0)); 
            
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(map  = (Map<String,Object>)map.get("replies"));
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(list = (List<Object>)map.get("children"));
            assertNotNull(map  = (Map<String,Object>)list.get(0));     
            
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(map  = (Map<String,Object>)map.get("replies"));
            assertNotNull(map  = (Map<String,Object>)map.get("data"));
            assertNotNull(list = (List<Object>)map.get("children"));
            assertNotNull(map  = (Map<String,Object>)list.get(0));               
            
        }finally{
            IO.close(stream);
            IO.close(reader);
        }
    }

    @Test
    public void testDecodeEncodeAll() throws Exception {
        
        Resource[] resources = Resources.scan("classpath:json/**/*.json");
        
        for(Resource resource : resources){
            if(resource.exists()){
                System.out.println("-> decoding json '" + resource.getURI().toString() + "'...") ;
                
                InputStream       stream = resource.getInputStream();
                InputStreamReader reader = null;
                
                try{
                    reader = new InputStreamReader(stream);
                    
                    JSONObject json = JSON.decode(reader);
                    
                    assertNotNull(json);
                    
                    String string = JSON.encode(json.value());
                    
                    assertNotNull(string);
                    
                    json = JSON.decode(string);
                    
                    assertNotNull(json);
                }finally{
                    IO.close(reader);
                    IO.close(stream);
                }
            }
        }
    }
    
    @Test
    public void testDecodeList() throws Exception {
    	
    	List<Bean> beans = new ArrayList<Bean>();
    	
    	beans.add(new Bean());
    	beans.add(new Bean());
    	
    	String json = JSON.encode(beans);
    	
    	Bean[] decoded = JSON.decodeToArray(json, Bean.class);
    	
    	assertEquals(2, decoded.length);
    	
    	for(int i=0;i<decoded.length;i++){
    		assertEquals(beans.get(i).name,decoded[i].name);
    	}
    }
    
    @Test
    public void testDecode1(){
    	JSON.decode("[{ id : \"\", fields:[{ id:\"\",attrs:{}}]}]");
    }

    private static class Bean {
    	public String name = UUID.randomUUID().toString();
    }
}
