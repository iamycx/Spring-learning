package com.iamycx.advice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestThrowAdvice {
	public static void main(String[] args) {
		String configPath = "com/iamycx/advice/beans.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
		ForumService forumService = (ForumService)ctx.getBean("forumService");
		
		try{
			forumService.removeForum(10);
		} catch (Exception e) {}		
		
		try{
			forumService.updateForum(new Forum());
		} catch (Exception e) {}			
	}
}