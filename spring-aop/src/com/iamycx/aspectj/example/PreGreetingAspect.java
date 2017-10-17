package com.iamycx.aspectj.example;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

//@Aspect注解将该类标识为一个切面
@Aspect
public class PreGreetingAspect{
	@Before("execution(* greetTo(..))") //切点和增强类型
	public void beforeGreeting(){
		System.out.println("How are you");
	}
}