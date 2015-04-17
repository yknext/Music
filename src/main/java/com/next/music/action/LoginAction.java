package com.next.music.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.next.music.entity.MUser;
import com.next.music.service.LoginService;
import com.next.music.util.Contents;

public class LoginAction extends BaseAction{
	private static final long serialVersionUID = 7234523451234123L;
	private String username;
	private String password;
	
	@Autowired
	private LoginService loginService;
	
	@Override
	public String execute() throws Exception {		
//		if (isGet()){
//			return SUCCESS;
//		}
		MUser user = new MUser();
		user.setUsername(username);
		user.setPassword(password);
		if (loginService.login(user)){
			user.setPassword("");
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

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	
}	
