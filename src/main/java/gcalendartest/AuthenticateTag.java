package gcalendartest;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Add this tag to top of a page to check user is logged in, otherwise redirect
 * to login.
 *
 */
public class AuthenticateTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() {
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		HttpSession session = pageContext.getSession();
		
		LoginInfo login = (LoginInfo)session.getAttribute(Constants.LOGIN_INFO);
		if(login == null || login.isLoggedIn() == false) {
			ServletUtils.pageForward(pageContext, Constants.LOGIN_SERVLET);
			return SKIP_PAGE;
		}
		return EVAL_PAGE;
	}
}
