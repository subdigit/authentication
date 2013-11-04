package com.subdigit.auth.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subdigit.auth.AuthenticationHelper;
import com.subdigit.utilities.ServletRequestResponseBroker;

/**
 * Servlet implementation class DisconnectServlet
 */
@WebServlet(
		description = "Disconnects 3rd party services from the application.", 
		urlPatterns = { 
			"/disconnect",
			"/disconnect/*"
		})
public class DisconnectServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DisconnectServlet()
	{
		super();
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		AuthenticationHelper ah = new AuthenticationHelper(new ServletRequestResponseBroker(request, response));
		ah.handleDisconnect();
	}
}
