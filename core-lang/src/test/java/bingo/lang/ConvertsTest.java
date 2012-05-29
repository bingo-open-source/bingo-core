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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import static org.junit.Assert.*;

import bingo.lang.testing.Df;
import bingo.lang.testing.Perf;
import bingo.lang.testing.junit.ConcurrentTestCase;

public class ConvertsTest extends ConcurrentTestCase {

	@Test
	public void testSimplePerformanceComparsion(){
		Perf.run("Handcode.parse", new Runnable() {
			public void run() {
				Integer.parseInt("100");
			}
		},1000000);		
		
		Perf.run("Convert.toType", new Runnable() {
			public void run() {
				Converts.convert("100",Integer.class);
			}
		},1000000);
		
		final TesterBean bean = Df.fillBean(new TesterBean());
		
		Perf.run("Converts.bean", new Runnable() {
			public void run() {
				Converts.convert(bean,Map.class);
			}
		},100000);
	}
	
	@Test
	public void testToPrimitiveClassObject(){
		assertTrue(0 == Converts.toPrimitive(null,Integer.TYPE));
		assertTrue(36.5d == Converts.toPrimitive(36.5,Double.TYPE));
	}
	
	static final class TesterBean  {
		
		private   String   id   = UUID.randomUUID().toString();
		private   String   name = UUID.randomUUID().toString();;
		public    int      age;
		protected Date     birthday;
		private   boolean enabled;
		private   boolean isDisabled;
		private   Boolean  isValid;
		private   Boolean  invalid;
		
		private String stringAttribute1;
		private String stringAttribute2;
		private String stringAttribute3;
		private String stringAttribute4;
		private String stringAttribute5;
		private String stringAttribute6;
		private String stringAttribute7;
		private String stringAttribute8;
		private String stringAttribute9;
		private String stringAttribute10;
		
		private Integer integerAttribute1;
		private Integer integerAttribute2;
		private Integer integerAttribute3;
		private Integer integerAttribute4;
		private Integer integerAttribute5;
		private Integer integerAttribute6;
		private Integer integerAttribute7;
		private Integer integerAttribute8;
		private Integer integerAttribute9;
		private Integer integerAttribute10;
		
		private int intAttribute1;
		private int intAttribute2;
		private int intAttribute3;
		private int intAttribute4;
		private int intAttribute5;
		
		private BigDecimal decimalAttribute1;
		private BigDecimal decimalAttribute2;
		private BigDecimal decimalAttribute3;
		private BigDecimal decimalAttribute4;
		private BigDecimal decimalAttribute5;
		private BigDecimal decimalAttribute6;
		private BigDecimal decimalAttribute7;
		private BigDecimal decimalAttribute8;
		private BigDecimal decimalAttribute9;
		private BigDecimal decimalAttribute10;

		public String getId() {
        	return id;
        }

		public void setId(String id) {
        	this.id = id;
        }

		public String getName() {
        	return name;
        }

		public void setName(String name) {
        	this.name = name;
        }

		public Date getBirthday() {
        	return birthday;
        }

		public void setBirthday(Date birthday) {
        	this.birthday = birthday;
        }
		
		public boolean isEnabled() {
        	return enabled;
        }

		public void setEnabled(boolean enabled) {
        	this.enabled = enabled;
        }
		
		public Boolean getInvalid() {
        	return invalid;
        }

		public void setInvalid(Boolean invalid) {
        	this.invalid = invalid;
        }
		
		public boolean isDisabled() {
        	return isDisabled;
        }

		public void setDisabled(boolean isDisabled) {
        	this.isDisabled = isDisabled;
        }
		
		public Boolean isValid() {
        	return isValid;
        }

		public void setValid(Boolean isValid) {
        	this.isValid = isValid;
        }

		public String getStringAttribute1() {
        	return stringAttribute1;
        }

		public void setStringAttribute1(String stringAttribute1) {
        	this.stringAttribute1 = stringAttribute1;
        }

		public String getStringAttribute2() {
        	return stringAttribute2;
        }

		public void setStringAttribute2(String stringAttribute2) {
        	this.stringAttribute2 = stringAttribute2;
        }

		public String getStringAttribute3() {
        	return stringAttribute3;
        }

		public void setStringAttribute3(String stringAttribute3) {
        	this.stringAttribute3 = stringAttribute3;
        }

		public String getStringAttribute4() {
        	return stringAttribute4;
        }

		public void setStringAttribute4(String stringAttribute4) {
        	this.stringAttribute4 = stringAttribute4;
        }

		public String getStringAttribute5() {
        	return stringAttribute5;
        }

		public void setStringAttribute5(String stringAttribute5) {
        	this.stringAttribute5 = stringAttribute5;
        }

		public String getStringAttribute6() {
        	return stringAttribute6;
        }

		public void setStringAttribute6(String stringAttribute6) {
        	this.stringAttribute6 = stringAttribute6;
        }

		public String getStringAttribute7() {
        	return stringAttribute7;
        }

		public void setStringAttribute7(String stringAttribute7) {
        	this.stringAttribute7 = stringAttribute7;
        }

		public String getStringAttribute8() {
        	return stringAttribute8;
        }

		public void setStringAttribute8(String stringAttribute8) {
        	this.stringAttribute8 = stringAttribute8;
        }

		public String getStringAttribute9() {
        	return stringAttribute9;
        }

		public void setStringAttribute9(String stringAttribute9) {
        	this.stringAttribute9 = stringAttribute9;
        }

		public String getStringAttribute10() {
        	return stringAttribute10;
        }

		public void setStringAttribute10(String stringAttribute10) {
        	this.stringAttribute10 = stringAttribute10;
        }

		public Integer getIntegerAttribute1() {
        	return integerAttribute1;
        }

		public void setIntegerAttribute1(Integer integerAttribute1) {
        	this.integerAttribute1 = integerAttribute1;
        }

		public Integer getIntegerAttribute2() {
        	return integerAttribute2;
        }

		public void setIntegerAttribute2(Integer integerAttribute2) {
        	this.integerAttribute2 = integerAttribute2;
        }

		public Integer getIntegerAttribute3() {
        	return integerAttribute3;
        }

		public void setIntegerAttribute3(Integer integerAttribute3) {
        	this.integerAttribute3 = integerAttribute3;
        }

		public Integer getIntegerAttribute4() {
        	return integerAttribute4;
        }

		public void setIntegerAttribute4(Integer integerAttribute4) {
        	this.integerAttribute4 = integerAttribute4;
        }

		public Integer getIntegerAttribute5() {
        	return integerAttribute5;
        }

		public void setIntegerAttribute5(Integer integerAttribute5) {
        	this.integerAttribute5 = integerAttribute5;
        }

		public Integer getIntegerAttribute6() {
        	return integerAttribute6;
        }

		public void setIntegerAttribute6(Integer integerAttribute6) {
        	this.integerAttribute6 = integerAttribute6;
        }

		public Integer getIntegerAttribute7() {
        	return integerAttribute7;
        }

		public void setIntegerAttribute7(Integer integerAttribute7) {
        	this.integerAttribute7 = integerAttribute7;
        }

		public Integer getIntegerAttribute8() {
        	return integerAttribute8;
        }

		public void setIntegerAttribute8(Integer integerAttribute8) {
        	this.integerAttribute8 = integerAttribute8;
        }

		public Integer getIntegerAttribute9() {
        	return integerAttribute9;
        }

		public void setIntegerAttribute9(Integer integerAttribute9) {
        	this.integerAttribute9 = integerAttribute9;
        }

		public Integer getIntegerAttribute10() {
        	return integerAttribute10;
        }

		public void setIntegerAttribute10(Integer integerAttribute10) {
        	this.integerAttribute10 = integerAttribute10;
        }

		public int getIntAttribute1() {
        	return intAttribute1;
        }

		public void setIntAttribute1(int intAttribute1) {
        	this.intAttribute1 = intAttribute1;
        }

		public int getIntAttribute2() {
        	return intAttribute2;
        }

		public void setIntAttribute2(int intAttribute2) {
        	this.intAttribute2 = intAttribute2;
        }

		public int getIntAttribute3() {
        	return intAttribute3;
        }

		public void setIntAttribute3(int intAttribute3) {
        	this.intAttribute3 = intAttribute3;
        }

		public int getIntAttribute4() {
        	return intAttribute4;
        }

		public void setIntAttribute4(int intAttribute4) {
        	this.intAttribute4 = intAttribute4;
        }

		public int getIntAttribute5() {
        	return intAttribute5;
        }

		public void setIntAttribute5(int intAttribute5) {
        	this.intAttribute5 = intAttribute5;
        }

		public BigDecimal getDecimalAttribute1() {
        	return decimalAttribute1;
        }

		public void setDecimalAttribute1(BigDecimal decimalAttribute1) {
        	this.decimalAttribute1 = decimalAttribute1;
        }

		public BigDecimal getDecimalAttribute2() {
        	return decimalAttribute2;
        }

		public void setDecimalAttribute2(BigDecimal decimalAttribute2) {
        	this.decimalAttribute2 = decimalAttribute2;
        }

		public BigDecimal getDecimalAttribute3() {
        	return decimalAttribute3;
        }

		public void setDecimalAttribute3(BigDecimal decimalAttribute3) {
        	this.decimalAttribute3 = decimalAttribute3;
        }

		public BigDecimal getDecimalAttribute4() {
        	return decimalAttribute4;
        }

		public void setDecimalAttribute4(BigDecimal decimalAttribute4) {
        	this.decimalAttribute4 = decimalAttribute4;
        }

		public BigDecimal getDecimalAttribute5() {
        	return decimalAttribute5;
        }

		public void setDecimalAttribute5(BigDecimal decimalAttribute5) {
        	this.decimalAttribute5 = decimalAttribute5;
        }

		public BigDecimal getDecimalAttribute6() {
        	return decimalAttribute6;
        }

		public void setDecimalAttribute6(BigDecimal decimalAttribute6) {
        	this.decimalAttribute6 = decimalAttribute6;
        }

		public BigDecimal getDecimalAttribute7() {
        	return decimalAttribute7;
        }

		public void setDecimalAttribute7(BigDecimal decimalAttribute7) {
        	this.decimalAttribute7 = decimalAttribute7;
        }

		public BigDecimal getDecimalAttribute8() {
        	return decimalAttribute8;
        }

		public void setDecimalAttribute8(BigDecimal decimalAttribute8) {
        	this.decimalAttribute8 = decimalAttribute8;
        }

		public BigDecimal getDecimalAttribute9() {
        	return decimalAttribute9;
        }

		public void setDecimalAttribute9(BigDecimal decimalAttribute9) {
        	this.decimalAttribute9 = decimalAttribute9;
        }

		public BigDecimal getDecimalAttribute10() {
        	return decimalAttribute10;
        }

		public void setDecimalAttribute10(BigDecimal decimalAttribute10) {
        	this.decimalAttribute10 = decimalAttribute10;
        }
	}
}
