package com.subdigit.basic;

import java.util.HashMap;
import java.util.LinkedList;


public class BasicResult
{
	public static final int DEFAULT_CODE				= -1;
	public static final String DEFAULT_MESSAGE			= null;
	public static final Exception DEFAULT_EXCEPTION		= null;
	public static final StatusLevel DEFAULT_STATUSLEVEL	= StatusLevel.INFO;

	protected boolean _initialized;
	protected boolean _success;
	protected Object _returnData;
	LinkedList<Status> _statusArchive;
	HashMap<String, Object> _dataStore;

	public BasicResult()
	{
		_initialized = false;
		_initialized = initialize();

		// We should probably throw an exception here if the service is invalid
		// to prevent the instance from being used.
//		if(!_initialized) throw InvalidServiceException();
	}

	
	public final boolean initialize()
	{
		reset();

		return true;
	}


	protected void reset()
	{
		_success = false;
		_returnData = null;
		_statusArchive = initializeStatusArchive();
		_dataStore = initializeDataStore();
	}

	
	private LinkedList<Status> initializeStatusArchive()
	{
		return new LinkedList<Status>();
	}
	
	private HashMap<String, Object> initializeDataStore()
	{
		return new HashMap<String, Object>();
	}

	
	public boolean isInitialized(){ return _initialized; }
	public boolean initialized(){ return isInitialized(); }

	public boolean getSuccess(){ return _success; }
	public void setSuccess(boolean value){ _success = value; }
	public boolean isSuccess(){ return getSuccess(); }
	public boolean success(){ return isSuccess(); }

	public Object getReturnData(){ return _returnData; }
	public void setReturnData(Object value){ _returnData = value; }
	public boolean hasReturnData(){ return (getReturnData() == null)?false:true; }

	public HashMap<String,Object> getDataStore(){ return _dataStore; }
	public void addVariable(String key, Object value){ if(key != null && _dataStore != null) _dataStore.put(key, value); }
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


	public StatusLevel getMessageLevel()
	{
		Status status = getRecentStatus();
		if(status != null) return status.getStatusLevel();
		else return DEFAULT_STATUSLEVEL;
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

	public void addStatus(int code, String message, StatusLevel messageLevel)
	{
		addStatus(code, message, null, messageLevel);
	}

	public void addStatus(int code, String message, Exception e, StatusLevel messageLevel)
	{
		Status status = new Status();
		status.setCode(code);
		status.setMessage(message);
		status.setException(e);
		status.setStatusLevel(messageLevel);

		addStatus(status);
	}
	
	public void addStatus(Status value)
	{
		if(value == null) return;

		if(_statusArchive == null) _statusArchive = initializeStatusArchive();

		_statusArchive.add(value);
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

		buffer.append("Return Data: " + getReturnData() + "\n");
		
		buffer.append("Code: " + getCode() + "\n");
		if(hasMessage()) buffer.append("Message: " + getMessage() + "\n");
		if(hasException()) buffer.append("Exception: " + getException() + "\n");
		if(hasMessageLevel()) buffer.append("Message Level: " + getMessageLevel() + "\n");

		buffer.append("Status count: " + getStatusCount() + "\n");
		buffer.append("Data count: " + getDataCount() + "\n");

		return buffer.toString();
	}


	public enum StatusLevel
	{
		DEBUG,
		INFO,
		WARNING,
		FATAL;

		// I like my enums printed out in lower case.
		public String toString(){ return name().toLowerCase(); }  
	}


	public class Status
	{
		private int _code					= DEFAULT_CODE;
		private String _message				= DEFAULT_MESSAGE;
		private Exception _exception		= DEFAULT_EXCEPTION;
		private StatusLevel _statusLevel	= DEFAULT_STATUSLEVEL;

		public int getCode(){ return _code; }
		public void setCode(int value){ _code = value; }

		public String getMessage(){ return _message; }
		public void setMessage(String value){ _message = value; }
		public boolean hasMessage(){ return (getMessage() == null)?false:true; }

		public Exception getException(){ return _exception; }
		public void setException(Exception value){ _exception = value; }
		public boolean hasException(){ return (getException() == null)?false:true; }

		public StatusLevel getStatusLevel(){ return _statusLevel; }
		public void setStatusLevel(StatusLevel value){ _statusLevel = value; }
	}
}
