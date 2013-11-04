package com.subdigit.utilities;

public abstract class AbstractRequestResponseBroker<T,K> implements RequestResponseBroker<T,K>
{
	public String getAttributeString(String key){ return (String) getAttribute(key); }
}
