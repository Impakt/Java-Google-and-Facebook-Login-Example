<%@ page import="com.impakt.social.login.servlet.SocialLoginServlet" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>

    <script src="https://accounts.google.com/gsi/client" async defer></script>

    <%
        String serverAddress = "https://" + request.getServerName();
        if ( request.getServerPort() != 443 ) {
            serverAddress += ":" + request.getServerPort();
        }
    %>

    <script>
			function checkLoginState() {
				FB.getLoginStatus(function (response) {
					var form = $('<form action="<%=serverAddress%>/social-login" method="post" style="display:none;">' +
						'<input type="text" name="accessToken" value="' +
						response.authResponse.accessToken + '" />' +
						'</form>');
					$('body').append(form);
					form.submit();
				});
			}
    </script>

    <title>Social Media Sign-in example</title>
</head>
<body>
<%-- facebook --%>
<div id="fb-root"></div>
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v13.0&appId=<%=SocialLoginServlet.FACEBOOK_APP_ID%>&autoLogAppEvents=1" nonce="3Zn22rTO"></script>
<%-- end facebook --%>

<br>

<%-- google --%>
<div id="g_id_onload"
     data-client_id="<%=SocialLoginServlet.GOOGLE_CLIENT_ID%>"
     data-login_uri="<%=serverAddress%>/social-login"
     data-auto_prompt="false">
</div>
<div class="g_id_signin"
     data-type="standard"
     data-size="large"
     data-theme="outline"
     data-text="sign_in_with"
     data-shape="rectangular"
     data-logo_alignment="left">
</div>
<%-- end google --%>

<%-- facebook --%>
<%-- continue as --%>
<div class="fb-login-button" data-width="" data-size="large" data-button-type="login_with" data-layout="default" data-auto-logout-link="false" data-use-continue-as="false" data-scope="public_profile,email"
     data-onlogin="checkLoginState();"></div>
</body>
</html>