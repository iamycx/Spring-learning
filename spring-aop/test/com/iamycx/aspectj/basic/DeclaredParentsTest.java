package com.iamycx.aspectj.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iamycx.aspectj.example.Seller;
import com.iamycx.aspectj.example.Waiter;

public class DeclaredParentsTest {
	public static void main(String[] args) {
		String configPath = "com/iamycx/aspectj/basic/beans.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
		Waiter waiter = (Waiter)ctx.getBean("waiter");
		waiter.greetTo("John");
		Seller seller = (Seller)waiter;
		seller.sell("Beer", "John");
	}
}
