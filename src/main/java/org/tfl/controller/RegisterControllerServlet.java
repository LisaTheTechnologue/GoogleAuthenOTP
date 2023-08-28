package org.tfl.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Logger;

import org.tfl.constants.AppConstants;
import org.tfl.crypto.CryptoUtil;
import org.tfl.dao.OTPDAO;
import org.tfl.dao.RegisterDAO;
import org.tfl.model.Users;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class RegisterControllerServlet
 */
public class RegisterControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(LoginControllerServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterControllerServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");

		HttpSession session = request.getSession(false);
		if (session == null) {// no existing session
			response.sendRedirect("index.jsp");
			return;
		}
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");
		String repassword = request.getParameter("repassword");
		String remoteip = request.getRemoteAddr();
		if (firstName == null || lastName == null || userid == null || password == null || repassword == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		if (password.length() < AppConstants.MIN_LENGTH_PASS) {
			throw new RuntimeException("Invalid password");
		}

		if (password.compareTo(repassword) != 0) {// no existing session
			throw new RuntimeException("Invalid password");
		}

		if (password.compareTo(repassword) != 0) {// no existing session
			throw new RuntimeException("Invalid password");
		}

		if (!RegisterDAO.checkPasswordValidation(repassword)) {
			throw new RuntimeException("Invalid password");
		}

		if (RegisterDAO.findUser(userid, request.getRemoteAddr())) {// no existing session
			throw new RuntimeException("User existed");
		} else {
			// TODO Generate salt by CryptoUtil
			byte[] salt = CryptoUtil.generateRandomBytes(32);

			// TODO Encode Base64 salt
			byte[] saltBase64 = Base64.getEncoder().encode(salt);
			// TODO Generate hexadecimal OTP Secret by CryptoUtil
			String otpsecret = CryptoUtil.genHexaOTPSecret();
			// TODO Add new user to Database
			Users user = new Users();

			user.setUserId(userid);
			user.setLastName(lastName);
			user.setFirstName(firstName);
			user.setOtpSecret(otpsecret);
			user.setPassword(repassword);
			boolean isSuccess = RegisterDAO.addUser(user, saltBase64, remoteip);

			System.out.println("User is saved to database: " + isSuccess);
			password = null;
			repassword = null;
			// Prevent Session fixation, invalidate and assign a new session
			session.invalidate();
			session = request.getSession(true);
			session.setAttribute("userid2fa", userid);
			// Set the session id cookie with HttpOnly, secure and samesite flags
			String custsession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
			response.setHeader("Set-Cookie", custsession);
			if (!isSuccess) {
				throw new RuntimeException("Error register user to database");
			}
			// Dispatch request to confirm.jsp

			String activeKey = OTPDAO.getBase32OTPSecret(userid, remoteip);
			if (activeKey == "") {
				throw new RuntimeException("Error get active key");
			}
			request.setAttribute("activeKey", activeKey);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/confirm.jsp");
			dispatcher.forward(request, response);
		}

	}

}
