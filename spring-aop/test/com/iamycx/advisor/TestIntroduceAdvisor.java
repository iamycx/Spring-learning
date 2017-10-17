package com.iamycx.advisor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iamycx.introduce.Monitorable;
import com.iamycx.proxy.ForumService;

public class TestIntroduceAdvisor {
	public static void main(String[] args) {
		String configPath = "com/baobaotao/advisor/beans.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
        ForumService forumService = (ForumService)ctx.getBean("forumService");
        forumService.removeForum(10);
        Monitorable moniterable = (Monitorable)forumService;
        moniterable.setMonitorActive(true);
        forumService.removeForum(10);
	}
}
