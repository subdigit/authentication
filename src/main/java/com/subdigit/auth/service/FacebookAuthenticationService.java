package com.subdigit.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.subdigit.auth.AuthenticationResult;
import com.subdigit.broker.RequestResponseBroker;
import com.subdigit.utilities.HttpConnectionHelper;

public class FacebookAuthenticationService extends AbstractAuthenticationService
{
	private static final String SERVICE_IDENTIFIER = "facebook";

	public FacebookAuthenticationService(){ super(); }
	public FacebookAuthenticationService(RequestResponseBroker<HttpServletRequest, HttpServletResponse> broker){ super(broker); }


	private String getLoginRedirectURL(String state)
	{
		return "https://www.facebook.com/dialog/oauth?" +
				"client_id=" + getServiceApplicationID() +
				"&redirect_uri=" + HttpConnectionHelper.encode(getCallbackURL()) +
				"&" + getStateCheckParameter() + "=" + state +
				"&scope=email,publish_stream";
	}


	private String getAuthenticationURL(String authenticationCode)
	{
		return "https://graph.facebook.com/oauth/access_token?" +
				"client_id=" + getServiceApplicationID() +
				"&redirect_uri=" + HttpConnectionHelper.encode(getCallbackURL()) +
				"&client_secret=" + getServiceApplicationSecret() +
				"&code=" + authenticationCode;
	}

	
	private String getDataURL(String accessToken)
	{
		return "https://graph.facebook.com/me?access_token=" + accessToken;
	}


	@Override
	public String getIdentifier(){ return SERVICE_IDENTIFIER; }


	@Override
	protected AuthenticationResult connectService(AuthenticationResult ar)
	{
		ar.addStatus(200, "Redirecting to the authentication service for: " + getIdentifier());
		ar.setRedirectURL(getLoginRedirectURL(ar.getState()));
		ar.setSuccess(true);

		return ar;
	}


	private String getAccessToken(String authenticationCode)
	{
		String token = null;
		
		token = HttpConnectionHelper.getBasicResponse(getAuthenticationURL(authenticationCode));
		token = StringUtils.removeEnd(
					StringUtils.removeStart(token, "access_token="),
					"&expires=5180795");

		return token;
	}

	@Override
	protected AuthenticationResult validateService(AuthenticationResult ar)
	{
		boolean success = false;
		String authenticationCode = null;
		String accessToken = null;

		authenticationCode = _broker.getParameter("code");

		// If there was an error in the token info, abort.
		if(StringUtils.isBlank(authenticationCode)){
			ar.addStatus(500, "Authentication code was empty or null.");
            return ar;
		}
		
		accessToken = getAccessToken(authenticationCode);

		// Make sure the token was created properly
		if(StringUtils.isBlank(accessToken)){
			ar.addStatus(500, "Failed during token creation");
            return ar;
		}

		success = getFacebookData(accessToken, ar);

		if(!success){
			ar.addStatus(401, "Could not fetch user data from facebook.");
			return ar;
		}

		ar.addStatus(200, "Successfully validated user.");
		ar.setSuccess(true);

		return ar;
	}


	private boolean getFacebookData(String accessToken, AuthenticationResult ar)
	{
		String responseBody = HttpConnectionHelper.getBasicResponse(getDataURL(accessToken));

		if(StringUtils.isBlank(responseBody)) return false;
		
		/*
		 * Returned JSON Object:
		 * {
		 * 	"id":"1317079204",
		 * 	"name":"Robi Brunner",
		 * 	"first_name":"Robi",
		 * 	"last_name":"Brunner",
		 * 	"link":"http://www.facebook.com/robi.brunner",
		 * 	"username":"robi.brunner",
		 * 	"email":"subdigit@yahoo.com",
		 * 	"timezone":-5,
		 * 	"locale":"en_US",
		 * 	"verified":true,
		 * 	"updated_time":"2012-11-15T12:59:27+0000"
		 * }
		 */
		
		JsonObject jsonObject = new JsonParser().parse(responseBody).getAsJsonObject();
		String facebookId = jsonObject.get("id").getAsString();
		String link = jsonObject.get("link").getAsString();
		String userName = jsonObject.get("username").getAsString();
		String fullName = jsonObject.get("name").getAsString();
		String firstName = jsonObject.get("first_name").getAsString();
		String lastName = jsonObject.get("last_name").getAsString();
		String email = jsonObject.get("email").getAsString();

		// The primary key we need to index against the user in the local data store.
		ar.setServiceUserID(facebookId);

		ar.addData(KEY_PROFILEURL, link);
		ar.addData(KEY_IMAGEURL, "https://graph.facebook.com/" + userName + "/picture");
		ar.addData(KEY_DISPLAYNAME, fullName);
		
		ar.addData("userName", userName);
		ar.addData("firstname", firstName);
		ar.addData("lastname", lastName);
		ar.addData("email", email);
		ar.addData("accesstoken", accessToken);

		ar.setReturnData(jsonObject);

		return true;
	}

    
	@Override
	protected AuthenticationResult disconnectService(AuthenticationResult ar)
	{
		ar.addStatus(200, "Successfully disconnected");
		ar.setSuccess(true);
		
		return ar;
	}
}
