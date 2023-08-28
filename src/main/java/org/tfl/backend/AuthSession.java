package org.tfl.backend;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;


public class AuthSession
{

    private static final Logger log = Logger.getLogger(AuthSession.class.getName());
    
    /**
     * Validate if a session has been authenticated successfully and is still valid
     * Redirect to login page if session is not authenticated or invalid
     * 
     * @param req
     * @param resp
     * @return true if session is authenticated successfully, false otherwise
     * @throws IOException
     * @throws ServletException
     */
    public static String validate(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException
    {
        String userid;
        if (req == null || response == null)
        {
        	//TODO Throw Exception
        	throw new ServletException("Validate Error");
        }

      
        HttpSession sess = req.getSession(false);
        
        if(sess == null)
        {
        	//TODO Redirect index.jsp
        	response.sendRedirect("index.jsp");
        	userid = null;
        	return userid;
        }
        

        if (sess.getAttribute("userid") == null)
        { // not authenticated
        	//TODO Redirect index.jsp
        	response.sendRedirect("index.jsp");
        	userid = null;
        } else {
        	userid = (String) sess.getAttribute("userid");
        }
		return userid;        
    }
    
    
   
    
    /**
     * Check if 2fa userid attribute is set. If it is not, redirect to specified error url
     * 
     * @param req
     * @param resp
     * @param redirecturl
     * @return true if 2fa userid attribute is properly set, false otherwise
     * @throws IOException
     * @throws ServletException
     */
    public static boolean check2FASession(HttpServletRequest req, HttpServletResponse resp, String redirecturl)
            throws IOException, ServletException
    {
        if (req == null || resp == null || redirecturl == null)
        {
        	//TODO Throw Exception
        	throw new ServletException("Check 2FA Error");
        }

        HttpSession session = req.getSession(false);
        
        if(session == null)
        {
        	//TODO Redirect redirecturl
        	resp.sendRedirect(redirecturl);
        	return false;
        }
       
        String userid2fa = (String) session.getAttribute("userid2fa");
        if (userid2fa == null)
        {
        	//TODO Redirect redirecturl
        	resp.sendRedirect(redirecturl);
        	return false;
        }
        
        return true;
    }
}

