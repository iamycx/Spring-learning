package com.iamycx.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable {
		Car car = initByDefaultConst();
		car.introduce();
	}

	private static Car initByDefaultConst() throws Throwable {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Class clazz = loader.loadClass("com.iamycx.reflect.Car");
		
		Constructor constructor = clazz.getDeclaredConstructor((Class[])null);
		Car car = (Car)constructor.newInstance();
		
		Method setBrand = clazz.getMethod("setBrand", String.class);
		setBrand.invoke(car, "BMW730");
		Method setColor = clazz.getMethod("setColor", String.class);
		setColor.invoke(car, "black");
		Method setMaxSpeed = clazz.getMethod("setMaxSpeed", int.class);
		setMaxSpeed.invoke(car, 300);
		
		return car;
	}

}
