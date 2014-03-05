package gcalendartest;

import java.io.IOException;
import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class LoginInfoImpl implements LoginInfo {
	
	private final GoogleAuthorizationCodeFlow flow;
	private final LoginHelper helper;
	private final HttpTransport transport;
	private Credential credential;
	private String lastState;
	
	public LoginInfoImpl(LoginHelper helper, HttpTransport transport, JsonFactory jfac) {
		this.helper = helper;
		flow = new GoogleAuthorizationCodeFlow(
				transport, jfac, helper.getClientId(), helper.getClientSecret(),
				Arrays.asList(new String[] {"https://www.googleapis.com/auth/calendar.readonly"}));
		this.transport = transport;
	}

	public synchronized String getNewAuthorizationUrl() {
		lastState = helper.getNewState();
		return flow.newAuthorizationUrl()
				.setRedirectUri(helper.getRedirectUri())
				.setState(lastState)
				.build();
	}
	
	public synchronized String getStateAndReset() {
		String state = lastState;
		lastState = null;
		return state;
	}
	
	public synchronized Credential getCredential(String code) throws IOException {
		GoogleAuthorizationCodeTokenRequest tokenRequest = 
				flow.newTokenRequest(code)
					.setRedirectUri(helper.getRedirectUri());
		GoogleTokenResponse tokenResponse = tokenRequest.execute();

		credential = flow.createAndStoreCredential(tokenResponse, null);
		return credential;
	}

	public synchronized void logout() throws IOException {
		HttpResponse revokeResponse = transport.createRequestFactory()
			    .buildGetRequest(new GenericUrl(
			        String.format(
			            "https://accounts.google.com/o/oauth2/revoke?token=%s",
			            credential.getAccessToken()))).execute();
		credential = null;
		if(revokeResponse.getStatusCode() != 200) {
			// google API uses IOException so use same
			throw new IOException("Revoke credential failed: " + revokeResponse.getStatusMessage());
		}
	}
	
	public synchronized Credential getCredential() {
		return credential;
	}
	
	public synchronized boolean isLoggedIn() {
		return credential != null && credential.getAccessToken() != null;
	}
}

class LoginInfoFactory {
	
	private final LoginHelper helper;
	private final HttpTransport transport;
	private final JsonFactory jsonFactory;
	
	public LoginInfoFactory(LoginHelper helper, HttpTransport transport, JsonFactory jsonFactory) {
		this.helper = helper;
		this.transport = transport;
		this.jsonFactory = jsonFactory;
	}
	
	public LoginInfo newInstance() {
		return new LoginInfoImpl(helper, transport, jsonFactory);
	}
}
