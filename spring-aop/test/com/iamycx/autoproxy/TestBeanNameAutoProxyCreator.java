package com.iamycx.autoproxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iamycx.advisor.Seller;
import com.iamycx.advisor.Waiter;

public class TestBeanNameAutoProxyCreator {
	public static void main(String[] args) {
		String configPath = "com/iamycx/autoproxy/beans.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
		Waiter waiter = (Waiter) ctx.getBean("waiter");
		Seller seller = (Seller) ctx.getBean("seller");
		waiter.serveTo("John");
		waiter.greetTo("John");
		seller.greetTo("Tom");
	}
}
