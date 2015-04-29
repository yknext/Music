package com.next.music.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.next.music.entity.MUser;
import com.next.music.service.UserService;
import com.next.music.util.Contents;

@Controller
public class UserAction extends BaseAction{
	private static final long serialVersionUID = 7234523451234123L;
	private MUser user;
	
	@Resource
	private UserService userService;
	
	@Override
	public String execute() throws Exception {
		if(user == null)
		{
			setData("用户为空");
			return ERROR;
		}
		System.out.println(user.getUsername()+" "+user.getPassword());
		MUser realUser = userService.findUserByNameAndPassword(user);
		if(realUser != null)
		{
			setData("登录成功");
			putSession(Contents.SES_USER_ID, realUser.getId());
		}
		else
		{
			setData("登录失败");
		}
		return SUCCESS;
	}

	public MUser getUser() {
		return user;
	}

	public void setUser(MUser user) {
		this.user = user;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public String test()
	{
		return SUCCESS;
	}
	
}	
