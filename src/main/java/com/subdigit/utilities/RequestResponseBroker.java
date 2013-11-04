package com.subdigit.utilities;

import java.util.Map;


public interface RequestResponseBroker<T,K>
{
	public boolean initialized();

	public T getRequest();
	public K getResponse();

	public String getAttributeString(String key);
	public Object getAttribute(String key);

	public void setAttribute(String key, Object value);

	public void removeAttribute(String key);

	public String getParameter(String key);
	public String getPathInfo();
	
	public boolean redirect(String url);
	
    public Map<String,String> extractRequestData();
}
