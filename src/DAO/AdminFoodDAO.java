package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import BEAN.Food;
import DB.MyConnection;
import javafx.collections.FXCollections;

public class AdminFoodDAO {
	// Load Food Category
	public static List<Food> LoadListFood() {
		String sql = "{CALL USP_GetListFood}";
		Connection cnn = MyConnection.getConnection();
		List<Food> list = FXCollections.observableArrayList();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int idFood = rs.getInt("id");
				String nameFood = rs.getString("nameFood");
				int price = rs.getInt("price");
				int idCategory = rs.getInt("idCategory");
				String nameCategory = rs.getString("nameCategory");
				Food food = new Food();
				food.setId(idFood);
				food.setNameFood(nameFood);
				food.setPrice(price);
				food.setIdCategory(idCategory);
				food.setNameCategory(nameCategory);
				list.add(food);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}
	
	public static List<Food> LoadListFoodFind(String findFood) {
		String sql = "{CALL USP_FindFood(N'%"+findFood+"%')}";
		Connection cnn = MyConnection.getConnection();
		List<Food> list = FXCollections.observableArrayList();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int idFood = rs.getInt("id");
				String nameFood = rs.getString("nameFood");
				int price = rs.getInt("price");
				int idCategory = rs.getInt("idCategory");
				String nameCategory = rs.getString("nameCategory");
				
				Food food = new Food();
				food.setId(idFood);
				food.setNameFood(nameFood);
				food.setPrice(price);
				food.setIdCategory(idCategory);
				food.setNameCategory(nameCategory);
				list.add(food);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}
	public static void InserFood(String nameFood,int price,int idCategory) {
		String sql = "{CALL USP_InsertFood(N'" + nameFood + "',"+price+","+idCategory+")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void DeleteFood(int id) {
		String sql = "{CALL USP_DeleteFood(N'" + id + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static void DeleteBillDetailByIdFood(int id) {
		String sql = "{CALL USP_DeleteBillDetailByIdFood(N'" + id + "')}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	public static void UpdateFood(int idFood, String nameFood,int price, int idCategory) {
		String sql = "{CALL USP_UpdateFood("+idFood+", N'" + nameFood + "',"+price+","+idCategory+")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}
	
	public static int GetIDFoodCategory(String nameFoodCategory) {
		int idFoodCategory = 0;
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT id FROM dbo.FoodCategory WHERE nameCategory = N'" + nameFoodCategory + "'";
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				idFoodCategory = rs.getInt(1);
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			return -1;
		}
		return idFoodCategory;
	}
	
	public static String GetNameFood(int id) {
		String nameFood = "";
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT nameFood FROM dbo.Food WHERE id = " + id + "";
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				nameFood = rs.getString(1);
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
		return nameFood;
	}
	
	public static boolean CheckSameNameFood(String nameFood) {
		String sql = "{CALL USP_CheckSameNameFood(N'" + nameFood + "')}";
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
