package com.iamycx.aspectj.example;

import com.iamycx.anno.NeedTest;

public interface Waiter {
	@NeedTest
	public void greetTo(String clientName);	
	public void serveTo(String clientName);
}
