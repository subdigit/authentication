package com.subdigit.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.subdigit.auth.AuthenticationResults;
import com.subdigit.utilities.RequestResponseBroker;

public class TwitterAuthenticationService extends AbstractAuthenticationService
{
	private static final String SERVICE_IDENTIFIER = "twitter";

	public TwitterAuthenticationService(){ super(); }
	public TwitterAuthenticationService(RequestResponseBroker<HttpServletRequest, HttpServletResponse> broker){ super(broker); }


	@Override
	public String getIdentifier(){ return SERVICE_IDENTIFIER; }


	@Override
	protected AuthenticationResults connectService(AuthenticationResults ar)
	{
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(getServiceApplicationID(), getServiceApplicationSecret());

		_broker.setAttribute("twitter", twitter);

        try {
        	RequestToken requestToken = twitter.getOAuthRequestToken(getCallbackURL());

        	if(requestToken == null){
                ar.addStatus(401, "Could not generate requestToken for service: " + getIdentifier());
                return ar;
        	}

        	_broker.setAttribute("requestToken", requestToken);
 
        	ar.addStatus(200, "Redirected to authentication URL: " + requestToken.getAuthenticationURL());
            ar.setRedirectURL(requestToken.getAuthenticationURL());
			ar.setSuccess(true);

        } catch (TwitterException e) {
        	ar.addStatus(500, "TwitterException thrown while attempting to redirect to authentication", e); 
        	 return ar;
        }

        return ar;
	}


	@Override
	protected AuthenticationResults validateService(AuthenticationResults ar)
	{
		Twitter twitter = (Twitter) _broker.getAttribute("twitter");
		User user = null;
		RequestToken requestToken = (RequestToken) _broker.getAttribute("requestToken");
		String verifier = _broker.getParameter("oauth_verifier");

		try {
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			_broker.removeAttribute("requestToken");

			// Make sure the token was created properly
			if(accessToken == null){
				ar.addStatus(500, "Failed during token creation");
	            return ar;
			}

			user = twitter.verifyCredentials();
			
			// Save the token for later use
			// storeAccessToken(user.getId() , accessToken.getToken(), accessToken.getTokenSecret());

			// The primary key we need to index against the user in the local data store.
			ar.setServiceUserID("" + user.getId());
			ar.addVariable(KEY_PROFILEURL, user.getURL());
			ar.addVariable(KEY_IMAGEURL, user.getProfileImageURL());
			ar.addVariable(KEY_DISPLAYNAME, user.getName());

			ar.addVariable("screenname", user.getScreenName());
			ar.addVariable("token", accessToken.getToken());
			ar.addVariable("tokensecret", accessToken.getTokenSecret());

			ar.setReturnData(twitter);

		} catch (TwitterException e){
        	ar.addStatus(500, "TwitterException thrown while attempting to redirect to authentication", e);
    		return ar;
		}
		
		ar.addStatus(200, "Successfully validated user.");
		ar.setSuccess(true);

		return ar;
	}


/*
	private Twitter getAuthenticatedTwitterInstance(String twitterID)
	{
		AccessToken accessToken = loadAccessToken(twitterID);

		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(getApplicationID(), getApplicationSecret());
		twitter.setOAuthAccessToken(accessToken);
//		twitter.verifyCredentials();

		return twitter;
	}


	// http://twitter4j.org/en/code-examples.html#signinwithtwitter
	private AccessToken loadAccessToken(String twitterID)
	{
		String token = null; // load from a persistent store
		String tokenSecret = null; // load from a persistent store

		return new AccessToken(token, tokenSecret);
	}
*/


	@Override
	protected AuthenticationResults disconnectService(AuthenticationResults ar)
	{
		ar.addStatus(200, "Successfully disconnected");
		ar.setSuccess(true);

		return ar;
	}
}
