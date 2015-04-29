package com.next.music.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.next.music.entity.MUser;


/**
 * 
 * @author Next
 *
 */
public abstract interface UserService {
	
	public abstract void saveUser(MUser user);	
	public abstract void updateUser(MUser user);	
	public abstract MUser findUserById(int id);	
	public abstract void deleteUser(MUser user);	
	public abstract List<MUser> findAllList();	
	public abstract MUser findUserByNameAndPassword(MUser user);
	
}
