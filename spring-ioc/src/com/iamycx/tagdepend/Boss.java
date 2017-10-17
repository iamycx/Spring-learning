package com.iamycx.tagdepend;

public class Boss {
	private String carId;

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}
    @Override
    public String toString() {
    	return "carId:"+carId;
    }
}
