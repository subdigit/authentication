package com.subdigit.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subdigit.auth.AuthenticationResults;
import com.subdigit.auth.conf.AuthenticationServiceConfiguration;


public class AuthenticationServiceHelper
{
	public static AuthenticationService getAuthenticationService(String service, HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException
	{
		AuthenticationService as = null;

		if(service == null) throw new IllegalArgumentException("Null service name passed in.");

		as = AuthenticationServiceConfiguration.getInstance().newServiceInstance(service);
		
		if(as == null) throw new IllegalArgumentException("Service: " + service + " could not be instantiated.");

		as.initialize(request, response);

		return as;
	}


	public static AuthenticationResults newAuthenticationResults(String service)
	{
		return new AuthenticationResults(service);
	}
}
