package com.iamycx.anno;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

public class Test {
	  public static void main(String[] args) throws Throwable{
		 ApplicationContext ctx = new ClassPathXmlApplicationContext("com/iamycx/anno/beans.xml");
		 LogonService logonService = ctx.getBean(LogonService.class);
		 Assert.notNull(logonService);
		 //LogonController controller = ctx.getBean(LogonController.class);
	     Car car1 = ctx.getBean(Car.class);
	     Car car2 = ctx.getBean(Car.class);
	     System.out.println(car1 != car2);
	     
	     ((ClassPathXmlApplicationContext)ctx).destroy();
//	     Thread.currentThread().sleep(10);
	}
}
