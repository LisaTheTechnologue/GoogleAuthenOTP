<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="styles/main.css">
<title>Write Message</title>
</head>
<body>



	<div class="webformdiv">

		<div class="formheader">
			<h3>Post message</h3>
		</div>


		<form method="POST" autocomplete="off" accept-charset="utf-8"
			action="messageWrite">
			<ul class="form-ul">

				<li><label>Message</label> <textarea required cols="30"
						rows="10" autofocus name="messageContent"></textarea></li>
				
				<li>Your level: ${userLabel}
				</li>

				<li><label>Message level</label> <select name="messageLabel" required>
						<option value="4">Top Secret</option>
						<option value="3">Secret</option>
						<option value="2">Confidential</option>
						<option value="1">Unclassified</option>
				</select></li>

				<li>
					<%
					String s2 = (String) request.getAttribute("response");
					if (s2 != null && !s2.equals("")) {
						out.print(s2);
					} else {
					}
					%>
				</li>

				<li><input type="submit" value="Submit"></li>
			</ul>
		</form>

	</div>

	<%@include file="templates/footer.html"%>

</body>
</html>