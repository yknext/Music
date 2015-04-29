package com.next.music.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsStatics;

import com.next.music.entity.MUser;
import com.next.music.util.Contents;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 登录拦截器
 * @author Next
 *
 */
public class LoginInteceptor extends MethodFilterInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected String doIntercept(ActionInvocation actionInvoke) throws Exception {
		
		ActionContext ac = actionInvoke.getInvocationContext();
		ServletContext servletContext = (ServletContext) ac.get(StrutsStatics.SERVLET_CONTEXT);
		ServletResponse servletResponse = (ServletResponse) ac.get(StrutsStatics.HTTP_RESPONSE);
		
		Object loginUserId = ac.getSession().get(Contents.SES_USER_ID);
		System.out.println("登录用户ID为:" + loginUserId);
		// 用户未登录
		if (loginUserId == null) {
			servletContext.getServletContextName();
			String cp = servletContext.getContextPath();
			String loginUrl = "";
			
			if (StringUtils.isNotBlank(cp) && cp.length() > 1) {
				loginUrl = cp + "/login.jsp";
			} else {
				loginUrl = "/login.jsp";
			}
			StringBuilder sb = new StringBuilder();
			
			sb.append("<script type=\"text/javascript\">");
			sb.append("	if(window.parent) {");
			sb.append("		window.parent.location.href = \"" + loginUrl + "\";");
			sb.append("	} else {");	
			sb.append("		window.location.href = \"" + loginUrl + "\";");
			sb.append("	}");
			sb.append("</script>");
			
			servletResponse.getWriter().write(sb.toString());
			return Action.NONE;
		}
		return actionInvoke.invoke();
	}
	
}
