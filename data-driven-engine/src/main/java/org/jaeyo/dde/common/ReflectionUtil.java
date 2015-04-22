package org.jaeyo.dde.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

	public static <T> T invokePrivateMethod(Class<?> clazz, String methodName, Class<T> returnType, Object... args) {
		Class<?>[] parameters = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			parameters[i] = args[i].getClass();
		} // for i
		return invokePrivateMethod(clazz, methodName, returnType, parameters, args);
	} // oinvokeMethod
	
	public static <T> T invokePrivateMethod(Object instance, Class<?> clazz, String methodName, Class<T> returnType, Object... args) {
		if(args==null)
			return invokePrivateMethod(instance, clazz, methodName, returnType, null, null);
		
		Class<?>[] parameters = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			parameters[i] = args[i].getClass();
		} // for i
		return invokePrivateMethod(instance, clazz, methodName, returnType, parameters, args);
	} // oinvokeMethod

	public static <T> T invokePrivateMethod(Class<?> clazz, String methodName, Class<T> returnType, Class<?>[] argsClazz, Object[] args) {
		try {
			Object obj = clazz.newInstance();
			return invokePrivateMethod(obj, clazz, methodName, returnType, argsClazz, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} // catch
	} // oinvokeMethod
	
	public static <T> T invokePrivateMethod(Object instance, Class<?> clazz, String methodName, Class<T> returnType, Class<?>[] argsClazz, Object[] args) {
		try {
			if(argsClazz==null && args==null){
				Method method = clazz.getDeclaredMethod(methodName);
				method.setAccessible(true);
				return (T) method.invoke(instance);
			} else{
				Method method = clazz.getDeclaredMethod(methodName, argsClazz);
				method.setAccessible(true);
				return (T) method.invoke(instance, args);
			} //if
		} catch (Exception e) {
			throw new RuntimeException(e);
		} // catch
	} // oinvokeMethod

	public static void setPrivateField(Object clazzInstance, String fieldName, Object fieldValue) {
		try {
			Field field = clazzInstance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(clazzInstance, fieldValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} // catch
	} // setPrivateField
	
	public static <T> T getPrivateField(Object clazzInstance, String fieldName, Class<T> returnType){
		try {
			Field field=clazzInstance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(clazzInstance);
		} catch (Exception e){
			throw new RuntimeException(e);
		} //catch
	} //getPrivateField
} // class