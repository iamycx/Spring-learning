package com.iamycx.aspectj.example;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class AspectjProxyTest {
	public static void main(String[] args) {
		Waiter target = new NaiveWaiter();
		AspectJProxyFactory factory = new AspectJProxyFactory();
		factory.setTarget(target);
		factory.addAspect(PreGreetingAspect.class);
		Waiter proxy = factory.getProxy();
		proxy.greetTo("John");
		proxy.serveTo("John");
	}
}
