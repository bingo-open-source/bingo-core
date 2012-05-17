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
package bingo.lang.cloning;

import org.junit.Test;

import bingo.lang.cloning.beans.Category;
import bingo.lang.cloning.beans.Product;

import static org.junit.Assert.*;

public class ClonesTest {

	@Test
	public void testCyclicRreferencesClone(){
		
		Cloner cloner = new Cloner();
		
		Category category = new Category();
		
		Product product1 = new Product(1, "sku1", "title1");
		Product product2 = new Product(2, "sku1", "title1");
		
		product1.setCategory(category);
		product2.setCategory(category);
		
		category.getProducts().add(product1);
		category.getProducts().add(product2);
		category.getProducts().add(product2);

		Category cloned = cloner.deepClone(category);
		
		assertNotNull(cloned);
		assertNotSame(category, cloned);
		assertEquals(3, cloned.getProducts().size());
		assertEquals(category.getId(), cloned.getId());
		
		for(int i=0;i<cloned.getProducts().size();i++){
			Product clonedProduct   = cloned.getProducts().get(i);
			Product originalProduct = category.getProducts().get(i);
			
			assertEquals(clonedProduct.getId(), originalProduct.getId());
		}
	}
}
