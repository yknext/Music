package com.next.music.dao;

import com.next.music.entity.MUser;




public abstract interface LoginDao {
	
	/**
	 * 登录
	 * @param user
	 * @return
	 */
	public boolean user_login(MUser user);
}
