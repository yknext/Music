package com.next.music.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.next.music.dao.BaseDao;
import com.next.music.entity.Music;
import com.next.music.service.MusicService;
import com.next.music.service.TokenService;
/**
 * 
 * @author Next
 *
 */
@Service("musicService")
public class MusicServerImpl implements MusicService{

	@Resource
	private BaseDao<Music> baseDao;
	@Resource
	private TokenService tokenService;
	
	@Override
	public void saveNewMUsic(Music m) {
		
		baseDao.save(m);
		
	}

	@Override
	public Music getMusicByName(String name) {
		List<Music> list = baseDao.find("from Music m where m.m_name=?0", new Object[]{name});
		if(null != list && list.size()>0)
			return list.get(0);
		return null;
	}

	@Override
	public Music getMusicByMd5(String md5) {
		List<Music> list = baseDao.find("from Music m where m.m_md5=?0", new Object[]{md5});
		if(null != list && list.size()>0)
			return list.get(0);
		return null;
	}

	@Override
	public boolean queryMusicByName(String name) {
		List<Music> list = baseDao.find("from Music m where m.m_name=?0", new Object[]{name});
		if(null != list && list.size()>0)
			return true;
		return false;
	}

	@Override
	public boolean queryMusicByMd5(String md5) {
		List<Music> list = baseDao.find("from Music m where m.m_md5=?0", new Object[]{md5});
		if(null != list && list.size()>0)
			return true;
		return false;
	}

	//根据同步策略生成最终的列表   key:[ upload | download ]  value [ filename ]
	@Override
	public JSONArray SyncJsonArray(String clientlist,String token) {
		//client最终列表
		JSONArray clientuploadmusicArray = new JSONArray();
		List<Music> clientListMusic = new ArrayList<Music>();
		
		//server最终列表
		JSONArray sermusicArray = new JSONArray();
		//客户端列表
		Map<String,String> clientmap = new HashMap<String,String>();
		
		JSONObject jsonObj = JSONObject.fromObject(clientlist);
		for (Iterator iter = jsonObj.keys(); iter.hasNext();) 
		{
			String key = (String)iter.next();
			//key：文件名   value： MD5值
			clientmap.put(key, jsonObj.getString(key));
			System.out.println(key+"--:--"+jsonObj.getString(key));
			JSONObject clientobj = new JSONObject();
			clientobj.put("type","upload");
			clientobj.put("filename", key);
			clientuploadmusicArray.add(clientobj);
			Music clientMusic = new Music();
			clientMusic.setM_filename(key);
			clientMusic.setM_md5(jsonObj.getString(key));
			clientListMusic.add(clientMusic);
		}
		
		int userid = tokenService.GetUserIDByTokenString(token);
		List<Music> serMusicList = getMusicByUserid(userid);
		
		
		//服务器上一首也没有，客户端有的情况，整体上传
		if((null == serMusicList || 0 == serMusicList.size()) && (null != clientmap && clientmap.size()>0))
		{
			System.out.println("全部上传");
			//整体上传客户端全部
			return clientuploadmusicArray;
		}
		//客户端一首也没有，服务器有的情况，整体下载
		if((null == clientmap || 0 == clientmap.size()) && (null != serMusicList && serMusicList.size()>0))
		{
			for(Music m : serMusicList)
			{
				JSONObject serobj = new JSONObject();
				serobj.put("type","download");
				serobj.put("filename", m.getM_filename());
				
				sermusicArray.add(serobj);
			}
			System.out.println("全部下载");
			return sermusicArray;
		}
		JSONArray subArray = new JSONArray();
		//服务器和客户端都有的情况  集合互相做差，生成 下载、上传列表,都存在的文件视为妙传
		if(null != clientListMusic && null != serMusicList && clientListMusic.size()>0 && serMusicList.size()>0)
		{
			//查找需要上传的md5
			for(Music cm : clientListMusic)
			{
				if(!md5IsExist(serMusicList,cm.getM_md5()))
				{
					//服务器不存在，需要上传
					JSONObject upClientJson = new JSONObject();
					upClientJson.put("type","upload");
					upClientJson.put("filename",cm.getM_filename());
					
					subArray.add(upClientJson);
				}
			}
			//查找需要下载的md5
			for(Music sm : serMusicList)
			{
				if(!md5IsExist(clientListMusic, sm.getM_md5()))
				{
					JSONObject upClientJson = new JSONObject();
					upClientJson.put("type","download");
					upClientJson.put("filename",sm.getM_filename());
					subArray.add(upClientJson);
				}
			}
			
			System.out.println("差分同步");
			if(0 != subArray.size())
				return subArray;
			else
				return null;
		}
		
		
		
		//都为空，则返回空
		return null;
	}
	/**
	 * 返回列表是否存在这个 md5
	 * @param list  列表
	 * @param md5  md5
	 * @return 是否存在
	 */
	private static boolean md5IsExist(List<Music> list,String md5)
	{
		for(Music m : list)
		{
			if(m.getM_md5().equals(md5))
			{
				return true;//找到匹配
			}
		}
		return false;
	}
	
	
	@Override
	public List<Music> getMusicByUserid(int userid) {
		return baseDao.find("from Music m where m.m_userid=?0", new Object[]{userid});
	}
	
}
