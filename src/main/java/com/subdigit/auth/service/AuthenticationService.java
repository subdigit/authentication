package com.subdigit.auth.service;

import com.subdigit.auth.result.AuthenticationResult;
import com.subdigit.broker.RequestResponseBroker;

public interface AuthenticationService
{
	public static String KEY_PROFILEURL		= "profileurl";
	public static String KEY_IMAGEURL		= "imageurl";
	public static String KEY_DISPLAYNAME	= "displayname";

	public boolean initialize(RequestResponseBroker<?,?,?> broker);

	public AuthenticationResult connect();
	public AuthenticationResult validate();
	public AuthenticationResult disconnect();
}
