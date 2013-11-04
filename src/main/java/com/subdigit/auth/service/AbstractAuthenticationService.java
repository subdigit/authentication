package com.subdigit.auth.service;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subdigit.auth.AuthenticationResults;
import com.subdigit.auth.conf.AuthenticationConfiguration;
import com.subdigit.auth.conf.AuthenticationServiceConfiguration;
import com.subdigit.utilities.RequestResponseBroker;

public abstract class AbstractAuthenticationService implements AuthenticationService
{
	protected RequestResponseBroker<?,?> _broker;
	protected AuthenticationConfiguration _ac;
	protected AuthenticationServiceConfiguration _asc;


	public AbstractAuthenticationService()
	{
		initialize(null);
	}


	public AbstractAuthenticationService(RequestResponseBroker<HttpServletRequest, HttpServletResponse> broker)
	{
		initialize(broker);
	}

	
	public boolean initialize(RequestResponseBroker<?,?> broker)
	{
		boolean success = false;

		_broker = broker;
		_ac = AuthenticationConfiguration.getInstance();
		_asc = AuthenticationServiceConfiguration.getInstance();
		
		if(_broker != null && _broker.initialized()) success = true;

		return success;
	}


	public AuthenticationResults newAuthenticationResults(){ return new AuthenticationResults(getIdentifier()); }

	public AuthenticationResults connect()
	{
		AuthenticationResults ar = newAuthenticationResults();

		if(enforceStateCheck()){
			String state = new BigInteger(130, new SecureRandom()).toString(32);
			_broker.setAttribute(getStateCheckParameter(), state);
			ar.setState(state);
		}
		
		ar = connectService(ar);

		return ar;
	}


	public AuthenticationResults validate()
	{
		AuthenticationResults ar = newAuthenticationResults();

		if(enforceStateCheck()){
			String state = _broker.getParameter(getStateCheckParameter());

			// Make sure we are talking about the same request.
			if(state != null && !state.equals(_broker.getAttributeString(getStateCheckParameter()))){
				ar.addStatus(401, "State mismatch.  Man in the middle attack?");
				return ar;
			}
		}

		ar = validateService(ar);
		
		return ar;
	}

	
	public AuthenticationResults disconnect()
	{
		AuthenticationResults ar = newAuthenticationResults();
		
		ar = disconnectService(ar);
		
		return ar;
	}


	public String getApplicationName(){ return _ac.getApplicationName(); }
	public String getApplicationURL(){ return _ac.getApplicationURL(); }
	public String getCallbackURL(){ return _ac.getApplicationServiceCallbackURL(getIdentifier()); }

	public String getStateCheckParameter(){ return _ac.getParameterState(); }

	public String getServiceApplicationID(){ return _asc.getServiceAppID(getIdentifier()); }
	public String getServiceApplicationSecret(){ return _asc.getServiceAppSecret(getIdentifier()); }
	public boolean enforceStateCheck(){ return _asc.getServiceStateCheck(getIdentifier()); }


	public abstract String getIdentifier();
	protected abstract AuthenticationResults connectService(AuthenticationResults ar);
	protected abstract AuthenticationResults validateService(AuthenticationResults ar);
	protected abstract AuthenticationResults disconnectService(AuthenticationResults ar);
}
