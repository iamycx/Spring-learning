package com.iamycx.advisor;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

public class GreetingAdvisor extends StaticMethodMatcherPointcutAdvisor {

	public boolean matches(Method arg0, Class<?> arg1) {
		return "greetTo".equals(Method.class);
	}

	public ClassFilter getClassFilter() {
		return new ClassFilter() {
			public boolean matches(Class clazz) {
				return Waiter.class.isAssignableFrom(clazz);
			}
		};

	}
}
