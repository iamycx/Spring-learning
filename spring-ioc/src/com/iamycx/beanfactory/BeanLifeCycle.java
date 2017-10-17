package com.iamycx.beanfactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.iamycx.Car;

public class BeanLifeCycle {
	private static void LifeCycleInBeanFactory() {
		Resource res = new ClassPathResource("com/iamycx/beanfactory/beans.xml");
		BeanFactory bf = new XmlBeanFactory(res);
		// InstantiationAwareBeanPostProcessor
		((ConfigurableBeanFactory) bf)
				.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
		// BeanPostProcessor
		((ConfigurableBeanFactory) bf)
				.addBeanPostProcessor(new MyBeanPostProcessor());
		Car car1 = (Car) bf.getBean("car1");
		car1.introduce();
		car1.setColor("ºìÉ«");
		Car car2 = bf.getBean("car1", Car.class);
		car2.introduce();
		((XmlBeanFactory) bf).destroySingletons();
	}

	public static void main(String[] args) {
		LifeCycleInBeanFactory();
	}
}
