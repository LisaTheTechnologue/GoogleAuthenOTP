<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.tfl.backend.AuthSession"%>
<%@page isErrorPage="true"%>

<%
response.setHeader("Cache-Control", "no-store");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>Error Page</title>
</head>
<body>
	<div class="mainbody">

		<h3>An error has occurred</h3>
		<p>Opps ... Please check back later. Contact the administrator if
			the problem persists.</p>
		<div id="error" class="settingmsg">
			Exception:
			<%=exception%>
		</div>
	</div>
	<%@include file="templates/footer.html"%>


</body>
</html>
