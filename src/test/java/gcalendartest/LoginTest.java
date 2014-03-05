package gcalendartest;

import gcalendartest.Constants;
import gcalendartest.LoginCallbackServlet;
import gcalendartest.LoginInfo;
import gcalendartest.LoginInfoFactory;
import gcalendartest.LoginServlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import static org.easymock.EasyMock.*;

public class LoginTest extends EasyMockSupport {

	MockServletContext context;
	MockServletConfig config;
	MockHttpSession session;
	
	@Before
	public void setUp() {
		// need this to populate context for testing servlets
		context = new MockServletContext("/webapp", null);
		config = new MockServletConfig(context);
		session = new MockHttpSession();
	}

	@Test
	public void testShowLogin() throws ServletException, IOException {
		LoginInfo info = createNiceMock(LoginInfo.class);
		LoginInfoFactory factory = createNiceMock(LoginInfoFactory.class);
		expect(factory.newInstance()).andStubReturn(info);
		expect(info.getNewAuthorizationUrl()).andStubReturn("TESTURL");

		context.setAttribute(Constants.LOGIN_INFO_FACTORY, factory);

		
		HttpServletResponse response = createNiceMock(HttpServletResponse.class);
		HttpServletRequest request = createNiceMock(HttpServletRequest.class);
		expect(request.getSession()).andStubReturn(session);

		// checks
		request.setAttribute("googleLoginUrl", "TESTURL");
		expectLastCall().once();
		expect(request.getRequestDispatcher("/login.jsp"))
			.andReturn(createNiceMock(RequestDispatcher.class)).once();

		replayAll();

		LoginServlet sut = new LoginServlet();
		sut.init(config); 
		sut.doGet(request, response);
		
		verifyAll();
	}

	@Test
	public void testLoginCallback() throws ServletException, IOException {
		LoginInfo info = createMock(LoginInfo.class);
		HttpServletResponse response = createNiceMock(HttpServletResponse.class);
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getSession()).andStubReturn(session);
		session.setAttribute(Constants.LOGIN_INFO, info);
		expect(request.getRequestURL()).andStubReturn(new StringBuffer("http://test/"));
		expect(request.getQueryString()).andStubReturn("state=TESTSTATE&code=TESTCODE");
		expect(info.getStateAndReset()).andReturn("TESTSTATE");

		// checks
		expect(info.getCredential("TESTCODE")).andReturn(null);
		expect(info.isLoggedIn()).andReturn(true);
		response.sendRedirect(endsWith(Constants.MAIN_SERVLET));
		expectLastCall().once();
		
		replayAll();

		LoginCallbackServlet sut = new LoginCallbackServlet();
		sut.init(config);
		sut.doGet(request, response);
		
		verifyAll();
	}
}
