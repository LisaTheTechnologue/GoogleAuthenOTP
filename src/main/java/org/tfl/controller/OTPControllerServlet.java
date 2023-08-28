package org.tfl.controller;

import java.io.IOException;
import java.util.logging.Logger;

import org.tfl.constants.Label;
import org.tfl.crypto.CryptoUtil;
import org.tfl.crypto.TimeBaseOTP;
import org.tfl.dao.LoginDAO;
import org.tfl.dao.OTPDAO;
import org.tfl.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class OTPControllerServlet
 */
public class OTPControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(OTPControllerServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OTPControllerServlet() {
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

		// Make sure it has a valid 2fa session from login page
		// userid2fa session attribute must be set

		// TODO check2FASession

		HttpSession session = request.getSession(false);
		String userid = (String) session.getAttribute("userid2fa");
		// Remove the userid2fa attribute to prevent multiple submission attempts
		session.removeAttribute("userid2fa");

		String otpvalue = (String) request.getParameter("totp");

		if (otpvalue == null) {
			// TODO Invalidate session
			session.invalidate();
			throw new RuntimeException("Invalid OTP");
		}

		String otpsecret = null;
		// TODO Get otpsecret from Database using OTPDAO class
		try {
			try {
				otpsecret = OTPDAO.getOTPSecret(userid, request.getRemoteAddr());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// GenerateOTP using TimeBaseOTP class
		String otpresult = TimeBaseOTP.generateOTP(CryptoUtil.hexStringToByteArray(otpsecret));

		otpsecret = null;

		if (otpresult == null) {
			// TODO Invalidate session
			session.invalidate();
			throw new RuntimeException("Cannot get otp");
		}
		
		String remoteip = request.getRemoteAddr();
		if (otpresult.equals(otpvalue)) {// Correct OTP value
			String userLabel = "";
			try {
				userLabel = Label.fromValue(UserDAO.getLabel(userid, request.getRemoteAddr())).getName();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot get user label");
			}
			session.invalidate();
			session = request.getSession(true);
			session.setAttribute("userid", userid);
			session.setAttribute("userLabel", userLabel);
			session.setAttribute("anticsrf_success", "AntiCSRF");

//            String custsession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
//            response.setHeader("Set-Cookie", custsession);

			// TODO reset fail login attempt
			
			try {
				OTPDAO.resetFailLogin(userid, remoteip);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// TODO Redirect /success.jsp
			request.getRequestDispatcher("success.jsp").forward(request, response);
			return;
		} else {// Incorrect OTP value
			
			log.warning("Error: Invalid otp value " + remoteip + " " + userid);

			// TODO Update fail login count.
			try {
				LoginDAO.incrementFailLogin(userid, remoteip);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// If account is locked reset session and redirect user
			// TODO pseudo code
			/*
			 * if account is locked then redirect /locked.html else
			 * session.setAttribute("userid2fa", userid); session.setAttribute("otperror",
			 * ""); send back to the otp input page again
			 * 
			 */
			try {
				boolean isLocked = LoginDAO.isAccountLocked(userid, remoteip);
				if (isLocked) {
					response.sendRedirect("locked.html");
					return;
				} else {
					session.setAttribute("userid2fa", userid);
					session.setAttribute("otperror", "");
					response.sendRedirect("otp.jsp");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

	}

}
