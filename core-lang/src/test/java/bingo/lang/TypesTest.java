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

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import bingo.lang.reflect.testbed.Foo;
import bingo.lang.reflect.testbed.GenericParent;
import bingo.lang.reflect.testbed.GenericTypeHolder;
import bingo.lang.reflect.testbed.StringParameterizedChild;
import bingo.lang.testing.Perf;

@SuppressWarnings( { "unchecked", "unused" })
public class TypesTest {
	
	public static class TypesTestGeneric<B> {
		public interface This<K, V> {
		}

		public class That<K, V> implements This<K, V> {
		}

		public interface And<K, V> extends This<Number, Number> {
		}

		public class The<K, V> extends That<Number, Number> implements And<String, String> {
		}

		public class Other<T> implements This<String, T> {
		}

		public class Thing<Q> extends Other<B> {
		}

		public class Tester implements This<String, B> {
		}

		public This<String, String> dis;

		public That<String, String> dat;

		public The<String, String> da;

		public Other<String> uhder;

		public Thing ding;

		public TypesTestGeneric<String>.Tester tester;

		public Tester tester2;

		public TypesTestGeneric<String>.That<String, String> dat2;

		public TypesTestGeneric<Number>.That<String, String> dat3;

		public Comparable<? extends Integer>[] intWildcardComparable;

		public static Comparable<String> stringComparable;

		public static Comparable<URI> uriComparable;

		public static Comparable<Integer> intComparable;

		public static Comparable<Long> longComparable;

		public static URI uri;
	}

    @Test
    public void testGetTypeArguments() throws Exception {
		Map<TypeVariable<?>, Type> typeVarAssigns;
		TypeVariable<?> treeSetTypeVar;
		Type typeArg;

		typeVarAssigns = Types.getTypeArguments(Integer.class, Comparable.class);
		treeSetTypeVar = Comparable.class.getTypeParameters()[0];
		Assert.assertTrue("Type var assigns for Comparable from Integer: " + typeVarAssigns, typeVarAssigns.containsKey(treeSetTypeVar));
		typeArg = typeVarAssigns.get(treeSetTypeVar);
		Assert.assertEquals("Type argument of Comparable from Integer: " + typeArg, Integer.class, typeVarAssigns.get(treeSetTypeVar));

		typeVarAssigns = Types.getTypeArguments(int.class, Comparable.class);
		treeSetTypeVar = Comparable.class.getTypeParameters()[0];
		Assert.assertTrue("Type var assigns for Comparable from int: " + typeVarAssigns, typeVarAssigns.containsKey(treeSetTypeVar));
		typeArg = typeVarAssigns.get(treeSetTypeVar);
		Assert.assertEquals("Type argument of Comparable from int: " + typeArg, Integer.class, typeVarAssigns.get(treeSetTypeVar));

		Collection<Integer> col = Arrays.asList(new Integer[0]);
		typeVarAssigns = Types.getTypeArguments(List.class, Collection.class);
		treeSetTypeVar = Comparable.class.getTypeParameters()[0];
		Assert.assertFalse("Type var assigns for Collection from List: " + typeVarAssigns, typeVarAssigns.containsKey(treeSetTypeVar));

		typeVarAssigns = Types.getTypeArguments(AAAClass.BBBClass.class, AAClass.BBClass.class);
		Assert.assertTrue(typeVarAssigns.size() == 2);
		Assert.assertEquals(String.class, typeVarAssigns.get(AAClass.class.getTypeParameters()[0]));
		Assert.assertEquals(String.class, typeVarAssigns.get(AAClass.BBClass.class.getTypeParameters()[0]));
    }	

	@Test
	public void testGetTypeArgument() throws Exception {
		assertEquals(Object.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list0").getGenericType()));
		assertEquals(String.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list1").getGenericType()));
		assertEquals(List.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list2").getGenericType()));
		assertEquals(List.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list3").getGenericType()));
		assertEquals(Map.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list4").getGenericType()));
		assertEquals(Object.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list5").getGenericType()));
		assertEquals(Object.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list6").getGenericType()));
		assertEquals(StringChild.class, Types.getActualTypeArgument(GenericContainer1.class.getDeclaredField("list5").getGenericType()));
		assertEquals(StringChild.class, Types.getActualTypeArgument(GenericContainer1.class.getDeclaredField("list6").getGenericType()));
		assertEquals(Object.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list7").getGenericType()));
		assertEquals(String[].class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("list8").getGenericType()));
		assertEquals(Object.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("field0").getGenericType()));
		assertEquals(Object.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("child1").getGenericType()));
		assertEquals(String.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("child2").getGenericType()));
		assertEquals(StringChild.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("child3").getGenericType()));
		assertEquals(Object.class, Types.getActualTypeArgument(GenericContainer.class.getDeclaredField("child4").getGenericType()));
	}
	
    @Test
    public void testGetRawType() throws SecurityException, NoSuchFieldException {
		Type stringParentFieldType = GenericTypeHolder.class.getDeclaredField("stringParent").getGenericType();
		Type integerParentFieldType = GenericTypeHolder.class.getDeclaredField("integerParent").getGenericType();
		Type foosFieldType = GenericTypeHolder.class.getDeclaredField("foos").getGenericType();
		
		Type genericParentT = GenericParent.class.getTypeParameters()[0];
		Assert.assertEquals(GenericParent.class, Types.getRawType(stringParentFieldType, null));
		Assert.assertEquals(GenericParent.class, Types.getRawType(integerParentFieldType, null));
		Assert.assertEquals(List.class, Types.getRawType(foosFieldType, null));
		Assert.assertEquals(String.class, Types.getRawType(genericParentT, StringParameterizedChild.class));
		Assert.assertEquals(String.class, Types.getRawType(genericParentT, stringParentFieldType));
		Assert.assertEquals(Foo.class, Types.getRawType(Iterable.class.getTypeParameters()[0], foosFieldType));
		Assert.assertEquals(Foo.class, Types.getRawType(List.class.getTypeParameters()[0], foosFieldType));
		Assert.assertNull(Types.getRawType(genericParentT, GenericParent.class));
		Assert.assertEquals(GenericParent[].class, Types.getRawType(GenericTypeHolder.class.getDeclaredField("barParents").getGenericType(), null));
    }	

    @Ignore
	@Test
	public void testGetTypeArgumentPerformance() throws Exception {
		final Type genericType = GenericContainer.class.getDeclaredField("child4").getGenericType();

		Perf.run("Types.getTypeArgument", new Runnable() {
			public void run() {
				Types.getActualTypeArgument(genericType);
			}
		}, 1000000);
	}
	

	private static class GenericContainer<T> {

		private List<?> list0;

		private List<String> list1;

		private List<List<?>> list2;

		private List<List<String>> list3;

		private List<Map<?, ?>> list4;

		private List<T> list5;

		private List<? extends T> list6;

		private List list7;

		private List<String[]> list8;

		private T field0;

		private String field1;

		private StringChild child0;

		private GenericParent<?> child1;

		private GenericParent<String> child2;

		private GenericContainer1 child3;

		private StringChildI child4;
		
		private Map<String, ?> map1;

	}

	private static class GenericContainer1<T extends StringChild> {

		private List<T> list5;

		private List<? extends T> list6;

		private T field0;
	}

	private static interface IGenericParent<T> {

	}

	private static class StringChild extends GenericParent<String> {

	}
	
	private static class StringChildChild extends StringChild {
		
	}

	private static class StringChildI implements IGenericParent<String> {

	}
}

class AAClass<T> {

	public class BBClass<S> {
	}
}

class AAAClass extends AAClass<String> {
	public class BBBClass extends BBClass<String> {
	}
}

@SuppressWarnings("unchecked")
class AClass extends AAClass<String>.BBClass<Number> {

	public AClass(AAClass<String> enclosingInstance) {
		enclosingInstance.super();
	}

	public class BClass<T> {
	}

	public class CClass<T> extends BClass {
	}

	public class DClass<T> extends CClass<T> {
	}

	public class EClass<T> extends DClass {
	}

	public class FClass extends EClass<String> {
	}

	public class GClass<T extends BClass<? extends T> & AInterface<AInterface<? super T>>> {
	}

	public BClass<Number> bClass;

	public CClass<? extends String> cClass;

	public DClass<String> dClass;

	public EClass<String> eClass;

	public FClass fClass;

	public GClass gClass;

	public interface AInterface<T> {
	}
}
