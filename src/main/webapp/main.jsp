<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/gcw.tld" prefix="gcw" %>
<gcw:authenticate/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Calendar Test</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<div class="topbar">
	<div class="topbar-center-panel">
		<div class="topbar-links">
			<a href="Logout">Log Out</a>
		</div>
	</div>
</div>

<div class="container">
	<div id="content">
	
		<div class="subheader">
		    <h1>Calendar Test</h1>
		</div>
		
		<div id="maincol">
			<p>${requestScope.status}</p>
			
			<c:forEach items="${events}" var="event">
				<div class="eventEntry">
					${event.summary}<br/>
					${event.timeDescription}
				</div>
			</c:forEach>
		</div>
		
		<div id="sidecol">
			<div id="button">
				<a href="#" onclick="javascript:openEmail()">Send as email...</a>
			</div>
		</div>
	</div>
</div>

</body>
<script type="text/javascript">
function openEmail() {
	if('${emailWarning}')
		alert('${emailWarning}');
	window.open('${emailUrl}' ,'gmailForm','scrollbars=yes,width=680,height=510,top=175,left=75,status=no,resizable=yes');
}
</script>
</html>