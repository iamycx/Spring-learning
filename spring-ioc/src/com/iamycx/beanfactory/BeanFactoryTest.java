package com.iamycx.beanfactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.iamycx.Car;

public class BeanFactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable{
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:com/iamycx/beanfactory/beans.xml");
		BeanFactory beanFactory = new XmlBeanFactory(resource);
		System.out.println("init BeanFactory");
		
		Car car = beanFactory.getBean("car1",Car.class);
		System.out.println("car bean is ok!!");
	}

}
