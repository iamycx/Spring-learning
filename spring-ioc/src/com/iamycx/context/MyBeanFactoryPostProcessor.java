package com.iamycx.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyBeanFactoryPostProcessor {

	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
		BeanDefinition bd = bf.getBeanDefinition("car1");
		bd.getPropertyValues().addPropertyValue("brand", "ÆæÈðQQ");
		System.out.println("µ÷ÓÃMyBeanFactoryPostProcessor.postProcessBeanFactory()£¡");
	}
}
