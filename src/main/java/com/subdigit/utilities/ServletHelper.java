package com.subdigit.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ServletHelper
{
	private ServletHelper(){}


	public static String getAttributeString(String key, HttpServletRequest request){ return (String) getAttribute(key, request); }
	public static Object getAttribute(String key, HttpServletRequest request)
	{
		if(request == null) return null;
		else return request.getSession().getAttribute(key);
	}


	public static void setAttribute(String key, Object value, HttpServletRequest request)
	{
		if(request == null) return;
		request.getSession().setAttribute(key, value);
	}


	public static void removeAttribute(String key, HttpServletRequest request)
	{
		if(request == null) return;
		request.getSession().removeAttribute(key);
	}


	public static String getBasicResponse(String url)
	{
		String responseBody = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
/*
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };
*/		

		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (IOException e){
		} finally {
			try {
				httpclient.close();
			} catch (IOException e){}
		}

        return responseBody;
    }
	

	public static boolean redirect(String url, HttpServletResponse response)
	{
		if(url == null || url.trim().length() == 0) return false;

		try {
			response.sendRedirect(url);
			return true;
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return false;
	}


	public static String encode(String value){ return encode(value, "UTF-8"); }
	public static String encode(String value, String charset)
	{
		String encoded = null;
		
		try {
			encoded = URLEncoder.encode(value, charset);
		} catch (UnsupportedEncodingException e){
			encoded = null;
		}
		
		return encoded;
	}


	public static String decode(String value){ return decode(value, "UTF-8"); }
	public static String decode(String value, String charset)
	{
		String decoded = null;
		
		try {
			decoded = URLDecoder.decode(value, charset);
		} catch (UnsupportedEncodingException e){
			decoded = null;
		}
		
		return decoded;
	}
}
