<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/gcw.tld" prefix="gcw" %>
<gcw:check expectAttr="googleLoginUrl" fromContextUrl="/Login"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Log In</title>
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js">
  </script>
  <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

<div class="container">
	<div id="content">
	
		<div class="subheader">
		    <h1>Calendar Test</h1>
		</div>
		
		<div id="maincol">
			<div id="page-description">
			
				<p>${requestScope.loginStatus}</p>
			
				<div id="signinButtonPanel">
					<a href="${requestScope.googleLoginUrl}" id="signinButton">Log in with Google</a>
				</div>
			
			</div>
		</div>
	</div>
</div>

</body>
</html>