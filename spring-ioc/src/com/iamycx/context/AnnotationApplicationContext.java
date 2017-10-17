package com.iamycx.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.iamycx.Car;

public class AnnotationApplicationContext {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(Beans.class);
		Car car = ctx.getBean("car1",Car.class);
	}

}
