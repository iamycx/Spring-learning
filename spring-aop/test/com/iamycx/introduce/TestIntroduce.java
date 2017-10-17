package com.iamycx.introduce;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iamycx.advice.ForumService;


public class TestIntroduce {
	public static void main(String[] args) {
		String configPath = "com/iamycx/introduce/beans.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
        ForumService forumService = (ForumService)ctx.getBean("forumService");
        forumService.removeForum(10);
//        forumService.removeTopic(1022);
        Monitorable moniterable = (Monitorable)forumService;
        moniterable.setMonitorActive(true);
        forumService.removeForum(10);
//        forumService.removeTopic(1022); 
	}
}
