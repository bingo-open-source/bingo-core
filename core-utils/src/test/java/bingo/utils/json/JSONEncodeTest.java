/*
 * Copyright 2010 the original author or authors.
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
package bingo.utils.json;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JSONEncodeTest {
	
	@Test
	public void testSimpleValue() throws Exception{
		//null
		assertEquals(JSONWriter.NULL_STRING,encode(null));
		
		//simple type
		assertEquals("\"test\"", encode("test"));
		assertEquals("\"c\"",    encode('c'));
		assertEquals("\"c\"",    encode(new Character('c')));
		assertEquals("true",   encode(true));
		assertEquals("true",   encode(new Boolean(true)));
		assertEquals("false",  encode(false));
		assertEquals("false",  encode(new Boolean(false)));
		assertEquals("100",    encode(100));
		assertEquals("100",    encode(new Integer(100)));
		assertEquals("200",    encode(new Long(200)));
		assertEquals("300.0",  encode(new Float(300)));
		assertEquals("100.1",  encode(100.1f));
		assertEquals("100.1",  encode(new Float(100.1f)));
		assertEquals("100.01", encode(new Double(100.01)));
		assertEquals("100",    encode(new BigDecimal(100)));
		assertEquals("100.1",  encode(new BigDecimal("100.1")));
		assertEquals("100000", encode(new BigInteger("100000")));
		assertEquals("0x01",   encode((byte)1));
		assertEquals("\"RED\"",  encode(Color.RED));
		
		//date type
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2010-11-01 10:10:10");
		assertEquals(String.valueOf(date.getTime()), encode(date));
	}
	
	@Test
	public void testSimpleArray() throws Exception {
		assertEquals("[\"test1\",\"test2\"]", encode(new String[]{"test1","test2"}));
		assertEquals("[\"c\",\"d\"]",         encode(new char[]{'c','d'}));
		assertEquals("[true,false]",      encode(new boolean[]{true,false}));
		assertEquals("[100,101]",         encode(new int[]{100,101}));
		assertEquals("[\"RED\",\"BLUE\"]",    encode(new Color[]{Color.RED,Color.BLUE}));
	}
	
	@Test
	public void testSimpleIterable() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		
		assertEquals("[\"1\",\"2\"]", encode(list));
	}
	
	@Test
	public void testSimpleMap() throws Exception {
		Map<Object,Object> map = new LinkedHashMap<Object, Object>();
		map.put(1, 1);
		map.put("2", "2");
		map.put(3, "3");
		
		assertEquals("{1:1,2:\"2\",3:\"3\"}", encode(map));
	}
	
	@Test
	public void testSimpleBean() throws Exception {
		assertEquals("{name:\"xiaoming\",age:100}", encode(new ParentBean("xiaoming",100)));
		assertEquals("{key:\"key\",name:\"xiaoming\",age:100}", encode(new ChildBean("xiaoming",100,"key")));
	}
	
	@Test
	public void testCyclicBean() throws Exception {
		Category parent = new Category("1");
		Category child1 = new Category("2",parent);
		Category child2 = new Category("3",parent);
		
		parent.getChilds().add(child1);
		parent.getChilds().add(child2);
		
		String json = encode(parent);;
		
		System.out.println(json);
	}
	
	public static void main(String[] args) {
		Object a = new int[]{};
	    System.out.println(a instanceof Object[]);
    }
	
	private static String encode(Object value){
		return JSON.encode(value);
	}
	
	private static enum Color {
		RED,
		BLUE;
	}
	
	static class DateBean{
		private Date  date ;

        public Date getDate() {
        	return date;
        }

        public void setDate(Date date) {
        	this.date = date;
        }
	}
	
	static class ParentBean {
		
		private String name;
		private int    age;
		
		private ParentBean(String name,int age){
			this.name = name;
			this.age  = age;
		}

		public String getName() {
        	return name;
        }

		public int getAge() {
        	return age;
        }
	}
	
	static final class ChildBean extends ParentBean{
		
		private String key;
		
		private ChildBean(String name,int age){
			super(name,age);
		}
		
		private ChildBean(String name,int age,String key){
			this(name,age);
			this.key = key;
		}

		public String getKey() {
        	return key;
        }

		public void setKey(String key) {
        	this.key = key;
        }
	}
	
	static final class Category {

		private String 			id;
		private List<Category> childs = new ArrayList<Category>();
		private Category 		parent;
		
		public Category(String id) {
			this.id = id;
        }
		
		public Category(String id,Category parent) {
			this.id     = id;
			this.parent = parent;
        }

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public List<Category> getChilds() {
			return childs;
		}

		public void setChilds(List<Category> childs) {
			this.childs = childs;
		}

		public Category getParent() {
			return parent;
		}

		public void setParent(Category parent) {
			this.parent = parent;
		}
	}
}
