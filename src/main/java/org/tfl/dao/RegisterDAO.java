package org.tfl.dao;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.function.IntPredicate;

import org.tfl.backend.DBConnect;
import org.tfl.constants.AppConstants;
import org.tfl.constants.UserConstants;
import org.tfl.crypto.CryptoUtil;
import org.tfl.model.Users;

public class RegisterDAO {

	/**
	 * Check user existence
	 * 
	 * @param userid
	 * @param remoteip client ip address
	 * @return true if user is existent, false otherwise
	 */
	public static boolean findUser(String userid, String remoteip) {
		boolean isFound = false;
		// TODO Pseudo code
		/*
		 * result = SqlQuery(userid); if result != 0 then isFound = true
		 */
		try {
			Users user = UserDAO.getUserByUserId(userid);
			if (user!=null) {
				isFound = true;
			}

		} catch (Exception error) {
			System.out.println(error);
		}
		System.out.println("Find user: " + isFound);
		return isFound;
	}

	public static boolean addUser(Users user, byte[] salt, String remoteip) {
		boolean isSuccess = false;

		// TODO Hash password with salt
		String password = CryptoUtil.byteArrayToHexString(
				CryptoUtil.getPasswordKey(user.getPassword().toCharArray(), salt, AppConstants.PASSWORD_ITERATION));
		user.setPassword(password);

		// TODO Insert new record to database. If operation is success set isSuccess to
		// true
		String query = "insert into " + UserConstants.TABLE_USERS + "( " + UserConstants.TABLE_USERS_ALL_COLUMNS + ")"
				+ " values(?,?,?,?,?,?)";
		DBConnect dbConnect = new DBConnect();
		long id = 0;

		try (Connection connection = dbConnect.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, user.getUserId());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getOtpSecret());
			String saltStr = Base64.getEncoder().encodeToString(salt);
			stmt.setString(5, saltStr);
			stmt.setString(6, user.getPassword());

			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				isSuccess = true;
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Done inserted user: " + (isSuccess?"Success":"Failed"));
		return isSuccess;
	}

	public static boolean checkPasswordValidation (String password) {
		return containsLowerCase(password) && 
		           containsUpperCase(password);	
	}
	private static boolean containsLowerCase(String value) {
	    return contains(value, i -> Character.isLetter(i) && Character.isLowerCase(i));
	}

	private static boolean containsUpperCase(String value) {
	    return contains(value, i -> Character.isLetter(i) && Character.isUpperCase(i));
	}
	
	private static boolean contains(String value, IntPredicate predicate) {
	    return value.chars().anyMatch(predicate);
	}
}
