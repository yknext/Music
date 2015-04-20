package com.next.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.next.music.dao.LoginDao;
import com.next.music.entity.MUser;
import com.next.music.service.LoginService;

public class LoginServiceImpl implements LoginService{

	//调用Dao对象 自动注入
	@Autowired
	LoginDao login_dao;
	
	@Override
	public boolean login(MUser user) {
		return login_dao.user_login(user);
	}

	public LoginDao getLogin_dao() {
		return login_dao;
	}

	public void setLogin_dao(LoginDao login_dao) {
		this.login_dao = login_dao;
	}

}
