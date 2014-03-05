package gcalendartest;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class CheckTag extends TagSupport {

	private String expectAttr;
	private String fromContextUrl;
	
	@Override
	public int doStartTag() {
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		if(pageContext.getRequest().getAttribute(expectAttr) == null) {
			ServletUtils.pageForward(pageContext, fromContextUrl);
		}
		return EVAL_PAGE;
	}
	
	public void setExpectAttr(String expectAttr) {
		this.expectAttr = expectAttr;
	}
	
	public void setFromContextUrl(String contextUrl) {
		this.fromContextUrl = contextUrl;
	}
}
