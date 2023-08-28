package org.tfl.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Objects;

import org.tfl.backend.AuthSession;
import org.tfl.dao.UserDAO;

/**
 * Servlet implementation class ChangeLabelController
 */
public class ChangeLabelController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeLabelController() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Cache-Control", "no-store");
		String userid = AuthSession.validate(request, response);
		if (Objects.isNull(userid)) {
			response.sendRedirect("index.jsp");
			return;
		}
		if (!userid.equals("admin")) {
			response.sendRedirect("logout.jsp");
			return;
		}

		request.setAttribute("userid", userid); // Will be available as ${products} in JSP
		request.getRequestDispatcher("accLabel.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Cache-Control", "no-store");
		HttpSession session = request.getSession(false);

		String userid = AuthSession.validate(request, response);
		if (Objects.isNull(userid)) {
			response.sendRedirect("index.jsp");
			return;
		}
		String changeUserid = request.getParameter("changeUserid");
		String changeLabel = request.getParameter("changeLabel");

		if (!userid.equals("admin")) {
			System.out.println("not admin, userid " + userid);
			session.invalidate();
			response.sendRedirect("logout.jsp");
			return;
		} else if (changeUserid == null || changeLabel == null) {
			System.out.println("null: " + changeUserid + " " + changeLabel);
			session.invalidate();
			response.sendRedirect("index.jsp");
			return;
		}

		boolean isChange = false;
		try {
			isChange = UserDAO.changeLabel(changeUserid, Integer.parseInt(changeLabel), request.getRemoteAddr());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (isChange) {
			session = request.getSession(true);
			session.setAttribute("changeUserid", changeUserid);
			session.setAttribute("changeLabel", changeLabel);
			session.setAttribute("label_alert", "Successful");
			response.sendRedirect("accLabel.jsp");
		} else {
			session.invalidate();
			session = request.getSession(true);
			// show data back to user
			session.setAttribute("changeUserid", changeUserid);
			session.setAttribute("changeLabel", changeLabel);
			session.setAttribute("label_alert", "Unavailable user");
			response.sendRedirect("accLabel.jsp");
		}
	}

}
