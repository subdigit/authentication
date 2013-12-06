package com.subdigit.auth.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subdigit.auth.AuthenticationHelper;
import com.subdigit.broker.servlet.ServletRequestResponseBroker;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(
		description = "Triggers logins using various authentication sources like Twitter, Facebook, and Google+.",
		urlPatterns = {
			"/login",
			"/login/*",
			"/connect",
			"/connect/*"
		})
public class LoginServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet()
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
		ah.handleLogin();
	}
}
