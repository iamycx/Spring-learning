package com.iamycx.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iamycx.Car;

@Configuration
public class Beans {

	@Bean(name="car")
	public Car buildCar() {
		Car car = new Car();
		car.setBrand("BMW");
		car.setColor("À¶É«");
		car.setMaxSpeed(300);
		return car;
	}
}
