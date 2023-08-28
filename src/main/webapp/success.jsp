<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.tfl.dao.UserDAO"%>
<%@page import="org.tfl.backend.AuthSession"%>
<%@page import="org.tfl.backend.HtmlEscape"%>
<%@page import="java.util.Objects"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
response.setHeader("Cache-Control", "no-store");
String userid = AuthSession.validate(request, response);
if (Objects.isNull(userid)) {
	response.sendRedirect("index.jsp");
	return;
}
boolean isAdmin = userid.equals("admin") ? true : false;
//Prevent CSRF by requring OTP validation each time page is displayed. 
String anticsrf = (String) session.getAttribute("anticsrf_success");
if (anticsrf == null) {//token not present redirect back to OTP page for validation again
	session.removeAttribute("userid");
	session.setAttribute("userid2fa", userid);
	userid = null;
	RequestDispatcher rd = request.getRequestDispatcher("otp.jsp");
	rd.forward(request, response);
	return;
} else {//token present
		//remvoe the token so that subsequent request will require OTP validation
	session.removeAttribute("anticsrf_success");
}

String username = UserDAO.getUserName(userid, request.getRemoteAddr());
username = HtmlEscape.escapeHTML(username);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="styles/main.css">
<title>Success Page</title>
</head>
<body>

	<div class="mainbody">

		<p>
			Welcome
			<%
		if (username != null) {
			out.print(username);
		} else {
			out.print("Unknown");
		}
		%>
			<br>
		</p>
		<p>
			<a href="messageRead">View notices</a>
		</p>
		<p>
			<a href="messageWrite">Write notices</a>
		</p>
		<p>
			<a href="changeLabel"> <%
 if (isAdmin) {
 	out.print("Change label");
 } else {
 	out.print("");
 }
 %>
			</a>
		</p>
		<p>
			<a href="logout.jsp">Logout</a>
		</p>


		<%@include file="templates/footer.html"%>


	</div>


</body>
</html>
