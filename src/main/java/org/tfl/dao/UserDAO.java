package org.tfl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.tfl.backend.DBConnect;
import org.tfl.constants.UserConstants;
import org.tfl.model.Users;

public class UserDAO {
	private static final Logger log = Logger.getLogger(UserDAO.class.getName());

	/**
	 * Retrieves the username for a userid
	 * 
	 * @param userid
	 * @param remoteip
	 * @return username if successful, null if there is an error
	 */
	public static String getUserName(String userid, String remoteip) {
		String fullName = null;
		Users user = getUserByUserId(userid);
		if (user!=null) {
			fullName = user.getLastName()
					+ " " + user.getFirstName();
		}
		return fullName;
	}

	public static int getLabel(String userid, String remoteAddr) {
		int label = -1;
		Users user = getUserByUserId(userid);
		if (user!=null) {
			label = user.getLabel();
		}
		return label;
	}

	public static boolean changeLabel(String changeUserid, int parseInt, String remoteAddr) {
		int row = updateLabel(changeUserid, parseInt);
		if(row!=-1) {
			return true;
		}
		return false;
	}
	
	public static Users getUserByUserId(String userid) {
		String query = "select * from " + UserConstants.TABLE_USERS + " where " + UserConstants.TABLE_USERS_USERID
				+ "=?";
		Users user = new Users();
		DBConnect db = new DBConnect();
		try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {		
				user.setUserId(userid);
				user.setLastName(rs.getString(UserConstants.TABLE_USERS_LASTNAME) );
				user.setFirstName(rs.getString(UserConstants.TABLE_USERS_FIRSTNAME));
				user.setIsLocked(rs.getInt(UserConstants.TABLE_USERS_ISLOCKED));
				user.setFailLogin(rs.getInt(UserConstants.TABLE_USERS_FAILLOGIN));
				user.setLabel(rs.getInt(UserConstants.TABLE_USERS_LABEL));
				user.setOtpSecret(rs.getString(UserConstants.TABLE_USERS_OTPSECRET));
				user.setSalt(rs.getString(UserConstants.TABLE_USERS_SALT));
				user.setPassword(rs.getString(UserConstants.TABLE_USERS_PASSWORD));
				return user;
			}

		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}

	public static void updateUserFailLogin(String userid, int count) {
		String query = "UPDATE " + UserConstants.TABLE_USERS + " SET " + UserConstants.TABLE_USERS_FAILLOGIN + " = "
				+ count + " where " + UserConstants.TABLE_USERS_USERID + " =?";
		DBConnect db = new DBConnect();
		try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
			stmt.setString(1, userid);
			stmt.executeUpdate();
			stmt.close();
			System.out.println("Done update user's fail login");
		} catch (Exception error) {
			System.out.println(error);
		}
	}

	public static void updateUserIsLocked(String userid) {
		String query = "UPDATE " + UserConstants.TABLE_USERS + " SET " + UserConstants.TABLE_USERS_ISLOCKED + " = 1"
				+ " where " + UserConstants.TABLE_USERS_USERID + " =?";
		DBConnect db = new DBConnect();
		try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
			stmt.setString(1, userid);
			stmt.executeUpdate();
			stmt.close();
			System.out.println("Done update user's isLocked");
		} catch (Exception error) {
			System.out.println(error);
		}
	}

	public static int updateLabel(String userid, int label) {
		String query = "UPDATE " + UserConstants.TABLE_USERS + " SET " + UserConstants.TABLE_USERS_LABEL + " = "
				+ label + " where " + UserConstants.TABLE_USERS_USERID + " =?";
		DBConnect db = new DBConnect();
		try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
//			con.setAutoCommit(false);
			stmt.setString(1, userid);
			int row = stmt.executeUpdate();
//			con.commit();
			stmt.close();
//			con.close();
			System.out.println("Done update user's label");
			return row;
		} catch (Exception error) {
			System.out.println("Update label: " + error);
		}
		return -1;
	}
}
