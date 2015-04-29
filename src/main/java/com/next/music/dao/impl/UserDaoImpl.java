package com.next.music.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.next.music.dao.UserDao;
import com.next.music.entity.MUser;

@Repository("userDAO")
public class UserDaoImpl extends BaseDaoImpl<MUser> implements UserDao{

	@Autowired
	protected SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public boolean user_login(MUser user) {
		System.out.println(user.getUsername()+" : "+user.getPassword());
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
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
