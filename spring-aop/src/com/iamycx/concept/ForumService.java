package com.iamycx.concept;

public class ForumService {
	private TransactionManager transManager;
	private PerformanceMonitor pmonitor;
	private TopicDao topicDao;
	private ForumDao forumDao;

	public void removeTopic(int topicId) {
		pmonitor.start();
		transManager.beginTransaction();

		topicDao.removeTopic(topicId);

		transManager.endTransaction();
		pmonitor.end();
	}
	public void CreateForum(Forum forum) {
		pmonitor.start();
		transManager.beginTransaction();
		
		forumDao.create(forum);
		
		transManager.endTransaction();
		pmonitor.end();
	}
}
