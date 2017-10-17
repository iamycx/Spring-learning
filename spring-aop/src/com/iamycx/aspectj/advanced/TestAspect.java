package com.iamycx.aspectj.advanced;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class TestAspect {

	// @After("within(com.iamycx.aspectj.example)"+"&&excution(* greeTo(..))")
	// public void greeToFun(){
	// System.out.println("-----greeToFun() executed!----");
	// }
	// @Before("!target(com.iamycx.aspectj.example.NaiveWaiter)"+"&&excution(* serveTo(..))")
	// public void notServeInNaiveWaiter(){
	// System.out.println("-----notServeInNaiveWaiter() executed!----");
	// }
	// @AfterReturning("target(com.iamycx.aspectj.example.Waiter) || "+" target(com.iamycx.aspectj.example.Seller)")
	// public void waiterOrSeller(){
	// System.out.println("--waiterOrSeller() executed!--");
	// }

	// ------------访问连接点对象----------//
	@Around("execution(* greetTo(..)) && target(com.iamycx.aspectj.example.NaiveWaiter)")
	public void joinPointAccess(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("-------joinPointAccess-------");
		System.out.println("args[0]:" + pjp.getArgs()[0]);
		System.out.println("signature:" + pjp.getTarget().getClass());
		pjp.proceed();
		System.out.println("-------joinPointAccess-------");
	}
}
