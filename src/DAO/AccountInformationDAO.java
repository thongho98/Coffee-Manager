package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import DB.MyConnection;

public class AccountInformationDAO {
	public static boolean UpdateAccountInfo(String userName, String pass, String newPass) {
		String sql = "{CALL USP_UpdateAccountInfo(" + userName + ",N'"+ pass + "',N'"+newPass+"')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.execute();
			cnn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean CheckPass(String username, String password) {
		String sql = "{CALL USP_Login(" + username + ",N'" + password + "')}";
		// String sql = "SELECT * FROM dbo.Account WHERE username ='" + username + "'
		// AND pass='" + password + "'";
		Connection cnn = MyConnection.getConnection();
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
}
