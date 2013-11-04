package com.subdigit.auth.service;

import com.subdigit.auth.AuthenticationResults;
import com.subdigit.utilities.RequestResponseBroker;

public interface AuthenticationService
{
	public static String KEY_PROFILEURL		= "profileurl";
	public static String KEY_IMAGEURL		= "imageurl";
	public static String KEY_DISPLAYNAME	= "displayname";

	public boolean initialize(RequestResponseBroker<?,?> broker);

	public AuthenticationResults connect();
	public AuthenticationResults validate();
	public AuthenticationResults disconnect();
}
