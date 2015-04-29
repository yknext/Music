package com.next.music.dao;

import org.springframework.stereotype.Repository;

import com.next.music.entity.MUser;



/**
 * 
 * @author Next
 *
 */
public abstract interface UserDao {
	
	/**
	 * 登录
	 * @param user
	 * @return
	 */
	public boolean user_login(MUser user);
}
