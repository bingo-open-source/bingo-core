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
package bingo.lang.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectType implements Type {
	
	private static ConcurrentHashMap<GenericTypeKey, ReflectType> genericTypeCache = new ConcurrentHashMap<GenericTypeKey, ReflectType>();
	
	private static final int TYPE_TypeVariable = 1;
	private static final int TYPE_ParameterizedType = 2;
	private static final int TYPE_GenericArrayType = 3;
	private static final int TYPE_WildcardType = 4;

	private Type       javaType;

	private Class<?>   ownerClass;

	private int 	    typeFlag;

	private Class<?>   actualClass;

	private Class<?>[] paramClasses;

	private Class<?>   componentClass;

	private Map<TypeVariable<?>, Type> ownerClassTypeVariables;

	private ReflectType(Type type, Class<?> ownerClass) {
		super();
		this.javaType = type;
		this.ownerClass = ownerClass;

		this.ownerClassTypeVariables = new HashMap<TypeVariable<?>, Type>();
		extractTypeVariablesInType(this.ownerClass, this.ownerClassTypeVariables);

		if (this.javaType instanceof TypeVariable<?>) {
			this.typeFlag = TYPE_TypeVariable;

			this.actualClass = getTypeVariableActualClass((TypeVariable<?>) this.javaType);
		} else if (this.javaType instanceof ParameterizedType) {
			this.typeFlag = TYPE_ParameterizedType;

			ParameterizedType pt = (ParameterizedType) this.javaType;

			this.actualClass = getParameterizedTypeActualRawClass(pt);
			this.paramClasses = getParameterizedTypeActualParamClasses(pt);
		} else if (this.javaType instanceof GenericArrayType) {
			this.typeFlag = TYPE_GenericArrayType;

			this.actualClass = evaluateGenericTypeAcualClass(this.javaType);
			this.componentClass = getGenericArrayTypeActualComponentClass((GenericArrayType) this.javaType);
		} else if (this.javaType instanceof WildcardType) {
			this.typeFlag = TYPE_WildcardType;

			this.actualClass = getWildcardTypeActualClass((WildcardType) this.javaType);
		} else
			throw new IllegalArgumentException("unknown type '" + type + "'");
	}

	public Type getJavaType() {
		return javaType;
	}

	public void setJavaType(Type type) {
		this.javaType = type;
	}

	public Class<?> getOwnerClass() {
		return ownerClass;
	}

	public Class<?> getActualClass() {
		return this.actualClass;
	}

	public Class<?>[] getParamClasses() {
		return this.paramClasses;
	}

	public Class<?> getComponentClass() {
		return this.componentClass;
	}

	public boolean isParameterizedType() {
		return TYPE_ParameterizedType == this.typeFlag;
	}

	public boolean isTypeVariable() {
		return TYPE_TypeVariable == this.typeFlag;
	}

	public boolean isGenericArrayType() {
		return TYPE_GenericArrayType == this.typeFlag;
	}

	public boolean isWildcardType() {
		return TYPE_WildcardType == this.typeFlag;
	}

	protected Class<?> getTypeVariableActualClass(TypeVariable<?> typeVariable) {
		Class<?> re = null;

		Type tp = ownerClassTypeVariables.get(typeVariable);

		//找不到，则使用上限类型再次解析
		if (tp == null) {
			Type[] upperBounds = typeVariable.getBounds();
			if (upperBounds != null && upperBounds.length >= 1)
				tp = upperBounds[0];
		}

		re = evaluateGenericTypeAcualClass(tp);

		return re;
	}

	protected Class<?> getGenericArrayTypeActualComponentClass(GenericArrayType gat) {
		Class<?> re = null;

		Type tp = gat.getGenericComponentType();
		re = evaluateGenericTypeAcualClass(tp);

		return re;
	}

	protected Class<?>[] getParameterizedTypeActualParamClasses(ParameterizedType pt) {
		Class<?>[] re = null;

		Type[] tps = pt.getActualTypeArguments();
		if (tps != null) {
			re = new Class<?>[tps.length];
			for (int i = 0; i < tps.length; i++)
				re[i] = evaluateGenericTypeAcualClass(tps[i]);
		}

		return re;
	}

	protected Class<?> getParameterizedTypeActualRawClass(ParameterizedType pt) {
		Type tp = pt.getRawType();

		return evaluateGenericTypeAcualClass(tp);
	}

	protected Class<?> getWildcardTypeActualClass(WildcardType pt) {
		Class<?> re = null;

		//通配符只能使用上限类型
		Type tp = null;
		Type[] upperBounds = pt.getUpperBounds();
		if (upperBounds != null && upperBounds.length >= 1)
			tp = upperBounds[0];

		re = evaluateGenericTypeAcualClass(tp);

		return re;
	}

	protected Class<?> evaluateGenericTypeAcualClass(Type type) {
		Class<?> re = null;

		//找不到对应类型，设置为Object.class
		if (type == null) {
			re = Object.class;
		} else if (type instanceof Class<?>) {
			re = (Class<?>) type;
		} else if (type instanceof WildcardType) {
			re = getWildcardTypeActualClass((WildcardType) type);
		} else if (type instanceof TypeVariable<?>) {
			re = getTypeVariableActualClass((TypeVariable<?>) type);
		} else if (type instanceof ParameterizedType) {
			re = getParameterizedTypeActualRawClass((ParameterizedType) type);
		} else if (type instanceof GenericArrayType) {
			re = getGenericArrayTypeActualComponentClass((GenericArrayType) type);
			re = Array.newInstance(re, 0).getClass();
		} else
			throw new IllegalArgumentException("unknown generic type '" + type + "'");

		return re;
	}

	protected void extractTypeVariablesInType(Type source, Map<TypeVariable<?>, Type> container) {
		if (source == null)
			return;
		else if (source instanceof Class<?>) {
			Class<?> clazz = (Class<?>) source;

			//实现的接口
			Type[] genericInterfaces = clazz.getGenericInterfaces();
			if (genericInterfaces != null) {
				for (Type t : genericInterfaces)
					extractTypeVariablesInType(t, container);
			}

			//父类
			Type genericSuperType = clazz.getGenericSuperclass();
			Class<?> superClass = clazz.getSuperclass();
			while (superClass != null && !Object.class.equals(superClass)) {
				extractTypeVariablesInType(genericSuperType, container);

				genericSuperType = superClass.getGenericSuperclass();
				superClass = superClass.getSuperclass();
			}

			//外部类
			Class<?> outerClass = clazz;
			while (outerClass.isMemberClass()) {
				Type genericOuterType = outerClass.getGenericSuperclass();
				extractTypeVariablesInType(genericOuterType, container);

				outerClass = outerClass.getEnclosingClass();
			}
		} else if (source instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) source;

			if (pt.getRawType() instanceof Class<?>) {
				Type[] actualArgTypes = pt.getActualTypeArguments();
				TypeVariable<?>[] typeVariables = ((Class<?>) pt.getRawType()).getTypeParameters();

				for (int i = 0; i < actualArgTypes.length; i++) {
					TypeVariable<?> tv = typeVariables[i];
					Type tvType = actualArgTypes[i];

					container.put(tv, tvType);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "GenericType [actualClass=" + actualClass + ", paramClasses=" + Arrays.toString(paramClasses) + ", componentClass=" + componentClass
		        + "]";
	}

	public static ReflectType getGenericType(Type type, Class<?> ownerClass) {
		if (type instanceof Class<?>)
			throw new IllegalArgumentException("'" + type.toString() + "' is Class type, no generic type info");

		ReflectType re = null;

		GenericTypeKey key = new GenericTypeKey(type, ownerClass);

		re = genericTypeCache.get(key);
		if (re == null) {
			re = new ReflectType(type, ownerClass);
			genericTypeCache.putIfAbsent(key, re);
		}

		return re;
	}

	protected static class GenericTypeKey {
		private Type 	  type;
		private Class<?> ownerClass;

		public GenericTypeKey(Type type, Class<?> ownerClass) {
			super();
			this.type = type;
			this.ownerClass = ownerClass;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((ownerClass == null) ? 0 : ownerClass.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GenericTypeKey other = (GenericTypeKey) obj;
			if (ownerClass == null) {
				if (other.ownerClass != null)
					return false;
			} else if (!ownerClass.equals(other.ownerClass))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
	}
}
