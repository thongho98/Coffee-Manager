package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import BEAN.OrderTable;
import BEAN.TableFood;
import DB.MyConnection;

public class HomeOrderTableDAO {
	public static List<TableFood> LoadTableList() {
		String sql = "{CALL USP_GetTableList}";
		Connection cnn = MyConnection.getConnection();
		List<TableFood> list = new ArrayList<TableFood>();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String nameTable = rs.getString("nameTable");
				String tableStatus = rs.getString("tableStatus");

				TableFood table = new TableFood();
				table.setId(id);
				table.setName(nameTable);
				table.setIdStatus(tableStatus);

				list.add(table);
			}
			rs.close();
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}

	public static List<OrderTable> GetListCustomer(int idTable) {
		String sql = "{CALL USP_GetListOrderTable("+idTable+")}";
		Connection cnn = MyConnection.getConnection();
		List<OrderTable> list = new ArrayList<OrderTable>();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String nameCustomer = rs.getString("nameCustomer");
				String phone = rs.getString("phone");
				String checkin = rs.getString("checkin");
				String timeOrder = rs.getString("timeOrder");
				int idTableFood = rs.getInt("idTable");
				
				OrderTable orderTable = new OrderTable();
				orderTable.setId(id);
				orderTable.setName(nameCustomer);
				orderTable.setPhone(phone);
				orderTable.setDateCheckin(checkin);
				orderTable.setTimeOrder(timeOrder);
				orderTable.setIdTable(idTableFood);
				
				list.add(orderTable);
			}
			rs.close();
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}
	
	public static void InserOrderTable(int idTable,String nameCustomer,String phone, String checkin) {
		String sql = "{CALL USP_InsertOrderTable(N'"+nameCustomer+"',N'"+phone+"','"+checkin+"', "+ idTable + ")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void DeleteOrderTable(int idTable) {
		String sql = "{CALL USP_DeleteOrderTable("+idTable+")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static String GetStatusTable(int idTable) {
		String nameStatus = "";
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT tableStatus FROM dbo.TableFood WHERE id = " + idTable + "";
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				nameStatus = rs.getString(1);
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
		return nameStatus;
	}
	public static void updateTable(int idTable) {
		Connection cnn = MyConnection.getConnection();
		String sql = "UPDATE dbo.TableFood SET tableStatus = N'Có người' WHERE id = " + idTable + "";
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			pstm.executeUpdate();
			cnn.close();
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
