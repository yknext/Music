package com.next.music.action;

import com.next.music.model.User;
import com.next.music.util.Contents;

public class LoginAction extends BaseAction{
	private static final long serialVersionUID = 7234523451234123L;
	private String username;
	private String password;
	
	
	@Override
	public String execute() throws Exception {
		
//		if (isGet()){
//			return SUCCESS;
//		}
		if ("admin".equals(username) && "lw321".equals(password)){
			User user = new User();
			user.setUsername(username);
			putSession(Contents.SES_USER, user);
			setData("登录成功");
			return SUCCESS;
		}else{
			addActionError("用户名或密码错误!");
			return LOGIN;
		}
	}
	
	public String home() 
	{
		return SUCCESS;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}	
