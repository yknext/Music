package com.next.music.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.next.music.entity.Music;
import com.next.music.service.MusicService;
import com.next.music.service.TokenService;
import com.next.music.service.UserService;
import com.next.music.util.Contents;
import com.next.music.util.FileUtil;

public class MusicAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	// 令牌
	public String token;
	//用于下载
	public String filename;
	
	private File uploadfile; // 上传的文件
	private String uploadfileContentType;// 文件的类型
	private String uploadfileFileName;// 文件的名称
	
	@Resource
	private TokenService tokenService;
	@Resource
	private MusicService musicService;
	@Resource
	private UserService userService;
	
	@Override
	public String execute() throws Exception {
		
		return SUCCESS;
	}
	/**
	 * 客户端列表
	 */
	private String clientlist;
	
	private String downloadFilePath;
	/**
	 * 下载文件
	 * @return
	 * 
	 */
	public String download()
	{
		if(tokenService.VerifyToken(token))
		{
			Music music = musicService.getMusicByName(filename);
			if(music != null)
			{
				downloadFilePath = music.getM_path();
				return SUCCESS;
			}
		}
		return ERROR;
	}
	
	public InputStream getInputStream() throws Exception {   
		  
        return new FileInputStream(new File(downloadFilePath));   
  
    }
	
	
	/**
	 * 根据策略生成同步列表
	 * @return
	 */
	public String synclist()
	{
		if (tokenService.VerifyToken(token)) {

		JSONArray resultJSONArray = musicService.SyncJsonArray(clientlist,token);
		data.setSuccess(true);
		if(null != resultJSONArray)
		{
			System.out.println("需要同步：" + resultJSONArray.size() +" 首");
			setData(resultJSONArray);
		}
		else
			setData("null");
		}
		else
		{
			data.setSuccess(false);
			setData("无权访问");
		}
		return SUCCESS;
	}
	
	/**
	 * 上传服务器没有的音乐
	 * @return SUCCESS
	 * @throws Exception
	 * 
	 */
	public String upload() throws Exception {
		//String filestr  = uploadfile == null ? "NULL File":"文件存在";
		//System.out.println(filestr + uploadfileFileName+"  "+uploadfileContentType+" "+token);
		
		if(null != uploadfile)
			System.out.println("收到上传文件："+uploadfile.getName());
		
		if (tokenService.VerifyToken(token)) {
			if (uploadfile != null && uploadfileContentType != null
					&& uploadfileFileName != null) {
				try {
					File destFile = new File(Contents.SAVEPATH,
							uploadfileFileName);

					if (!destFile.exists()) {
						destFile.createNewFile();
					}
					String md5 = FileUtil.getMD5(uploadfile);

					FileUtils.copyFile(uploadfile, destFile);
					String realPath = destFile.getPath();

					tokenService.GetUserIDByTokenString(token);
					// 新建音乐
					Music m = new Music();
					m.setM_name(uploadfileFileName);
					m.setM_filename(uploadfileFileName);
					m.setM_path(realPath);
					m.setM_md5(md5);
					m.setM_userid(tokenService.GetUserIDByTokenString(token));
					m.setM_date(new Date());

					musicService.saveNewMUsic(m);

					data.setSuccess(true);
					setData("上传成功...");
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			} else {
				data.setSuccess(false);
				setData("上传失败");
			}
		}
		else
		{
			data.setSuccess(false);
			System.out.println("权限不足...");
			setData("权限不足");
		}
		return SUCCESS;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public File getUploadfile() {
		return uploadfile;
	}

	public void setUploadfile(File uploadfile) {
		this.uploadfile = uploadfile;
	}

	public String getUploadfileContentType() {
		return uploadfileContentType;
	}

	public void setUploadfileContentType(String uploadfileContentType) {
		this.uploadfileContentType = uploadfileContentType;
	}

	public String getUploadfileFileName() {
		return uploadfileFileName;
	}

	public void setUploadfileFileName(String uploadfileFileName) {
		this.uploadfileFileName = uploadfileFileName;
	}

	public String getClientlist() {
		return clientlist;
	}

	public void setClientlist(String clientlist) {
		this.clientlist = clientlist;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
