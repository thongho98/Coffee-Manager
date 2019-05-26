package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import BEAN.Account;
import BEAN.TypeRight;
import DB.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminAccountDAO {
	public static List<Account> LoadListAccount() {
		String sql = "{CALL USP_GetAccount}";
		Connection cnn = MyConnection.getConnection();
		List<Account> list = FXCollections.observableArrayList();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int idAccount = rs.getInt("id");
				String displayName = rs.getString("displayName");
				String username = rs.getString("username");
				String password = rs.getString("pass");
				String typeRight = rs.getString("typeName");

				Account account = new Account();
				account.setId(idAccount);
				account.setDisplayname(displayName);
				account.setUsername(username);
				account.setPassword(password);
				account.setTyperight(typeRight);
				list.add(account);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}
	
	public static ObservableList<TypeRight> LoadTypeRightList() {
		String sql = "{CALL USP_GetListTypeRight}";
		Connection cnn = MyConnection.getConnection();
		ObservableList<TypeRight> list = FXCollections.observableArrayList();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String typeName = rs.getString("typeName");

				TypeRight typeRight = new TypeRight();
				typeRight.setId(id);
				typeRight.setTypeName(typeName);

				list.add(typeRight);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}
	public static int GetIDTypeRight(String nameType) {
		int id = 0;
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT id FROM dbo.TypeRight WHERE typeName = N'" + nameType + "'";
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			return -1;
		}
		return id;
	}
	public static void InserAccount(String displayName, String username, String pass, int typeRight) {
		String sql = "{CALL USP_InsertAccount(N'" + displayName + "',N'"+username+"',N'"+pass+"',"+typeRight+")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void DeleteAccount(int id) {
		String sql = "{CALL USP_DeleteAccount(N'" + id + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void UpdateAccount(int id,String displayName, String username, int typeRight) {
		String sql = "{CALL USP_UpdateAccount("+id+", N'" + displayName + "',N'"+username+"',"+typeRight+")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void ResetPassword(int id) {
		String sql = "{CALL USP_ResetPasswordAccount("+id+")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static boolean CheckSameUsernameAndId(int id, String username) {
		String sql = "{CALL USP_CheckSameUsernameAndId("+id+",N'" + username + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			int count = 0;
			while(rs.next()) {
				count = rs.getInt(1);
			}
			if(count>0) return true;
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return false;
	}
	public static boolean CheckSameUsername(String username) {
		String sql = "{CALL USP_CheckSameUsername(N'" + username + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			int count = 0;
			while(rs.next()) {
				count = rs.getInt(1);
			}
			if(count>0) return true;
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return false;
	}
}
