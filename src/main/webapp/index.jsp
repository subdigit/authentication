<%@page contentType="text/html" pageEncoding="UTF-8"%><%@page import="com.subdigit.auth.AuthenticationResults,com.subdigit.auth.conf.AuthenticationConfiguration,com.subdigit.auth.conf.AuthenticationServiceConfiguration,com.subdigit.utilities.ServletHelper" %><!DOCTYPE html>
<!-- The top of file index.html -->
<html itemscope itemtype="http://schema.org/Article">
<head>
  <!-- BEGIN Pre-requisites -->
<!--
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js">
  </script>
  <script type="text/javascript">
    (function () {
      var po = document.createElement('script');
      po.type = 'text/javascript';
      po.async = true;
      po.src = 'https://plus.google.com/js/client:plusone.js?onload=start';
      var s = document.getElementsByTagName('script')[0];
      s.parentNode.insertBefore(po, s);
    })();
  </script>
-->
  <!-- END Pre-requisites -->
  
  <style>
.container {
	background: none;
	position: relative;
	width: 680px;
	margin: 0 auto;
	padding: 60px 0 0;
}

#content {
	display: table;
	margin: auto;
	width: 100%;
	padding: 15px;
	background: #eee;
	border-radius: 5px;
}

#signindescription {
	width: 250px;
	float: right;
	margin-left: 10px;
	margin-right: 45px;
	color: #666;
}

#signinservices {
	width: 250px;
	margin-left: 45px;
	margin-right: 10px;
}

#signinservicesmessage {
	color: #666;
}

button {
	cursor: pointer;
	padding: 0 10px 0 10px;
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	-webkit-box-align: center;
	box-sizing: border-box;
}

button.signin {
	position: relative;
	display: block;
	border: 0;
	min-width: 242px;
	color: white;
	font-weight: bold;
	font-size: 14px;
	vertical-align: middle;
	margin: 0 0 20px 0px;
	height: 45px;
	text-align: left;
	border-radius: 2px;
	white-space: nowrap;
}

button.twitter {
	xxxbackground: #46a5e3;
	background: #00aced;
}

button.facebook {
	xxxbackground: #3d5382;
	background: #3b5998;
}

button.googleplus {
	xxxbackground: #dd4b39;
	background: #d03324;
}

button.twitter:hover {
	xxxbackground: #57b6f4;
	background: #11bdfe;
}

button.facebook:hover {
	xxxbackground: #4e6493;
	background: #4c6aa9;
}

button.googleplus:hover {
	xxxbackground: #ee5c4a;
	background: #e14435;
}

#signinresults {
	background: #fff;
	margin-top: 15px;
	border-radius: 5px;
	padding: 10px;
	color: #666;
}

img.center {
	vertical-align: middle;
}

img.profile {
	border-radius: 5px;
}
  </style>
</head>
<body>
<div class="container">
<div id="content">
<div id="signindescription">
Just a quick experiment with 3rd party authentications.  The system will redirect to the respective party's authentication system and request for application permission.  Once granted, it will redirect back to the callback servlet and process the information.
<br /><br />If all goes well, you should see some relevant personal data below.
</div>
<div id="signinservices">

<% if(AuthenticationServiceConfiguration.getInstance().getAvailableServicesCount() > 0){ %>
	<% for(String service : AuthenticationServiceConfiguration.getInstance().getAvailableServices()){ %>
<form action="<%=AuthenticationConfiguration.getInstance().getApplicationServiceLoginURL(service, false, true)%>">
	<button type="submit" class="signin <%=service%>">
		<span class='signin-icon <%=service%>'></span><span class="signin-text"><img class="center" width="32" height="32" border="0" src="images/icons/<%=service%>.png"/> Connect with <%=AuthenticationServiceConfiguration.getInstance().getServiceName(service)%></span>
	</button>
</form>
	<% } %>
<% } else { %>
<div id="signinservicesmessage">
<h2>No services available!</h2>
Hmm, did you configure your secrets properly in the property file?  How about making sure .enabled is true?
</div>
<% } %>


</div>
<br clear="all" />
<div id="signinresults">
<br />
<%
AuthenticationResults ar = (AuthenticationResults) ServletHelper.getAttribute(AuthenticationConfiguration.getInstance().getAttributeResults(), request);
if(ar != null && ar.hasServiceUserID()){
	String profileURL = (String) ar.getVariable("profileurl");
	String imageURL = (String) ar.getVariable("imageurl");
	String displayName = (String) ar.getVariable("displayname");
%>
<img class="profile center" src="<%=imageURL%>" width="50" height="50" border="0" /> Hi <%=displayName%>.  Thanks for logging in with your <%=AuthenticationServiceConfiguration.getInstance().getServiceName(ar.getService())%> account: <a href="<%=profileURL%>"><%=ar.getServiceUserID()%></a>
<%
} else {
%>Not logged in.  Pick a service from above!<%
}
%>
<br />
<br />

<!-- Add where you want your sign-in button to render -->
<div id="signinButton">
  <span class="g-signin"
    data-scope="https://www.googleapis.com/auth/plus.login"
    data-clientid="813702056983.apps.googleusercontent.com"
    data-redirecturi="postmessage"
    data-accesstype="offline"
    data-cookiepolicy="single_host_origin"
    data-callback="signInCallback">
  </span>
</div>
<div id="result"></div>
</div>
</div>
</div>



<!-- Last part of BODY element in file index.html -->
<script type="text/javascript">
function signInCallback(authResult) {
  if (authResult['code']) {

    // Hide the sign-in button now that the user is authorized, for example:
    $('#signinButton').attr('style', 'display: none');

    // Send the code to the server
    $.ajax({
      type: 'POST',
      url: '/authentication/callback?via=googleplus&code=' + authResult['code'],
//      contentType: 'application/octet-stream; charset=utf-8',
      contentType: 'text/plain; charset=utf-8',
      success: function(result) {
        // Handle or verify the server response if necessary.

        // Prints the list of people that the user has allowed the app to know
        // to the console.
        console.log(result);
        if (result['profile'] && result['people']){
          $('#results').html('Hello ' + result['profile']['displayName'] + '. You successfully made a server side call to people.get and people.list');
        } else {
          $('#results').html('Failed to make a server-side call. Check your configuration and console.');
        }
      },
      processData: false
    });
  } else if (authResult['error']) {
    // There was an error.
    // Possible error codes:
    //   "access_denied" - User denied access to your app
    //   "immediate_failed" - Could not automatially log in the user
    // console.log('There was an error: ' + authResult['error']);
  }
}
</script>
  </body>
</html>
