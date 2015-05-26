package com.next.music.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.next.music.dao.BaseDao;
import com.next.music.entity.MUser;
import com.next.music.entity.Token;
import com.next.music.service.TokenService;
@Service("tokenService")
public class TokenServiceImpl implements TokenService{

	@Resource
	private BaseDao<Token> baseDao;
	
	
	@Override
	public Token saveNewToken(Integer userid) {
		Token token = new Token();
		token.setUserid(userid);
		
		token.setCreatedate(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(token.getCreatedate());
		cal.add(Calendar.DATE, 30);                 //有效期30天
		token.setLastdata(cal.getTime());
		//生成令牌
		String tokenstr = getRandomString(16);
		token.setToken(tokenstr);
		
		//删除所有旧的Token
		baseDao.executeHql("delete from Token t where t.userid=?0",new Object[]{userid});
		
		baseDao.save(token);
			
		
		return token;
	}

	@Override
	public boolean VerifyToken(String tokenString) {
		List<Token> tklist = baseDao.find("from Token t where t.token=?0", new Object[]{tokenString});
		boolean ok = false;
		if(tklist != null && tklist.size()>0)
		{
			for(Token t : tklist)
			{
				Calendar now=Calendar.getInstance(); 
				now.setTime(new Date()); 
				Calendar lastdate=Calendar.getInstance(); 
				lastdate.setTime(t.getLastdata()); 				
				if(lastdate.before(now))
				{
					//过期了
					System.out.println("登录令牌过期了,需要重新登录...");
				}
				else
				{ 
					ok = true;
					//没过期					
					//System.out.println("登录令牌没过期");
				}
			}
		}
		
		return ok;
	}

	@Override
	public Integer GetUserIDByTokenString(String tokenString) {
		List<Token> list = baseDao.find("from Token t where t.token=?0", new Object[]{tokenString});
		if(null != list && list.size()>0)
		{
			return list.get(0).getUserid();
		}
		return null;
	}
	
	/**
	 * 生成随机字符串
	 * @param length 长度
	 * @return
	 * 随机字符串
	 */
	public static String getRandomString(int length) { 
	    String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }   

	
}
