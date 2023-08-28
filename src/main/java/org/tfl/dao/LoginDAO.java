
package org.tfl.dao;

import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Logger;

import org.tfl.backend.DBConnect;
import org.tfl.constants.AppConstants;
import org.tfl.crypto.CryptoUtil;
import org.tfl.model.Users;

import jakarta.servlet.ServletException;

public class LoginDAO {

	private static final Logger log = Logger.getLogger(LoginDAO.class.getName());

	/**
	 * Validates user credential
	 *
	 * @param userid
	 * @param password
	 * @param remoteip client ip address
	 * @return true if user is valid, false otherwise
	 */
	public static boolean validateUser(String userid, String password, String remoteip) throws SQLException {

		boolean isValid = false;
		/*
		 * TODO Pseudo code result = SqlQuery(userid); if result != 0 then if account
		 * not lock then if hash(password+salt) is valid then isValid = true else begin
		 * failLogin = failLogin + 1 if failLogin > MAX Lock account
		 *
		 */
		if (userid == null || password == null || remoteip == null) {
			isValid = false;
			return isValid;
		}
		String pass, salt;
		Users user = UserDAO.getUserByUserId(userid);
		if (user!=null) {
			pass = user.getPassword();
			salt = user.getSalt();
		} else {
			isValid = false;
			return isValid;
		}

		// hash		
		byte[] saltByte = Base64.getDecoder().decode(salt);
		byte[] passByte = CryptoUtil.getPasswordKey(password.toCharArray(), saltByte, AppConstants.PASSWORD_ITERATION);
		password = CryptoUtil.byteArrayToHexString(passByte);

		if (pass.equals(password)) {
			isValid = true;
		} else {
			isValid = false;
		}
		log.info("Done validate user");
		return isValid;
	}

	/**
	 * Check if a user account is locked
	 *
	 * @param userid
	 * @param remoteip client ip address
	 * @return true if account is locked or false otherwise
	 * @throws ServletException
	 */
	public static boolean isAccountLocked(String userid, String remoteip) throws ServletException, SQLException {
		boolean isLocked = false;
		/*
		 * TODO Pseudo code result = SqlQuery(userid); if result != 0 then if account is
		 * lock then isLocked = true
		 *
		 */
		Users user = UserDAO.getUserByUserId(userid);
		if (user!=null) {
			isLocked = user.getIsLocked()==1?true:false;
		}
		log.info("User is locked: " + isLocked);
		return isLocked;
	}

	/**
	 * Increments the failed login count for a user Locked the user account if fail
	 * logins exceed threshold.
	 *
	 * @param userid
	 * @param remoteip
	 * @throws ServletException
	 */
	public static void incrementFailLogin(String userid, String remoteip) throws ServletException {
		if (userid == null || remoteip == null) {
			// TODO throw exception;
			throw new ServletException("No userid or remoteip found");
		}

		// TODO Pseudo code
		/*
		 * failLogin = failLogin + 1 if failLogin > MAX Lock account update database
		 */

		int failLogin = 0;
		Users user = UserDAO.getUserByUserId(userid);
		if (user!=null) {
			failLogin = user.getFailLogin();
		}
		failLogin += 1;
		if (failLogin > AppConstants.MAX_FAIL_LOGIN) {
			UserDAO.updateUserIsLocked(userid);
		} else {
			UserDAO.updateUserFailLogin(userid, failLogin);
		}
	}
}
