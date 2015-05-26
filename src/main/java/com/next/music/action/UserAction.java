package com.next.music.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.next.music.entity.MUser;
import com.next.music.entity.Token;
import com.next.music.service.TokenService;
import com.next.music.service.UserService;
import com.next.music.util.Contents;
import com.next.music.util.LoginData;

/**
 * 
 * @author Next
 * 
 */
@Controller
public class UserAction extends BaseAction{
	private static final long serialVersionUID = 7234523451234123L;
	private MUser user;
	/**
	 * 登录协议
	 */
	private LoginData logindata = new LoginData();
	@Resource
	private UserService userService;
	@Resource
	TokenService tokenService;
	
	public String reguser()
	{
		long start = System.currentTimeMillis();
		if(user != null)
			System.out.println("用户："+user.getUsername()+" 尝试注册...");
		else
			System.out.println("注册用户为空");
		
		if(user == null || user.getUsername().equals("") || user.getPassword().equals(""))
		{
			logindata.setToast("请填写完整...");
			data.setSuccess(false);
		}
		else
		{
				if(userService.findUserByName(user.getUsername()) == null)
				{
					userService.saveUser(user);					
					logindata.setToast("注册成功，用户名:"+user.getUsername());								
					data.setSuccess(true);
					System.out.println("用户："+user.getUsername()+" 注册成功...");
				}
				else
				{
					logindata.setToast("用户名已存在");
					data.setSuccess(false);
				}
				
		}
		
		setData(logindata);
		
		long stop = System.currentTimeMillis();
		System.out.println("耗时："+ (stop -start));
		return SUCCESS;
	}
	
	
	@Override
	public String execute() throws Exception {
		long start = System.currentTimeMillis();
		if(user != null)
			System.out.println("用户："+user.getUsername()+" 尝试登录...");
		else
			System.out.println("登录用户为空");
		
		if(user == null)
		{
			logindata.setToast("请输入用户名密码");
			data.setSuccess(false);
		}
		else
		{
			MUser realUser = userService.findUserByNameAndPassword(user);
			if(realUser != null)
			{
				data.setSuccess(true);
				//用户id				
				int userid = userService.findUserByName(user.getUsername()).getId();
				
				Token token = tokenService.saveNewToken(userid);
				logindata.setToken(token);
				logindata.setToast("登录成功");
				
				System.out.println("用户"+user.getUsername()+" 登录成功...");
			}
			else
			{
				data.setSuccess(false);
				logindata.setToast("登录失败,用户名或密码错误");
			}
		}
		
		setData(logindata);
		
		long stop = System.currentTimeMillis();
		System.out.println("耗时："+ (stop -start));
		return SUCCESS;
	}

	public MUser getUser() {
		return user;
	}

	public void setUser(MUser user) {
		this.user = user;
	}


	

	
}	
