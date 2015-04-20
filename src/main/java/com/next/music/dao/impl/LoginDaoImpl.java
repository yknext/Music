package com.next.music.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.next.music.dao.LoginDao;
import com.next.music.entity.MUser;


public class LoginDaoImpl implements LoginDao{

	@Autowired
	protected SessionFactory sessionFactory;
	
	@Override
	public boolean user_login(MUser user) {
		System.out.println(user.getUsername()+" : "+user.getPassword());
		
		Session session = sessionFactory.openSession();
		session.save(user);
		
		return true;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	

}
