package com.next.music.hadoop.hdfs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import com.alibaba.fastjson.JSONObject;
import com.next.music.entity.MUser;
import com.next.music.hadoop.util.HadoopConfigure;

/**
 * 获取文件列表 完成
 *  
 *
 */
public class getdir extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public getdir() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw = response.getWriter();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		JSONObject json = new JSONObject();
		
		if(username==null || password == null)
		{
			json.put("filelist", null);
		}
		else
		{
			List<String> list = getDirForHdfs(username,password);
			json.put("filelist", list);
		}
		
		pw.write(json.toString());
		pw.close();
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	public static List<String> getDirForHdfs(String username,String password)
	{
		//校验用户名密码 null
		MUser user = new MUser();
		user.setUsername(username);
		user.setPassword(password);
		
		List<String> filelist = null;
		
		if(user!=null)
		{
			//获取文件列表
			String path = "";
			filelist = new ArrayList<String>();
			//配置文件系统
			Configuration hdfsconfig = new Configuration();
	        hdfsconfig.set("fs.default.name", HadoopConfigure.HadoopServer);
			
			try {
				FileSystem hdfs = FileSystem.get(URI.create(path),hdfsconfig);
				FileStatus[] fs = hdfs.listStatus(new Path(path));
				Path[] listPath = FileUtil.stat2Paths(fs);
				if(listPath!=null)
				{
					for(Path p : listPath)
					{
						//去掉路径 留下文件名
						String filename = p.toString().replace(path+"/", "");
						//System.out.println(filename);
						filelist.add(filename);
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return filelist;
	}
	
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> list = getDirForHdfs("chenjiao", "chenjiao");
		if(list !=null)
		{
			System.out.println(list.size());
			for(String name:list)
			{
				System.out.println(name);
			}
		}
		else
		{
			System.out.println("空");
		}
	}
}
