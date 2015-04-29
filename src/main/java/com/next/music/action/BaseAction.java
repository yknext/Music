package com.next.music.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;
import org.springframework.stereotype.Controller;

import com.next.music.util.Data;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Next
 *
 */
@Controller
public class BaseAction extends ActionSupport {

	private static final long serialVersionUID = -300312364285482112L;
	protected Data data = Data.success(null);
	
	protected boolean isGet() {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest httpReq = (HttpServletRequest) ctx.get(StrutsStatics.HTTP_REQUEST);
		return "get".equalsIgnoreCase(httpReq.getMethod());
	}
	
	protected void putSession(String key, Object value) {
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().put(key, value);
	}
	
	protected void removeSession(String key) {
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().remove(key);
	}
	
	public Data getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data.setData(data);
	}
	
	public HttpServletRequest getHttpServletRequest(){
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest httpReq = (HttpServletRequest) ctx.get(StrutsStatics.HTTP_REQUEST);
		return httpReq;
		
	}
}