package org.tfl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.tfl.backend.DBConnect;
import org.tfl.constants.AppConstants;
import org.tfl.constants.Label;
import org.tfl.constants.MessageConstants;
import org.tfl.constants.UserConstants;
import org.tfl.model.Message;

import jakarta.servlet.ServletException;

public class MessageDAO {
	
	public static boolean checkLevel(int messageLabel, int userLabel) {
		return messageLabel>=userLabel;
	}
	
	public static List<Message> readMessage(String userid, int userLabel, String remoteAddr) {
		List<Message> list = new LinkedList<Message>();
		String query = "select * from " + MessageConstants.TABLE_MESSAGE + " where "
				+ MessageConstants.TABLE_MESSAGE_LABEL + " between ? and ?";

		DBConnect dbConnect = new DBConnect();
		try (Connection connection = dbConnect.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query);) {
			// chi doc xuong
			stmt.setLong(1, 0);
			stmt.setLong(2, userLabel);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(MessageConstants.TABLE_MESSAGE_ID);
				String author = rs.getString(MessageConstants.TABLE_MESSAGE_USERID);
				String content = rs.getString(MessageConstants.TABLE_MESSAGE_CONTENT);
				LocalDateTime date = rs.getObject(MessageConstants.TABLE_MESSAGE_DATE,LocalDateTime.class);
				int label = rs.getInt(MessageConstants.TABLE_MESSAGE_LABEL);
				String labelStr = Label.fromValue(label).getName();
				Message message = new Message(id,author, content, date, labelStr);
				list.add(message);
			}

			return list;

		} catch (Exception error) {
			System.out.println(error);
		}
		return null;
	}

	public static boolean writeMessage(String userid, int userLabel, String remoteip, Message message)
			throws ServletException {
		boolean isSuccess = false;
		String query = "insert into " + MessageConstants.TABLE_MESSAGE + " (" + MessageConstants.TABLE_MESSAGE_USERID
				+ "," + MessageConstants.TABLE_MESSAGE_CONTENT + "," + MessageConstants.TABLE_MESSAGE_DATE + ","
				+ MessageConstants.TABLE_MESSAGE_LABEL + ")" + " values(?,?,?,?);";
		DBConnect dbConnect = new DBConnect();

		try (Connection connection = dbConnect.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query);) {
			stmt.setString(1, userid);
			stmt.setString(2, message.getContent());
			stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			stmt.setInt(4, message.getLabel());
			
			int row = stmt.executeUpdate();
			if (row > 0) {
				isSuccess = true;
			}
			connection.close();
		} catch (Exception error) {
			System.out.println(error);
		}
		System.out.println("Done inserted message");
		return isSuccess;
	}

}
