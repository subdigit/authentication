package com.subdigit.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subdigit.auth.AuthenticationResults;

public interface AuthenticationService
{
	public boolean initialize(HttpServletRequest request, HttpServletResponse response);

	public AuthenticationResults connect();
	public AuthenticationResults validate();
	public AuthenticationResults disconnect();
}
