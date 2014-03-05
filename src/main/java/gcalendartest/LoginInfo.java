package gcalendartest;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;

public interface LoginInfo {

	public abstract String getNewAuthorizationUrl();

	public abstract String getStateAndReset();

	public abstract Credential getCredential(String code) throws IOException;

	public abstract void logout() throws IOException;

	public abstract Credential getCredential();

	public abstract boolean isLoggedIn();

}