package com.iamycx.conf;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaConfigTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConf.class);
		LogonService logonService = ctx.getBean(LogonService.class);
		logonService.printHelllo();
	}

}
