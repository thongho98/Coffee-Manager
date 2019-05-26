package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import BEAN.Account;
import DB.MyConnection;

public class LoginDAO {
	// Kiểm tra login
	public static boolean Login(String username, String password, Connection cnn) {
		String sql = "{CALL USP_Login(" + username + ",N'" + password + "')}";
		// String sql = "SELECT * FROM dbo.Account WHERE username ='" + username + "'
		// AND pass='" + password + "'";
		cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				String usernameCheck = rs.getString("username");
				String passwordCheck = rs.getString("pass");
				if (usernameCheck.equalsIgnoreCase(username) && passwordCheck.equalsIgnoreCase(password)) {
					return true;
				}
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return false;
	}

	// lay account
	public static Account acc = new Account();
	public static void GetAccountByUsername(String username,Connection cnn) {
		String sql = "{CALL USP_GetAccountByUsername(" + username + ")}";
		cnn = MyConnection.getConnection();
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LoginDAO.acc.setId(rs.getInt(1));
				LoginDAO.acc.setDisplayname(rs.getString(2));
				LoginDAO.acc.setUsername(rs.getString(3));
				LoginDAO.acc.setPassword(rs.getString(4));
				LoginDAO.acc.setTyperight(rs.getString(5));
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
}
