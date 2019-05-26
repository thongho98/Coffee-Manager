package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import DB.MyConnection;

public class AdminTableDAO {
	public static boolean CheckSameNameTable(String nameTable) {
		String sql = "{CALL USP_CheckSameNameTable(N'" + nameTable + "')}";
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
	
	public static void InserTable(String nameTable) {
		String sql = "{CALL USP_InsertTable(N'" + nameTable + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void DeleteTableFood(int idTable) {
		String sql = "{CALL USP_DeleteTableFood(" + idTable + ")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void UpdateNameTable(int idTableFood, String nameTable) {
		String sql = "{CALL USP_UpdateTableFood("+idTableFood+", N'" + nameTable + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
}
