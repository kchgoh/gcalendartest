package gcalendartest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.ServletContext;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;

public class LoginHelperImpl implements LoginHelper {

	private final GoogleClientSecrets clientSecrets;
	private final String CLIENT_ID;
	private final String CLIENT_SECRET;

	public LoginHelperImpl(ServletContext servletContext, JsonFactory jfac) {
		Reader reader = new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/client_secrets.json"));
		try {
			clientSecrets = GoogleClientSecrets.load(jfac, reader);
		} catch (IOException e) {
			throw new Error("No client_secrets.json found", e);
		}
		CLIENT_ID = clientSecrets.getWeb().getClientId();
		CLIENT_SECRET = clientSecrets.getWeb().getClientSecret();
	}

	public String getNewState() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}

	public String getClientId() {
		return CLIENT_ID;
	}

	public String getClientSecret() {
		return CLIENT_SECRET;
	}

	public String getRedirectUri() {
		return "http://localhost:8080/google-calendar-test-webapp/doLoginCallback";
	}
}
