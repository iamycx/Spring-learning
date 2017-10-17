package com.iamycx.aspectj.advanced;

import org.aspectj.lang.annotation.Pointcut;

public class TestNamePointcut {
	@Pointcut("within(com.iamycx.aspectj.example.*)")
	private void inPackage(){}
//	
//	@Pointcut("execution(* greetTo(..)))")
//    protected void greetTo(){}
//
    @Pointcut("inPackage() and greetTo()")
    public void inPkgGreetTo(){}
}
