package com.subdigit.broker;

import java.util.Map;


public interface RequestResponseBroker<RequestType, ResponseType>
{
	public boolean reinitialize();
	public boolean initialize(RequestType request, ResponseType response);
	public boolean initialized();

	public RequestType getRequest();
	public ResponseType getResponse();

	public String getAttributeString(String key);
	public Object getAttribute(String key);

	public void setAttribute(String key, Object value);

	public void removeAttribute(String key);

	public String getParameter(String key);
	public String getPathInfo();
	
	public boolean redirect(String url);
	
    public Map<String,String> extractRequestData();
}
