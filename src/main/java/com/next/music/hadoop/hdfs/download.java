package com.next.music.hadoop.hdfs;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.next.music.entity.*;
import com.next.music.hadoop.util.HadoopConfigure;
/**
 * 下载类
 * 从HDFS文件系统下载文件
 * // /download?username=&password=&filename=
 * 
 *
 */
public class download extends HttpServlet {

	String enc = "UTF-8";
	String contentType = "application/x-msdownload";
	Configuration hdfsconfig;
	/**
	 * Constructor of the object.
	 */
	public download() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		//获取参数
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String filename = request.getParameter("filename");

		if (username == null || password == null || filename == null) {
			// 没有用户名密码
		} else {
			MUser user = new MUser();;
			user.setUsername(username);
			user.setPassword(password);
			
			if (user != null)
			{
				// 文件路径
				String fullFilePath = "/" + filename;
				System.out.println(fullFilePath);
				//HDFS文件系统
				FileSystem fs = FileSystem.get(URI.create(fullFilePath),hdfsconfig);
				// 判断文件是否存在
				long fileLength = 0;
				FileStatus fileList[] = fs.listStatus(new Path(fullFilePath)); 
				
				if(fileList != null)
				{
					if(fileList[0].getPath().getName().equals(filename))
						fileLength = fileList[0].getLen();// 获取文件长度
				}
				
				/* 如果文件长度大于0 */
				if (fileLength != 0) {
					response.reset();
					response.setContentType(contentType);
					response.addHeader("Content-Disposition",
							"attachment; filename=\"" + filename + "\"");
					response.setContentLength((int)fileLength);
					/* 创建输入流 */
					byte[] buf = new byte[4096];
					FSDataInputStream hdfsInStream = fs.open(new Path(fullFilePath));
					/* 创建输出流 */
					ServletOutputStream servletOS = response.getOutputStream();
					int readLength;
					while (((readLength = hdfsInStream.read(buf)) != -1)) {
						servletOS.write(buf, 0, readLength);
					}
					hdfsInStream.close();
					servletOS.flush();
					servletOS.close();
				}
				else
				{
					//文件不存在
					System.out.println("文件不存在");
				}
				
			}
			
		}

	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// 初始化HDFS配置
		hdfsconfig = new Configuration();
		hdfsconfig.set("fs.default.name", HadoopConfigure.HadoopServer);
	}

}
