package com.iamycx.advisor;

import org.springframework.aop.support.IntroductionInfoSupport;

import com.iamycx.introduce.Monitorable;

public class MyIntroduceInfo extends IntroductionInfoSupport{
   public MyIntroduceInfo(){
	   super();
	   super.publishedInterfaces.add(Monitorable.class);
   }
}
