<%@page contentType="text/html" pageEncoding="UTF-8"%><%@page import="com.subdigit.auth.AuthenticationResults,com.subdigit.auth.conf.AuthenticationConfiguration,com.subdigit.auth.conf.AuthenticationServiceConfiguration,com.subdigit.utilities.ServletHelper" %><!DOCTYPE html>
<!-- The top of file index.html -->
<html itemscope itemtype="http://schema.org/Article">
<head>
<!-- BEGIN Pre-requisites -->
  <meta http-equiv="X-UA-Compatible" content="IE=Edge">

  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

<% if(AuthenticationServiceConfiguration.getInstance().isServiceEnabled("facebook")){ %>
  <script type="text/javascript">
    // Facebook beautification fix
    // http://stackoverflow.com/a/10068176/223362
  	$(window).on('load', function(e){
  	  if(window.location.hash == '#_=_'){
  	    window.location.hash = ''; // for older browsers, leaves a # behind
  	    history.pushState('', document.title, window.location.pathname); // nice and clean
  	    e.preventDefault(); // no page reload
  	  }
  	});
    </script>
<% } %>


<% if(AuthenticationServiceConfiguration.getInstance().isServiceEnabled("googleplus")){ %>
<!--
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
<% } %>
  
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

<% if(AuthenticationServiceConfiguration.getInstance().isServiceEnabled("twitter")){ %>
button.twitter {
	background: #00aced;
}

button.twitter:hover {
	background: #11bdfe;
}
<% } %>

<% if(AuthenticationServiceConfiguration.getInstance().isServiceEnabled("facebook")){ %>
button.facebook {
	background: #3b5998;
}

button.facebook:hover {
	background: #4c6aa9;
}
<% } %>

<% if(AuthenticationServiceConfiguration.getInstance().isServiceEnabled("googleplus")){ %>
button.googleplus {
	background: #d03324;
}

button.googleplus:hover {
	background: #e14435;
}
<% } %>

<% if(AuthenticationServiceConfiguration.getInstance().isServiceEnabled("persona")){ %>
button.persona {
	background: #bbbbbb;
}

button.persona:hover {
	background: #cccccc;
}
<% } %>

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

<%
	if(AuthenticationServiceConfiguration.getInstance().getEnabledServicesCount() > 0){
%>
	<%
		for(String service : AuthenticationServiceConfiguration.getInstance().getEnabledServices()){
	%>
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
String profileURL = null;
String imageURL = null;
String displayName = null;
String email = null;
String service = null;

if(ar != null && ar.hasServiceUserID()){
	profileURL = (String) ar.getVariable("profileurl");
	imageURL = (String) ar.getVariable("imageurl");
	displayName = (String) ar.getVariable("displayname");
	email = (String) ar.getVariable("email");
	service = ar.getService();
	
	if(imageURL == null) imageURL = "images/icons/" + ar.getService() + ".png";
	if(displayName == null) displayName = "there";
%>
    <img class="profile center" src="<%=imageURL%>" width="50" height="50" border="0" /> Hi <%=displayName%>.  Thanks for logging in with your <%=AuthenticationServiceConfiguration.getInstance().getServiceName(ar.getService())%> account:
  <% if(profileURL != null){ %>
<a href="<%=profileURL%>"><%=ar.getServiceUserID()%></a>
  <% } else { %>
<%=ar.getServiceUserID()%>
  <% } %>
<% } else { %>
Not logged in.  Pick a service from above!
<% } %>
    <br />
    <br />

<!-- Google+ Button: Add where you want your sign-in button to render -->
<!-- 
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
-->

    </div>
  </div>
</div>

<% if(AuthenticationServiceConfiguration.getInstance().isServiceEnabled("googleplus")){ %>
<!-- Google+ integration -->
<!--
<script type="text/javascript">
function signInCallback(authResult) {
  if(authResult['code']){
    // Hide the sign-in button now that the user is authorized, for example:
    $('#signinButton').attr('style', 'display: none');

    // Send the code to the server
    $.ajax({
      type: 'POST',
      url: '/authentication/validate/googleplus',
      data: {code: authResult['code']},
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
-->
<% } %>

<% if(AuthenticationServiceConfiguration.getInstance().isServiceEnabled("persona")){ %>
<!-- Persona integration: https://developer.mozilla.org/en-US/docs/Persona/Quick_Setup -->
<script src="https://login.persona.org/include.js"></script>
<script type="text/javascript">
var currentService = null;
var currentUser = null;
<% if(email != null){ %>currentUser = '<%=email%>';<% } %>
<% if(service != null){ %>currentService = '<%=service%>';<% } %>

var signinLink = $("button.signin.persona");
if(signinLink){
  signinLink.click(function(){
    currentService = "persona";
    navigator.id.request();
    return false;
  });
}

if(currentService == "persona"){
var signoutLink = $("button.signout.persona");
if(signoutLink){
  signoutLink.click(function(){
    navigator.id.logout();
    return false;
  });
}
}

navigator.id.watch({
  loggedInUser: currentUser,
  onlogin: function(assertion) {
	  if(currentService == "persona"){
    // A user has logged in! Here you need to:
    // 1. Send the assertion to your backend for verification and to create a session.
    // 2. Update your UI.
    $.ajax({
      type: 'POST',
      url: '/authentication/validate/persona',
      data: {assertion: assertion},
      success: function(res, status, xhr) { window.location.reload(); },
      error: function(xhr, status, err) {
        navigator.id.logout();
        alert("Login failure: " + err);
      }
    });
	  }
  },

  onlogout: function() {
	  if(currentService == "persona"){
    // A user has logged out! Here you need to:
    // Tear down the user's session by redirecting the user or making a call to your backend.
    // Also, make sure loggedInUser will get set to null on the next page load.
    // (That's a literal JavaScript null. Not false, 0, or undefined. null.)
    $.ajax({
      type: 'POST',
      url: '/authentication/logout/persona', // This is a URL on your website.
      success: function(res, status, xhr) { window.location.reload(); },
      error: function(xhr, status, err) { alert("Logout failure: " + err); }
    });
  }
  }
});
</script>
<% } %>
</body>
</html>
