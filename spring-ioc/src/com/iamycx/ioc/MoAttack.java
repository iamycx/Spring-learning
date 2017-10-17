package com.iamycx.ioc;

public class MoAttack implements ActorArrangable{

	private GeLi geli;

	public void injectGeli(GeLi geli) {
		this.geli = geli;		
	}
	
	public void cityGateAsk() {
		geli.responseAsk("Ä«Õß¸ïÀë");
	}
}
