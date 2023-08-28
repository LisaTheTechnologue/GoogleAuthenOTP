package org.tfl.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import org.tfl.backend.AuthSession;
import org.tfl.constants.Label;
import org.tfl.dao.MessageDAO;
import org.tfl.dao.UserDAO;
import org.tfl.model.Message;

/**
 * Servlet implementation class MessageReadController
 */
public class MessageReadController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageReadController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		String userid = AuthSession.validate(request, response);
		if (userid == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		int userLabel = 0;
		try {
			userLabel = Label.fromName((String) session.getAttribute("userLabel")).getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String remoteip = request.getRemoteAddr();

		// TODO Auto-generated method stub
		List<Message> messages = MessageDAO.readMessage(userid, userLabel, remoteip);
		request.setAttribute("messages", messages);
		request.getRequestDispatcher("messageRead.jsp").forward(request, response);
	}

}
