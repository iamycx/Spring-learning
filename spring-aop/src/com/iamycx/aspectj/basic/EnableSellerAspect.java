package com.iamycx.aspectj.basic;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

import com.iamycx.advisor.Seller;
import com.iamycx.aspectj.example.SmartSeller;

@Aspect
public class EnableSellerAspect {

	@DeclareParents(value="com.iamycx.aspectj.example.NaiveWaiter",defaultImpl=SmartSeller.class)
	public Seller seller;
}
