package org.tfl.constants;

public class UserConstants {
	public static final String TABLE_USERS = "tbl_users";
	public static final String TABLE_USERS_USERID = "userid";
	public static final String TABLE_USERS_LASTNAME = "lastname";
	public static final String TABLE_USERS_FIRSTNAME = "firstname";
	public static final String TABLE_USERS_ISLOCKED = "isLocked";
	public static final String TABLE_USERS_OTPSECRET = "otpsecret";
	public static final String TABLE_USERS_LABEL = "label";
	public static final String TABLE_USERS_FAILLOGIN = "faillogin";
	public static final String TABLE_USERS_PASSWORD = "password";
	public static final String TABLE_USERS_SALT = "salt";
	public static final String TABLE_USERS_ALL_COLUMNS = TABLE_USERS_USERID+","+TABLE_USERS_LASTNAME+","+
			TABLE_USERS_FIRSTNAME+","+TABLE_USERS_OTPSECRET+","+TABLE_USERS_SALT+","+TABLE_USERS_PASSWORD;
}
