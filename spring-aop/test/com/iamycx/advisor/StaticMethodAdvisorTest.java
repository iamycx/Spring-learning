package com.iamycx.advisor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StaticMethodAdvisorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String configPath = "com/iamycx/advisor/beans.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
		Waiter waiter = (Waiter)ctx.getBean("waiter");
		Seller seller = (Seller)ctx.getBean("seller");
		waiter.greetTo("John");
		waiter.serveTo("John");
		seller.greetTo("John");	
	}

}
