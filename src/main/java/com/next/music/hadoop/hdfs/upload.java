package com.next.music.hadoop.hdfs;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;  
import javax.servlet.http.*;  

import java.io.*;  
import java.net.URI;
import java.util.*;  

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;  
import org.apache.commons.fileupload.disk.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import com.alibaba.fastjson.JSONObject;
import com.next.music.entity.MUser;
import com.next.music.hadoop.util.HadoopConfigure;

/**
 * 上传文件 upload 
 * 上传文件到Hadoop HDFS文件系统
 * /upload?username=&password=
 *
 */
public class upload extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String filePath;    // 文件存放目录  
    private String tempPath;    // 临时文件目录  
 
    //用户名
    private String username;
    private String password;
    private boolean userok  =  false;
    //HDFS
    Configuration hdfsconfig ;
    // 初始化  
    public void init(ServletConfig config) throws ServletException  
    {  
        super.init(config);  
        // 从配置文件中获得初始化参数  
        filePath = config.getInitParameter("filepath");  
        tempPath = config.getInitParameter("temppath");  
        //hdfs
        hdfsconfig = new Configuration();
        hdfsconfig.set("fs.default.name", HadoopConfigure.HadoopServer);
        
        ServletContext context = getServletContext();  
        
//        filePath = context.getRealPath(filePath);  
//        tempPath = context.getRealPath(tempPath);  
        
        //System.out.println("路径" + filePath +" "+ tempPath);
        //如果路径不存在，则创建路径
        File pathFile = new File(filePath);
        File pathTemp = new File(tempPath);
        if(!pathFile.exists()){
        	pathFile.mkdirs();
        }
        if(!pathTemp.exists()){
        	pathTemp.mkdirs();
        }
        //System.out.println("文件存放目录、临时文件目录准备完毕 ...");  
    }  
      
    // doPost  
    public void doPost(HttpServletRequest req, HttpServletResponse res)  
        throws IOException, ServletException  
    {  
    	req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
    	//System.out.println("接收到文件上传...");
        res.setContentType("text/plain;charset=UTF-8");  
        PrintWriter pw = res.getWriter(); 
        
        try{  
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();  
            // threshold 极限、临界值，即硬盘缓存 1G 
            diskFactory.setSizeThreshold(1000 * 1024 * 1024);  
            // repository 贮藏室，即临时文件目录  
            diskFactory.setRepository(new File(tempPath));  
          
            ServletFileUpload upload = new ServletFileUpload(diskFactory);  
            // 设置允许上传的最大文件大小 1G 
            upload.setSizeMax(1000 * 1024 * 1024);  
            // 解析HTTP请求消息头  
            List<FileItem> fileItems = upload.parseRequest(new ServletRequestContext(req));  
            Iterator<FileItem> iter = fileItems.iterator();  
            while(iter.hasNext())  
            {  
                FileItem item = (FileItem)iter.next();  
                if(item.isFormField())  
                {  
                    //System.out.println("处理表单内容 ...");  
                    processFormField(item, pw);  
                }else{  
                    //System.out.println("处理上传的文件 ...");  
                    processUploadFile(item, pw);  
                }  
            }// end while()  
 
            pw.close();  
        }catch(Exception e){  
            //System.out.println("使用 fileupload 包时发生异常 ...");  
            e.printStackTrace();  
        }// end try ... catch ...  
    }// end doPost()  
 
    // 处理表单内容  
    private void processFormField(FileItem item, PrintWriter pw)  
        throws Exception  
    {  
        String name = item.getFieldName();  
        String value = item.getString(); 
        //System.out.println(name+":"+value);
        //获取用户名和密码
        if(item.getFieldName().equals("username"))
        {
        	username = item.getString();
//        	pw.println(name + " : " + value + "\r\n");  
        }
        if(item.getFieldName().equals("password"))
        {
        	password = item.getString();
        }
       
    }  
      
    // 处理上传的文件  
    private void processUploadFile(FileItem item, PrintWriter pw)  
        throws Exception  
    {  
        // 此时的文件名包含了完整的路径，得注意加工一下  
        String filename = item.getName();         
        String nn = item.getFieldName();
        //System.out.println("项目名"+nn+"完整的文件名：" + filename);  
        int index = filename.lastIndexOf("\\");  
        filename = filename.substring(index + 1, filename.length());  
 
        long fileSize = item.getSize();  
 
        if("".equals(filename) && fileSize == 0)  
        {             
            //System.out.println("文件名为空 ...");  
            return;  
        }  
 
//        File uploadFile = new File(filePath + "/" + filename);  
//        if(!uploadFile.exists()){
//        	uploadFile.createNewFile();
//        }
        //不写到本地 
       //item.write(uploadFile);
        //通过用户名获取用户id ,创建用户
        MUser user = new MUser();
        user.setPassword(password);
        user.setUsername(username);
        if(user!=null)
        {
        	userok  =  true;
        }
        
        
        JSONObject json = new JSONObject();
        if(userok)
        {
        	
        	//System.out.println("用户id");
            //直接写到HDFS
            //用户文件夹 /alluser/u存到用户的目录下
            String hdfsfilepath = HadoopConfigure.HadoopServer+"/alluser/u/" + filename;
            //System.out.println(hdfsfilepath);
            FileSystem fs = FileSystem.get(URI.create(hdfsfilepath), hdfsconfig);
    		InputStream fis =  item.getInputStream();//获取输入流
    		OutputStream os = fs.create(new Path(hdfsfilepath));
    		IOUtils.copyBytes(fis, os, 4096, true);
    		os.close();
    		fis.close();
       
    		//换成json数据
//        	pw.println(filename + " 文件保存完毕 ...");  
//          pw.println("文件大小为 ：" + fileSize + "\r\n");  
    		
    		json.put("flag", 0);
    		pw.write(json.toString());
        }
        else
        {
    		json.put("flag", 1);
    		pw.write(json.toString());
        }
    }  
      
    // doGet  
    public void doGet(HttpServletRequest req, HttpServletResponse res)  
        throws IOException, ServletException  
    {  
        doPost(req, res);  
    }  
    
    
}
