
package org.tfl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.tfl.backend.DBConnect;
import org.tfl.constants.UserConstants;
import org.tfl.crypto.CryptoUtil;
import org.tfl.model.Users;

import jakarta.servlet.ServletException;

public class OTPDAO {

	private static final Logger log = Logger.getLogger(OTPDAO.class.getName());

	/**
	 * Retrieves the otp secret hexadecimal string from the userid
	 * 
	 * @param userid
	 * @param remoteip
	 * @return hexadecimal secret string
	 * @throws ServletException
	 */
	public static String getOTPSecret(String userid, String remoteip) throws ServletException {
		String otpSecret = null;
		if (userid == null || remoteip == null) {
			// TODO throw exception;
			throw new IllegalStateException("Null userid or remoteip");
		}

		Users user = UserDAO.getUserByUserId(userid);
		if (user!=null) {
			otpSecret = user.getOtpSecret();
			log.info(otpSecret);
		}
		return otpSecret;
	}

	/**
	 * Retrieves the otp secret hexadecimal string from the userid
	 * 
	 * @param userid
	 * @param remoteip
	 * @return hexadecimal secret string
	 * @throws ServletException
	 */
	public static String getBase32OTPSecret(String userid, String remoteip) throws ServletException {
		String hexaString = getOTPSecret(userid, remoteip);
		if(hexaString==null) {
			return "";
		}
		byte[] hexaCode = CryptoUtil.hexStringToByteArray(hexaString);
		return CryptoUtil.base32Encode(hexaCode);
	}

	/**
	 * Check if a user account is locked
	 * 
	 * @param userid
	 * @param remoteip
	 * @return true if account is locked, false otherwise
	 * @throws ServletException
	 */
	public static boolean isAccountLocked(String userid, String remoteip) throws ServletException {
		boolean isLock = true;
		if (userid == null || remoteip == null) {
			// TODO throw exception;
			throw new IllegalStateException("Null userid or remoteip");
		}

		Users user = UserDAO.getUserByUserId(userid);
		if (user!=null) {
			isLock = user.getIsLocked()==1?true:false;
		}
		return isLock;
	}

	/**
	 * Reset the failed login counts of a user to zero If an account is locked an
	 * exception will be thrown
	 * 
	 * @param userid
	 * @param remoteip
	 * @throws ServletException
	 */
	public static void resetFailLogin(String userid, String remoteip) throws ServletException {
		if (userid == null || remoteip == null) {
			// TODO throw exception;
			throw new IllegalStateException("Null userid or remoteip");
		}

		// TODO Update failLogin in database to zero
		UserDAO.updateUserFailLogin(userid,0);
	}

}
