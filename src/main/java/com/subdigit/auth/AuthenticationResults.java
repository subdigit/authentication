package com.subdigit.auth;

import java.util.HashMap;
import java.util.LinkedList;


public class AuthenticationResults
{
	public int DEFAULT_CODE						= -1;
	public String DEFAULT_MESSAGE				= null;
	public Exception DEFAULT_EXCEPTION			= null;
	public MessageLevel DEFAULT_MESSAGELEVEL	= MessageLevel.INFO;

	boolean _initialized;
	String _service;
	boolean _success;
	String _redirectURL;
	String _state;
	String _serviceUserID;
	Object _returnData;
	LinkedList<Status> _statusArchive;
	HashMap<String, Object> _dataStore;

	public AuthenticationResults(String service)
	{
		_initialized = false;
		_initialized = initialize(service);
		
		// We should probably throw an exception here is the service is invalid
		// to prevent the instance from being used.
//		throw InvalidServiceException();
	}

	
	public boolean initialize(String service)
	{
		reset();
		
		_service = service;

		if(_service == null) throw new IllegalArgumentException("Service not specified upon initialization");

		return true;
	}


	private void reset()
	{
		_service = null;
		_success = false;
		_redirectURL = null;
		_state = null;
		_serviceUserID = null;
		_returnData = null;
		_statusArchive = initializeStatusArchive();
		_dataStore = new HashMap<String, Object>();
	}

	
	private LinkedList<Status> initializeStatusArchive()
	{
		return new LinkedList<Status>();
	}
	
	public boolean isInitialized(){ return _initialized; }

	public String getService(){ return _service; }
//	private void setService(String value){ _service = value; }

	public boolean getSuccess(){ return _success; }
	public void setSuccess(boolean value){ _success = value; }
	public boolean isSuccess(){ return getSuccess(); }
	public boolean success(){ return isSuccess(); }

	public String getRedirectURL(){ return _redirectURL; }
	public void setRedirectURL(String value){ _redirectURL = value; }
	public boolean hasRedirectURL(){ return (getRedirectURL() == null)?false:true; }

	public String getState(){ return _state; }
	public void setState(String value){ _state = value; }
	public boolean hasState(){ return (getState() == null)?false:true; }

	public String getServiceUserID(){ return _serviceUserID; }
	public void setServiceUserID(String value){ _serviceUserID = value; }
	public boolean hasServiceUserID(){ return (getServiceUserID() == null)?false:true; }

	public Object getReturnData(){ return _returnData; }
	public void setReturnData(Object value){ _returnData = value; }
	public boolean hasReturnData(){ return (getReturnData() == null)?false:true; }
	
	public HashMap<String,Object> getDataStore(){ return _dataStore; }
	public Object addVariable(String key, Object value){ if(key != null && _dataStore != null) return _dataStore.put(key, value); else return null; }
	public Object getVariable(String key){ return _dataStore.get(key); }
	public int getDataCount()
	{
		return (_dataStore != null) ? _dataStore.size() : -1;
	}


	public int getCode()
	{
		Status status = getRecentStatus();
		if(status != null) return status.getCode();
		else return DEFAULT_CODE;
	}

	public String getMessage()
	{
		Status status = getRecentStatus();
		if(status != null) return status.getMessage();
		else return DEFAULT_MESSAGE;
	}
	public boolean hasMessage(){ return (getMessage() == null)?false:true; }


	public Exception getException()
	{
		Status status = getRecentStatus();
		if(status != null) return status.getException();
		else return DEFAULT_EXCEPTION;
	}
	public boolean hasException(){ return (getException() == null)?false:true; }


	public MessageLevel getMessageLevel()
	{
		Status status = getRecentStatus();
		if(status != null) return status.getMessageLevel();
		else return DEFAULT_MESSAGELEVEL;
	}
	public boolean hasMessageLevel(){ return (getMessage() == null)?false:true; }
	
	
	public void addStatus(int code, String message)
	{
		addStatus(code, message, (Exception) null);
	}

	public void addStatus(int code, String message, Exception e)
	{
		addStatus(code, message, e, null);
	}

	public void addStatus(int code, String message, MessageLevel messageLevel)
	{
		addStatus(code, message, null, messageLevel);
	}

	public void addStatus(int code, String message, Exception e, MessageLevel messageLevel)
	{
		Status status = new Status(code, message, e, messageLevel);
		if(_statusArchive == null) _statusArchive = initializeStatusArchive();

		_statusArchive.add(status);
	}
	
	public Status getRecentStatus()
	{
		Status status = null;

		if(_statusArchive != null && _statusArchive.size() > 0) status = _statusArchive.getLast();

		return status;
	}
	
	public int getStatusCount()
	{
		return (_statusArchive != null) ? _statusArchive.size() : -1;
	}
	public LinkedList<Status> getStatusArchive(){ return _statusArchive; }

	public String printDiagnostics()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append("Initialized: " + isInitialized() + "\n");
		buffer.append("Success: " + success() + "\n");
		buffer.append("State: " + getState() + "\n");
		buffer.append("Redirect URL: " + getRedirectURL() + "\n");
		buffer.append("Service: " + getService() + "\n");
		buffer.append("Service User ID: " + getServiceUserID() + "\n");

		buffer.append("Return Data: " + getReturnData() + "\n");
		
		buffer.append("Code: " + getCode() + "\n");
		if(hasMessage()) buffer.append("Message: " + getMessage() + "\n");
		if(hasException()) buffer.append("Exception: " + getException() + "\n");
		if(hasMessageLevel()) buffer.append("Message Level: " + getMessageLevel() + "\n");

		buffer.append("Status count: " + getStatusCount() + "\n");
		buffer.append("Data count: " + getDataCount() + "\n");

		return buffer.toString();
	}


	public enum MessageLevel
	{
		DEBUG,
		INFO,
		WARNING,
		FATAL;
	}


	public class Status
	{
		private int _code;
		private String _message;
		private Exception _exception;
		private MessageLevel _messageLevel;

		public Status()
		{
			initialize(DEFAULT_CODE, DEFAULT_MESSAGE, DEFAULT_EXCEPTION, DEFAULT_MESSAGELEVEL);
		}
		
		
		public Status(int code)
		{
			initialize(code, DEFAULT_MESSAGE, DEFAULT_EXCEPTION, DEFAULT_MESSAGELEVEL);
		}

		
		public Status(int code, String message, Exception e)
		{
			initialize(code, message, e, DEFAULT_MESSAGELEVEL);
		}

		
		public Status(int code, String message, MessageLevel messageLevel)
		{
			initialize(code, message, DEFAULT_EXCEPTION, messageLevel);
		}

		
		public Status(int code, String message)
		{
			initialize(code, message, DEFAULT_EXCEPTION, DEFAULT_MESSAGELEVEL);
		}

		
		public Status(int code, String message, Exception e, MessageLevel messageLevel)
		{
			initialize(code, message, e, messageLevel);
		}

		
		public boolean initialize(int code, String message, Exception e, MessageLevel messageLevel)
		{
			reset();

			_code = code;
			_message = message;
			if(e != null) _exception = e;
			if(messageLevel != null) _messageLevel = messageLevel;
			
			return true;
		}

	
		public void reset()
		{
			_code = DEFAULT_CODE;
			_message = DEFAULT_MESSAGE;
			_exception = DEFAULT_EXCEPTION;
			_messageLevel = DEFAULT_MESSAGELEVEL;
		}


		public int getCode(){ return _code; }
		public void setCode(int value){ _code = value; }
		
		public String getMessage(){ return _message; }
		public void setMessage(String value){ _message = value; }
		public boolean hasMessage(){ return (getMessage() == null)?false:true; }
		
		public Exception getException(){ return _exception; }
		public void setException(Exception value){ _exception = value; }
		public boolean hasException(){ return (getException() == null)?false:true; }

		public MessageLevel getMessageLevel(){ return _messageLevel; }
		public void setMessageLevel(MessageLevel value){ _messageLevel = value; }
	}
}
