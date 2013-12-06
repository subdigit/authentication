package com.subdigit.auth.service;

import com.subdigit.auth.conf.AuthenticationServiceConfiguration;
import com.subdigit.auth.result.AuthenticationResult;
import com.subdigit.broker.RequestResponseBroker;


public class AuthenticationServiceHelper
{
	public static AuthenticationService getAuthenticationService(String service, RequestResponseBroker<?,?,?> broker) throws IllegalArgumentException
	{
		AuthenticationService as = null;

		if(service == null) throw new IllegalArgumentException("Null service name passed in.");

		as = AuthenticationServiceConfiguration.getInstance().newServiceInstance(service);
		
		if(as == null) throw new IllegalArgumentException("Service: " + service + " could not be instantiated.");

		as.initialize(broker);

		return as;
	}


	public static AuthenticationResult newAuthenticationResults(String service)
	{
		return new AuthenticationResult(service);
	}
}
