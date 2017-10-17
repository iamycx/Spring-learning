package com.iamycx.injectfun;
import java.lang.reflect.Method;
import org.springframework.beans.factory.support.MethodReplacer;
public class Boss2 implements MethodReplacer {
	public Object reimplement(Object arg0, Method arg1, Object[] arg2)
			throws Throwable {
		Car car = new Car();
		car.setBrand("√¿»À±™");
		return car;
	}
}
