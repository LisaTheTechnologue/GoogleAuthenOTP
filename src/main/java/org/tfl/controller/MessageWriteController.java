package org.tfl.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import org.tfl.backend.AuthSession;
import org.tfl.constants.Label;
import org.tfl.dao.MessageDAO;
import org.tfl.dao.UserDAO;
import org.tfl.model.Message;

/**
 * Servlet implementation class MessageWriteController
 */
public class MessageWriteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageWriteController() {
		super();
		// TODO Auto-generated constructor stub
	}	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session == null) {// no existing session
			response.sendRedirect("index.jsp");
			return;
		}
		String userid = (String) session.getAttribute("userid");
		if (userid == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		String userLabel = "";
		try {
			userLabel = Label.fromValue(UserDAO.getLabel(userid, request.getRemoteAddr())).getName();
		} catch (Exception e) {
			throw new RuntimeException("Cannot get label");
		}
        request.setAttribute("userLabel", userLabel); 
        request.getRequestDispatcher("messageWrite.jsp").forward(request,response);
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		String userid = AuthSession.validate(request, response);
		if (userid.equals(null)) {
			response.sendRedirect("index.jsp");
			return;
		}
		int userLabel = 0;
		try {
			userLabel =  Label.fromName((String)session.getAttribute("userLabel")).getValue();
		} catch (Exception e) {
			throw new RuntimeException("Cannot get label");
		}
		String remoteip = request.getRemoteAddr();

		int messageLabel = Integer.parseInt(request.getParameter("messageLabel"));
		if(MessageDAO.checkLevel(messageLabel,userLabel)) {
			Message message = new Message();
			message.setLabel(messageLabel);
			message.setContent(request.getParameter("messageContent"));
			message.setUserId(userid);
			message.setDate(java.time.LocalDateTime.now());
			boolean isSuccess = false;
			try {
				isSuccess = MessageDAO.writeMessage(userid, userLabel, remoteip, message);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(isSuccess) {
				request.setAttribute("response","Successful!");
				return;
			} else {
				throw new RuntimeException("Cannot insert message");
			}
		} else {
			request.setAttribute("response","Error! Message's level is not lower your level");
			return;
		}
	}
}
