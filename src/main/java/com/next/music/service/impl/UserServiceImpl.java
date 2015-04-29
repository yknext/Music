package com.next.music.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.next.music.dao.BaseDao;
import com.next.music.dao.UserDao;
import com.next.music.entity.MUser;
import com.next.music.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource
	private BaseDao<MUser> baseDao;

	@Override
	public void saveUser(MUser user) {
		baseDao.save(user);
	}

	@Override
	public void updateUser(MUser user) {
		baseDao.update(user);
	}

	@Override
	public MUser findUserById(int id) {
		return baseDao.get(MUser.class, id);
	}

	@Override
	public void deleteUser(MUser user) {
		baseDao.delete(user);
	}

	@Override
	public List<MUser> findAllList() {
		return baseDao.find("from MUser");
	}

	@Override
	public MUser findUserByNameAndPassword(MUser user) {
		return baseDao.get("from MUser u where u.username=?0 and u.password=?1 ", new Object[]{user.getUsername(),user.getPassword()});
	}
	
	
}
