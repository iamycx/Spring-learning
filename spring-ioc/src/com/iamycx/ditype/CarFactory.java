package com.iamycx.ditype;

public class CarFactory {

	public Car createHongQiCar(){
		   Car car = new Car();
		   car.setBrand("∫Ï∆ÏCA72");
		   return car;
	   }
	   
	   public static Car createCar(){
		   Car car = new Car();
		   return car;
	   }
}
