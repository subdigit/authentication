package com.subdigit.auth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.subdigit.auth.conf.AuthenticationConfiguration;
import com.subdigit.auth.service.AuthenticationService;
import com.subdigit.auth.service.AuthenticationServiceHelper;
import com.subdigit.utilities.ServletHelper;

public class AuthenticationHelper
{
	protected HttpServletRequest _request;
	protected HttpServletResponse _response;
	protected AuthenticationConfiguration _ac;


	public AuthenticationHelper(HttpServletRequest request, HttpServletResponse response)
	{
		initialize(request, response);
	}

	
	public boolean initialize(HttpServletRequest request, HttpServletResponse response)
	{
		boolean success = false;

		_request = request;
		_response = response;
		_ac = AuthenticationConfiguration.getInstance();

		if(_request != null && _response != null) success = true;

		return success;
	}


	public AuthenticationResults connect() throws ServletException { return connect(extractService()); }
	public AuthenticationResults connect(String service) throws ServletException { return service(Request.CONNECT, service); }

	public AuthenticationResults validate() throws ServletException { return validate(extractService()); }
	public AuthenticationResults validate(String service) throws ServletException { return service(Request.VALIDATE, service); }

	public AuthenticationResults disconnect() throws ServletException { return disconnect(extractService()); }
	public AuthenticationResults disconnect(String service) throws ServletException { return service(Request.DISCONNECT, service); }

	protected AuthenticationResults service(Request request) throws ServletException { return service(request, _request.getParameter(_ac.getParameterVia())); }
	protected AuthenticationResults service(Request request, String service) throws ServletException
	{
		AuthenticationService authService = AuthenticationServiceHelper.getAuthenticationService(service, _request, _response);
		AuthenticationResults authResults = AuthenticationServiceHelper.newAuthenticationResults(service);

		if(authService != null){
			switch(request){
				case CONNECT:
					authResults = authService.connect();
					break;
				case VALIDATE:
					authResults = authService.validate();
					break;
				case DISCONNECT:
					authResults = authService.disconnect();
					break;
				default:
			}

			if(authResults == null){
				authResults = AuthenticationServiceHelper.newAuthenticationResults(service);
				authResults.addStatus(500, request + ": Invalid results returned for service: " + service);
			}
		} else authResults.addStatus(404, "Authentication service '" + service + "' not found");

		// Save the results for use outside of this scope.
		ServletHelper.setAttribute(_ac.getAttributeResults(), authResults, _request);

		return authResults;
	}

	
	private String extractService()
	{
		String service = null;

		if(_ac.isApplicationRestStyleEndpoint()){
			service = StringUtils.substringBefore(StringUtils.substringAfter(_request.getPathInfo(), "/"), "/");
		} else {
			service = _request.getParameter(_ac.getParameterVia());
		}

		return service;
	}

	private enum Request
	{
		CONNECT,
		VALIDATE,
		DISCONNECT;
	}
}
