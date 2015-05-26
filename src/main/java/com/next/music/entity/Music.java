package com.next.music.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="music_music")
public class Music implements Serializable{

	private static final long serialVersionUID = 11324123L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	/**
	 * 音乐名称
	 */
	private String m_name;
	//文件名
	private String m_filename;
	/**
	 * 保存路径
	 */
	private String m_path;
	/**
	 * 文件MD5
	 */
	private String m_md5;
	
	/**
	 * 用户ID
	 */
	private Integer m_userid;	
	/**
	 * 创建时间
	 */
	private Date m_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public String getM_path() {
		return m_path;
	}
	public void setM_path(String m_path) {
		this.m_path = m_path;
	}
	
	
	
	public Integer getM_userid() {
		return m_userid;
	}
	public void setM_userid(Integer m_userid) {
		this.m_userid = m_userid;
	}
	public Date getM_date() {
		return m_date;
	}
	public void setM_date(Date m_date) {
		this.m_date = m_date;
	}
	
	
	
	
	public String getM_filename() {
		return m_filename;
	}
	public void setM_filename(String m_filename) {
		this.m_filename = m_filename;
	}
	public String getM_md5() {
		return m_md5;
	}
	public void setM_md5(String m_md5) {
		this.m_md5 = m_md5;
	}
	
	
}
