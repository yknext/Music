package com.next.music.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="music_token")
public class Token implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 123179832L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	/**
	 * 令牌
	 */
	private String token;
	/**
	 * 用户ID
	 */
	private Integer userid;
	/**
	 * 创建时间
	 */
	private Date createdate;
	/**
	 * 过期时间
	 */
	private Date lastdate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getLastdata() {
		return lastdate;
	}
	public void setLastdata(Date lastdata) {
		this.lastdate = lastdata;
	}
	
	
}
