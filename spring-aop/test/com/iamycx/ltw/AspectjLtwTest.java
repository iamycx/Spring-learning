package com.iamycx.ltw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AspectjLtwTest {
	public static void main(String[] args) {
		String configPath = "com/iamycx/ltw/beans.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
		Waiter waiter = ctx.getBean(Waiter.class);
		waiter.greetTo("John");
		waiter.serveTo("John");	
	}
}
