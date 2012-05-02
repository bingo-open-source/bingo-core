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

import static bingo.lang.asm.Opcodes.AALOAD;
import static bingo.lang.asm.Opcodes.ACC_PUBLIC;
import static bingo.lang.asm.Opcodes.ACC_VARARGS;
import static bingo.lang.asm.Opcodes.ACONST_NULL;
import static bingo.lang.asm.Opcodes.ALOAD;
import static bingo.lang.asm.Opcodes.ARETURN;
import static bingo.lang.asm.Opcodes.ASTORE;
import static bingo.lang.asm.Opcodes.ATHROW;
import static bingo.lang.asm.Opcodes.BIPUSH;
import static bingo.lang.asm.Opcodes.CHECKCAST;
import static bingo.lang.asm.Opcodes.DUP;
import static bingo.lang.asm.Opcodes.GETFIELD;
import static bingo.lang.asm.Opcodes.ILOAD;
import static bingo.lang.asm.Opcodes.INVOKESPECIAL;
import static bingo.lang.asm.Opcodes.INVOKESTATIC;
import static bingo.lang.asm.Opcodes.INVOKEVIRTUAL;
import static bingo.lang.asm.Opcodes.NEW;
import static bingo.lang.asm.Opcodes.PUTFIELD;
import static bingo.lang.asm.Opcodes.RETURN;
import static bingo.lang.asm.Opcodes.V1_5;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import bingo.lang.asm.ClassWriter;
import bingo.lang.asm.Label;
import bingo.lang.asm.MethodVisitor;
import bingo.lang.asm.Opcodes;
import bingo.lang.asm.Type;
import bingo.lang.exceptions.ReflectException;

public abstract class ReflectAccessor {
    
    private static final String CLASS_NAME = ReflectAccessor.class.getName().replaceAll("\\.", "/");
    
	Field[]  fields;
	Method[] methods;
	
	public abstract Object newInstance();

	public abstract Object invokeMethod(Object object, int methodIndex, Object... args);
	
	public abstract void setField(Object object, int fieldIndex, Object value);
	
	public abstract Object getField(Object object, int fieldIndex);

    int getMethodIndex(Method method) {
        for (int i = 0, n = methods.length; i < n; i++) {
            if(method.equals(methods[i])){
                return i;
            }
        }
        return -1;
    }
    
    int getFieldIndex(Field field){
        for (int i = 0, n = fields.length; i < n; i++) {
            if(field.equals(fields[i])){
                return i;
            }
        }
        return -1;
    }
    
    static ReflectAccessor createFor(Class<?> type){
        ReflectLoader loader = new ReflectLoader(type.getClassLoader());
        
        String typeClassName      = type.getName();
        String accessorClassName  = typeClassName + "$bingo_lang_accessor";
        Class<?> accessorClass    = null;
        
        try {
            accessorClass = loader.loadClass(accessorClassName);
        } catch (ClassNotFoundException ignored) {
        }
        
        Method[] methods = getDeclaredMethods(type);
        Field[]  fields  = getDelaredFields(type);
        
        if (accessorClass == null) {
            String accessorClassNameInternal = accessorClassName.replace('.', '/');
            String typeClassNameInternal     = typeClassName.replace('.', '/');

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            
            defineAccessorConstructor(accessorClassNameInternal,cw);
            
            defineNewInstance(typeClassNameInternal,cw);
            
            defineInvokeMethod(typeClassNameInternal,methods, cw);
            
            defineSetField(typeClassNameInternal,fields, cw);
            
            defineGetField(typeClassNameInternal,fields, cw);

            cw.visitEnd();
            
            byte[] data   = cw.toByteArray();
            accessorClass = loader.defineClass(accessorClassName, data);
        }
        
        try {
            ReflectAccessor accessor = (ReflectAccessor)accessorClass.newInstance();
            accessor.methods = methods;
            accessor.fields  = fields;
            return accessor;
        } catch (Exception ex) {
            throw new ReflectException(ex,"Error constructing reflect accessor class: {0}",accessorClassName);
        }
    }
    
    private static void defineAccessorConstructor(String accessorClassNameInternal,ClassWriter cw){
        cw.visit(V1_5, ACC_PUBLIC, accessorClassNameInternal, null, CLASS_NAME, null);
        
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, CLASS_NAME, "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
    
    private static void defineNewInstance(String classNameInternal,ClassWriter cw){
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "newInstance", "()Ljava/lang/Object;", null, null);
        
        mv.visitCode();
        mv.visitTypeInsn(NEW, classNameInternal);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL,classNameInternal, "<init>", "()V");  
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
    
    private static void defineInvokeMethod(String classNameInternal,Method[] methods, ClassWriter cw){
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, 
                                          "invokeMethod", 
                                          "(Ljava/lang/Object;I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        mv.visitCode();

        if (methods.length > 0) {
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, classNameInternal);
            mv.visitVarInsn(ASTORE, 4);

            mv.visitVarInsn(ILOAD, 2);
            Label[] labels = new Label[methods.length];
            for (int i = 0;i < labels.length; i++){
                labels[i] = new Label();
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            StringBuilder buffer = new StringBuilder(128);
            for (int i = 0;i < labels.length; i++) {
                mv.visitLabel(labels[i]);
                
                if (i == 0){
                    mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] {classNameInternal}, 0, null);
                }else{
                    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                }
                mv.visitVarInsn(ALOAD, 4);

                buffer.setLength(0);
                buffer.append('(');

                Method method = methods[i];
                Class<?>[] paramTypes = method.getParameterTypes();
                for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
                    mv.visitVarInsn(ALOAD, 3);
                    mv.visitIntInsn(BIPUSH, paramIndex);
                    mv.visitInsn(AALOAD);
                    Type paramType = Type.getType(paramTypes[paramIndex]);
                    switch (paramType.getSort()) {
                        case Type.BOOLEAN:
                            mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
                            break;
                        case Type.BYTE:
                            mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
                            break;
                        case Type.CHAR:
                            mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C");
                            break;
                        case Type.SHORT:
                            mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S");
                            break;
                        case Type.INT:
                            mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
                            break;
                        case Type.FLOAT:
                            mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
                            break;
                        case Type.LONG:
                            mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J");
                            break;
                        case Type.DOUBLE:
                            mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
                            break;
                        case Type.ARRAY:
                            mv.visitTypeInsn(CHECKCAST, paramType.getDescriptor());
                            break;
                        case Type.OBJECT:
                            mv.visitTypeInsn(CHECKCAST, paramType.getInternalName());
                            break;
                    }
                    buffer.append(paramType.getDescriptor());
                }

                buffer.append(')');
                buffer.append(Type.getDescriptor(method.getReturnType()));
                mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, method.getName(), buffer.toString());

                switch (Type.getType(method.getReturnType()).getSort()) {
                    case Type.VOID:
                        mv.visitInsn(ACONST_NULL);
                        break;
                    case Type.BOOLEAN:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
                        break;
                    case Type.BYTE:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                        break;
                    case Type.CHAR:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
                        break;
                    case Type.SHORT:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                        break;
                    case Type.INT:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                        break;
                    case Type.FLOAT:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                        break;
                    case Type.LONG:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                        break;
                    case Type.DOUBLE:
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                        break;
                }

                mv.visitInsn(ARETURN);
            }

            mv.visitLabel(defaultLabel);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Method not found: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(ATHROW);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
    
    private static void defineSetField(String classNameInternal,Field[] fields, ClassWriter cw){
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "setField", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 2);

        if (fields.length > 0) {
            Label[] labels = new Label[fields.length];
            for (int i = 0, n = labels.length; i < n; i++)
                labels[i] = new Label();
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                Field field = fields[i];
                Type fieldType = Type.getType(field.getType());

                mv.visitLabel(labels[i]);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(CHECKCAST, classNameInternal);
                mv.visitVarInsn(ALOAD, 3);

                switch (fieldType.getSort()) {
                    case Type.BOOLEAN:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
                        break;
                    case Type.BYTE:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
                        break;
                    case Type.CHAR:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C");
                        break;
                    case Type.SHORT:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S");
                        break;
                    case Type.INT:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
                        break;
                    case Type.FLOAT:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
                        break;
                    case Type.LONG:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J");
                        break;
                    case Type.DOUBLE:
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
                        break;
                    case Type.ARRAY:
                        mv.visitTypeInsn(CHECKCAST, fieldType.getDescriptor());
                        break;
                    case Type.OBJECT:
                        mv.visitTypeInsn(CHECKCAST, fieldType.getInternalName());
                        break;
                }

                mv.visitFieldInsn(PUTFIELD, classNameInternal, field.getName(), fieldType.getDescriptor());
                mv.visitInsn(RETURN);
            }

            mv.visitLabel(defaultLabel);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Field not found: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(ATHROW);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
    
    private static void defineGetField(String classNameInternal,Field[] fields, ClassWriter cw){
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getField", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 2);

        if (fields.length > 0) {
            Label[] labels = new Label[fields.length];
            for (int i = 0, n = labels.length; i < n; i++){
                labels[i] = new Label();
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                Field field = fields[i];

                mv.visitLabel(labels[i]);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(CHECKCAST, classNameInternal);
                mv.visitFieldInsn(GETFIELD, classNameInternal, field.getName(), Type.getDescriptor(field.getType()));

                Type fieldType = Type.getType(field.getType());
                switch (fieldType.getSort()) {
                case Type.BOOLEAN:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
                    break;
                case Type.BYTE:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                    break;
                case Type.CHAR:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
                    break;
                case Type.SHORT:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                    break;
                case Type.INT:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                    break;
                case Type.FLOAT:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                    break;
                case Type.LONG:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                    break;
                case Type.DOUBLE:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                    break;
                }

                mv.visitInsn(ARETURN);
            }

            mv.visitLabel(defaultLabel);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Field not found: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(ATHROW);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
    
    private static Method[] getDeclaredMethods(Class<?> type){
        ArrayList<Method> methods = new ArrayList<Method>();
        Class<?> nextClass = type;
        
        while (nextClass != Object.class) {
            Method[] declaredMethods = nextClass.getDeclaredMethods();
            
            for (int i = 0, n = declaredMethods.length; i < n; i++) {
                Method method = declaredMethods[i];

                if (Modifier.isPrivate(method.getModifiers())) {
                	continue;
                }
                
                methods.add(method);
            }
            
            nextClass = nextClass.getSuperclass();
        }
        
        return methods.toArray(new Method[methods.size()]);
    }
    
    private static Field[] getDelaredFields(Class<?> type){
        ArrayList<Field> fields = new ArrayList<Field>();
        Class<?> nextClass = type;
        
        while (nextClass != Object.class) {
            Field[] declaredFields = nextClass.getDeclaredFields();
            
            for (int i = 0, n = declaredFields.length; i < n; i++) {
                Field field = declaredFields[i];
                
                if (Modifier.isPrivate(field.getModifiers())) {
                	continue;
                }
                
                fields.add(field);
            }
            
            nextClass = nextClass.getSuperclass();
        }
        
        return fields.toArray(new Field[fields.size()]);
    }
    
    static final class ReflectLoader extends ClassLoader {
    	
    	ReflectLoader (ClassLoader parent) {
    		super(parent);
    	}

    	protected synchronized java.lang.Class<?> loadClass (String name, boolean resolve) throws ClassNotFoundException {
    	    if (name.equals(ReflectAccessor.class.getName())) {
    		    return ReflectAccessor.class;
    		}
    		
    		return super.loadClass(name, resolve);
    	}

    	Class<?> defineClass (String name, byte[] bytes) throws ClassFormatError {
    		try {
    			Method method = ClassLoader.class.getDeclaredMethod("defineClass", 
    			                                                    new Class[] {String.class, byte[].class, int.class,int.class});
    			
    			method.setAccessible(true);
    			
    			return (Class<?>)method.invoke(getParent(), new Object[] {name, bytes, new Integer(0), new Integer(bytes.length)});
    		} catch (Exception ignored) {
    		    //do nothing
    		}
    		
    		return defineClass(name, bytes, 0, bytes.length);
    	}
    }   
}