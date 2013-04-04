package com.subdigit.auth.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subdigit.auth.AuthenticationHelper;
import com.subdigit.auth.AuthenticationResults;
import com.subdigit.auth.conf.AuthenticationConfiguration;
import com.subdigit.utilities.ServletHelper;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet(
		description = "Logs the user out of the application.", 
		urlPatterns = { 
			"/logout",
			"/logout/*"
		})
public class LogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogoutServlet()
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
		AuthenticationResults ar = null;
		AuthenticationHelper ah = new AuthenticationHelper(request, response);
		
		// Ask the AuthenticationHelper to logout the user.
		// If something went wrong, check the codes in the AuthentcationResults to figure out what to do.
		ar = ah.disconnect();

		// Were we successful in initiating the request.  This is not about _completing_, just initiating.
		if(ar == null || !ar.success()){
System.err.println(ar.printDiagnostics());
			throw(new ServletException());
		}

		if(ar.hasRedirectURL()){
			ServletHelper.redirect(ar.getRedirectURL(), response);
		} else {
			ServletHelper.redirect(AuthenticationConfiguration.getInstance().getApplicationURL(), response);
		}

System.err.println(ar.printDiagnostics());
	}
}
