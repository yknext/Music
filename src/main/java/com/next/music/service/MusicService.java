package com.next.music.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.next.music.entity.Music;

public abstract interface MusicService {
	
	
	public void saveNewMUsic(Music m);
	
	public Music getMusicByName(String name);
	
	public List<Music> getMusicByUserid(int userid);
	
	public Music getMusicByMd5(String md5);
	
	public boolean queryMusicByName(String name);
	
	public boolean queryMusicByMd5(String md5);
	
	public JSONArray SyncJsonArray(String clientlist,String token);
}
