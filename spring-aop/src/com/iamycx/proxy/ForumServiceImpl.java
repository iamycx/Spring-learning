package com.iamycx.proxy;

public class ForumServiceImpl implements ForumService {

	public void removeTopic(int topicId) {
//		PerformanceMonitor.begin("com.baobaotao.proxy.ForumServiceImpl.removeTopic");
		System.out.println("Ä£ÄâÉ¾³ýTopic¼ÇÂ¼:"+topicId);
		try {
			Thread.currentThread().sleep(20);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
//		PerformanceMonitor.end();
	}

	public void removeForum(int forumId) {
//		PerformanceMonitor.begin("com.baobaotao.proxy.ForumServiceImpl.removeForum");
		System.out.println("Ä£ÄâÉ¾³ýForum¼ÇÂ¼:"+forumId);
		try {
			Thread.currentThread().sleep(40);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
//		PerformanceMonitor.end();
	}
}
