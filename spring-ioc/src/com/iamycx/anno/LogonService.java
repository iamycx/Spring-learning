package com.iamycx.anno;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class LogonService implements BeanNameAware{
	@Autowired(required=false)
	private LogDao logDao;
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	public LogDao getLogDao() {
		return logDao;
	}
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void setBeanName(String beanName) {
		System.out.println("beanName:"+beanName);		
	}
	
	public void initMethod1(){
		System.out.println("initMethod1");
	}
	public void initMethod2(){
		System.out.println("initMethod2");
	}
//	@Autowired
//	public void setLogDao(LogDao logDao) {
//		this.logDao = logDao;
//	}
//	
//	@Autowired
//	@Qualifier("userDao")
//	public void setUserDao(UserDao userDao) {
//		System.out.println("auto inject");
//		this.userDao = userDao;
//	}
//	@Autowired
//	public void init(@Qualifier("userDao")UserDao userDao,LogDao logDao){
//		System.out.println("multi param inject");
//		this.userDao = userDao;
//		this.logDao =logDao;
//	}
	
}
