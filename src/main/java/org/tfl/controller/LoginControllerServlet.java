package org.tfl.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Logger;

import org.tfl.backend.AuthSession;
import org.tfl.dao.LoginDAO;
/**
 * Servlet implementation class LoginControllerServlet
 */
public class LoginControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(LoginControllerServlet.class.getName());

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);
        String userid = request.getParameter("userid");
        String password = request.getParameter("password");

        if (userid == null || password == null)
        {        	
        	//TODO Redirect to index.jsp 
        	response.sendRedirect("index.jsp");
			return;
        }
        
        try {
			if(LoginDAO.isAccountLocked(userid, request.getRemoteAddr()))
			{
			    log.warning("Error: Account is locked " + userid  + " " + request.getRemoteAddr());
			    response.sendRedirect("index.jsp");
			    
			}

			else if (LoginDAO.validateUser(userid, password, request.getRemoteAddr()))
			{
			    password = null;
			    //Prevent Session fixation, invalidate and assign a new session
			    
			    session.invalidate();
			    session = request.getSession(true);
			    session.setAttribute("userid2fa", userid);
			    //Set the session id cookie with HttpOnly, secure and samesite flags
			    String custsession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
			    response.setHeader("Set-Cookie", custsession);
			    
			    //Dispatch request to otp.jsp
			    response.sendRedirect("otp.jsp");
				return;
			}
			else
			{	
				log.warning("Error: Username or password is invalid " + userid  + " " + request.getRemoteAddr());
				session.setAttribute("loginerror", "error");
				String remoteip = request.getRemoteAddr();
				LoginDAO.incrementFailLogin(userid, remoteip);
				response.sendRedirect("index.jsp");
			}
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
