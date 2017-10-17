package com.iamycx.tagdepend;

public class Car {

	private String color;
	private String brand;
	private double price;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String toString() {
		return "brand:" + brand + "/price:" + price + "/color:" + color;
	}

}
