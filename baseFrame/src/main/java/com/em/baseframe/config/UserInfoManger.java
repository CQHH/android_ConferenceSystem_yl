package com.em.baseframe.config;

import com.em.baseframe.util.SPUtils;

/**
 * @title  获取设置用户登录状态
 * @date   2017/06/17
 * @author enmaoFu
 */
public abstract class UserInfoManger {

	/**
	 * 获得登陆状态
	 */
	public static boolean isLogin() {
		SPUtils spUtils = new SPUtils("userConfig");
		return (Boolean) spUtils.get("isLogin", false);
	}

	/**
	 * 设置登陆状态
	 */
	public static void setIsLogin( boolean b) {
		SPUtils spUtils = new SPUtils("userConfig");
		spUtils.put("isLogin", b);
	}

	
		
	

}
