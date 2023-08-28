<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="org.tfl.dao.UserDAO"%>
<%@page import="org.tfl.backend.AuthSession"%>
<%@page import="org.tfl.backend.HtmlEscape"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="styles/main.css">
<title>Change Label Page</title>
</head>
<body>

	<div class="mainbody">
		<h2>
			<c:out value="Assign security level"></c:out>
		</h2>
		<form method="POST" autocomplete="off" accept-charset="utf-8"
			action="changeLabel">
			<ul class="form-ul">

				<li><label>User name</label><input type="text" required
					autofocus name="changeUserid"></li>

				<li><label>Level</label> <select name="changeLabel">
						<option value="4">Top Secret</option>
						<option value="3">Secret</option>
						<option value="2">Confidential</option>
						<option value="1">Unclassified</option>
				</select></li>
				<li><font color="red"><c:out value="${label_alert}"></c:out></font>
				<li><input type="submit" value="Apply"></li>

			</ul>
		</form>
		<p>
			<a href="logout.jsp">Logout</a>
		</p>
	</div>

	<%@include file="templates/footer.html"%>


</body>
</html>