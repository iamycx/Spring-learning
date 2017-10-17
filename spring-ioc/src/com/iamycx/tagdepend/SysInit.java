package com.iamycx.tagdepend;

public class SysInit {
   public SysInit(){  
	   System.out.println("SysInit");
	   //模拟从数据库中加载系统设置信息
	   SystemSettings.REFRESH_CYCLE = 100;
	   SystemSettings.SESSION_TIMEOUT = 10;
   }
}
