package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import BEAN.BillByDate;
import BEAN.ReceipMonthOfYear;
import DB.MyConnection;

public class AdminReceiptDAO {
	public static List<BillByDate> GetListBillByDate(Connection cnn, String dateCheckIn, String dateCheckOut) {
		String sql = "{CALL USP_GetListBillByDate(" + dateCheckIn + "," + dateCheckOut + ")}";
		List<BillByDate> list = new ArrayList<BillByDate>();
		cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String nameTable = rs.getString(2);
				String dateCheckInBill = rs.getString(3);
				String dateCheckOutBill = rs.getString(4);
				int totalPrice = rs.getInt(5);
				
				BillByDate billByDate = new BillByDate();
				billByDate.setId(id);
				billByDate.setNameTable(nameTable);
				billByDate.setDateCheckIn(dateCheckInBill);
				billByDate.setDateCheckOut(dateCheckOutBill);
				billByDate.setTotalPrice(totalPrice);
				list.add(billByDate);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}
	
	public static int GetCountBillByDate(Connection cnn, String dateCheckIn, String dateCheckOut) {
		String sql = "{CALL USP_CountBill(" + dateCheckIn + "," + dateCheckOut + ")}";
		int count = 0;
		cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return count;
	}
	
	public static List<ReceipMonthOfYear> GetReceiptMonthOfYear(String year) {
		String sql = "{CALL USP_ThongKeTuThangTheoNam(N'" + year + "')}";
		List<ReceipMonthOfYear> list = new ArrayList<ReceipMonthOfYear>();
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int month = rs.getInt(1);
				int totalPrice = rs.getInt(2);
				
				ReceipMonthOfYear receipMonthOfYear = new ReceipMonthOfYear();
				receipMonthOfYear.setMonth(month);
				receipMonthOfYear.setTotalprice(totalPrice);

				list.add(receipMonthOfYear);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}
}
