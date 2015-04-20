package com.next.music.service;

import com.next.music.entity.MUser;



public abstract interface LoginService {
	public abstract boolean login(MUser user);
}
