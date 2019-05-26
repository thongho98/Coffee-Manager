package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import BEAN.FoodCategory;
import DB.MyConnection;
import javafx.collections.FXCollections;

public class AdminFoodCategoryDAO {
	// Load Food Category
	public static List<FoodCategory> LoadListFoodCategory() {
		String sql = "{CALL USP_GetListFoodCategory}";
		Connection cnn = MyConnection.getConnection();
		List<FoodCategory> list = FXCollections.observableArrayList();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int idCategory = rs.getInt("id");
				String nameFoodCategory = rs.getString("nameCategory");

				FoodCategory foodCategory = new FoodCategory();
				foodCategory.setId(idCategory);
				foodCategory.setNameCategory(nameFoodCategory);
				list.add(foodCategory);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}

	public static void InserFoodCategory(String nameCategory) {
		String sql = "{CALL USP_InsertFoodCategory(N'" + nameCategory + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void DeleteFoodByIDCategory(int idCategory) {
		String sql = "{CALL USP_DeleteFoodByIDCategory(" + idCategory + ")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void DeleteFoodCategory(int id) {
		String sql = "{CALL USP_DeleteCategory(N'" + id + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void UpdateNameCategory(int idCategory, String nameCategory) {
		String sql = "{CALL USP_UpdateCategory("+idCategory+", N'" + nameCategory + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static boolean CheckSameNameCategory(String nameCategory) {
		String sql = "{CALL USP_CheckSameNameCategory(N'" + nameCategory + "')}";
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
