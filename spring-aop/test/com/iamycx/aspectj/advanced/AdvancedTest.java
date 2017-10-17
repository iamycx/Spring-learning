package com.iamycx.aspectj.advanced;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iamycx.aspectj.example.Waiter;

public class AdvancedTest {
	public static void main(String[] args) {
		String configPath = "com/iamycx/aspectj/advanced/beans.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
		Waiter naiveWaiter = (Waiter) ctx.getBean("naiveWaiter");
		Waiter naughtyWaiter = (Waiter) ctx.getBean("naughtyWaiter");
//		naiveWaiter.greetTo("John");
//		naiveWaiter.serveTo("John");
//		naughtyWaiter.greetTo("Tom");
//		naughtyWaiter.serveTo("Tom");
		
        //--通过joinPoint接口访问连接点上下文信息
//		naiveWaiter.greetTo("John");
		
		//--绑定连接点参数
//		((NaiveWaiter)naiveWaiter).smile("John",2);
		
		//--绑定代理对象
		naiveWaiter.greetTo("John");
		
		//--绑定类注解
//		((NaiveWaiter)naiveWaiter).greetTo("John");

		//绑定返回值
//		SmartSeller seller = (SmartSeller) ctx.getBean("seller");
//		seller.sell("Beer","John");	
		
		//绑定异常
//		SmartSeller seller = (SmartSeller) ctx.getBean("seller");
//		seller.checkBill(2);
//		seller.checkBill(1);
	}
}
