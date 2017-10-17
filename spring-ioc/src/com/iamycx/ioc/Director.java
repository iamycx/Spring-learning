package com.iamycx.ioc;

public class Director {

	public void director(){
		GeLi geLi = new LiuDeHua();
		MoAttack moAttack = new MoAttack();
		moAttack.injectGeli(geLi);
		moAttack.cityGateAsk();
	}
}
