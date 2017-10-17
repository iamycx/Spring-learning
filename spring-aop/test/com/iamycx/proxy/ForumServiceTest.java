package com.iamycx.proxy;

import java.lang.reflect.Proxy;

public class ForumServiceTest {

	public static void main(String[] args) {
		// // 业务类正常编码的测试
		// ForumService forumService = new ForumServiceImpl();
		// forumService.removeForum(10);
		// forumService.removeTopic(1012);
		//
		// // 使用JDK动态代理
		// ForumService target = new ForumServiceImpl();
		// PerformanceHandler handler = new PerformanceHandler(target);
		// ForumService proxy = (ForumService) Proxy.newProxyInstance(target
		// .getClass().getClassLoader(),
		// target.getClass().getInterfaces(), handler);
		// proxy.removeForum(10);
		// proxy.removeTopic(1012);

		// 使用CGLib动态代理
		CglibProxy proxy = new CglibProxy();
		ForumService forumService = (ForumService) proxy
				.getProxy(ForumServiceImpl.class);
		forumService.removeForum(10);
		forumService.removeTopic(1023);

	}
}