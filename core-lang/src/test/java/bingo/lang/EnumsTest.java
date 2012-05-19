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

import static org.junit.Assert.*;

import org.junit.Test;

import bingo.lang.exceptions.InvalidValueException;

@SuppressWarnings("unused")
public class EnumsTest {

	@Test
	public void testNotValued() {
		assertEquals("A", Enums.getValue(Enum1.A));
		assertEquals("B", Enums.getValue(Enum1.B));
		
		assertSame(Enum1.A,Enums.valueOf(Enum1.class, "A"));
		assertSame(Enum1.B,Enums.valueOf(Enum1.class, "B"));
		
		
		try {
	        Enums.valueOf(Enum1.class, "C");
	        
	        fail("should throw InvaidValueException");
        } catch (InvalidValueException e) {
        	
        }
	}
	
	@Test
	public void testIsValued() {
		assertEquals(1, Enums.getValue(Enum2.A));
		assertEquals(2, Enums.getValue(Enum2.B));
		
		assertSame(Enum2.A,Enums.valueOf(Enum2.class, "1"));
		assertSame(Enum2.B,Enums.valueOf(Enum2.class, "2"));
		assertSame(Enum2.A,Enums.valueOf(Enum2.class, 1));
		assertSame(Enum2.B,Enums.valueOf(Enum2.class, 2));
		assertSame(Enum2.A,Enums.valueOf(Enum2.class, new Integer(1)));
		assertSame(Enum2.B,Enums.valueOf(Enum2.class, new Integer(2)));
		assertSame(Enum2.A,Enums.valueOf(Enum2.class, (byte)1));
		assertSame(Enum2.A,Enums.valueOf(Enum2.class, (short)1));
		assertSame(Enum2.B,Enums.valueOf(Enum2.class, (short)2));
		assertSame(Enum2.A,Enums.valueOf(Enum2.class, (long)1));
		assertSame(Enum2.B,Enums.valueOf(Enum2.class, (long)2));		
		assertSame(Enum2.A,Enums.valueOf(Enum2.class, '1'));
		assertSame(Enum2.B,Enums.valueOf(Enum2.class, '2'));
		
		try {
	        Enums.valueOf(Enum1.class, 3);
	        fail("should throw InvaidValueException");
        } catch (InvalidValueException e) {
        	
        }
	}
	
	private enum Enum1 {
		A,
		B
	}
	
	private enum Enum2 {
		A(1),
		B(2);
		
		private int value;
		
		Enum2(int value){
			this.value = value;
		}
	}
	
}
