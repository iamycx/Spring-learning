package com.iamycx.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PrivateCarReflect {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable{

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Class clazz = loader.loadClass("com.iamycx.reflect.PrivateCar");
		PrivateCar pCar = (PrivateCar)clazz.newInstance();
		
		Field colorField = clazz.getDeclaredField("color");
		colorField.setAccessible(true);
		colorField.set(pCar, "À¶É«");
		
		Method dirMethod = clazz.getDeclaredMethod("drive", (Class[])null);
		
		dirMethod.setAccessible(true);
		dirMethod.invoke(pCar, (Object[])null);
	}

}
