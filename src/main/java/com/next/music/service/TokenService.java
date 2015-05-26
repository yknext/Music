package com.next.music.service;

import com.next.music.entity.Token;

public abstract interface TokenService {
	
	/**
	 * 创建新的令牌
	 * @param userid
	 * @return
	 * 令牌字符串
	 */
	public Token saveNewToken(Integer userid);
	/**
	 * 验证令牌字符串
	 * @param tokenString
	 * @return
	 */
	public boolean VerifyToken(String tokenString);
	
	/**
	 * 获取令牌对应的用户ID
	 * @param tokenString
	 * @return
	 */
	public Integer GetUserIDByTokenString(String tokenString);
	
}
