<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
response.setHeader("Cache-Control", "no-store");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="styles/main.css">
<title>Read Notices</title>
</head>
<body>



	<div class="mainbody">

		<h3>Notices</h3>

		<table>
			<tr>
				<th>ID#</th>
				<th>Content</th>
				<th>Author</th>
				<th>Date</th>
				<th>Label</th>
			</tr>
			<!-- 			https://stackoverflow.com/questions/50506321/java-servlet-mvc-show-database-query-result-to-jsp-table -->
			<c:choose>
				<c:when test="${!empty messages}">
					<c:forEach items="${messages}" var="message" varStatus="status">
						<tr>
							<td>${message.getId()}</td>
							<td>${message.getContent()}</td>
							<td>${message.getUserId()}</td>
							<td>${message.getDate()}</td>
							<td>${message.getLabelStr()}</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
        No records available.
        <br />
				</c:otherwise>
			</c:choose>
		</table>

	</div>

	<%@include file="templates/footer.html"%>
</body>
</html>