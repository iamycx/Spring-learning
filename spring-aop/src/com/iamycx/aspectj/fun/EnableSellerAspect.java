package com.iamycx.aspectj.fun;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.core.Ordered;

import com.iamycx.aspectj.example.Seller;
import com.iamycx.aspectj.example.SmartSeller;


@Aspect
public class EnableSellerAspect implements Ordered {
	@DeclareParents(value = "com.baobaotao.NaiveWaiter", defaultImpl = SmartSeller.class)
	public static Seller seller;
	public int getOrder() {
		return 2;
	}
}
