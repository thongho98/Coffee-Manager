package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import BEAN.BillDetail;
import BEAN.Food;
import BEAN.FoodCategory;
import BEAN.Menu;
import BEAN.TableFood;
import DB.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HomeStaffDAO {
	// Load khu vuc

	// Load table list
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

	public static ObservableList<TableFood> LoadTableFood() {
		String sql = "{CALL USP_GetTableListEmpty}";
		Connection cnn = MyConnection.getConnection();
		ObservableList<TableFood> list = FXCollections.observableArrayList();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("nameTable");
				String tableStatus = rs.getString("tableStatus");

				TableFood table = new TableFood();
				table.setId(id);
				table.setName(name);
				table.setIdStatus(tableStatus);

				list.add(table);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}

	// Kiem tra bill chua dc thanh toan
	public static int GetUncheckBill(int idTable) {
		int idBill = 0;
		String sql = "{CALL USP_GetUncheckBill(" + idTable + ")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				idBill = rs.getInt(1);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return idBill;
	}
	
	public static int checkBillDetail(int idBill) {
		int count = 0;
		String sql = "{CALL USP_checkBillDetail(" + idBill + ")}";
		Connection cnn = MyConnection.getConnection();
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
	
	// Lay list BillDetail
	public static List<BillDetail> GetListBillDetail(Connection cnn, int id) {
		String sql = "{CALL USP_GetListBillInfo}";
		List<BillDetail> list = new ArrayList<BillDetail>();
		cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int idBillDetail = rs.getInt("id");
				int idBill = rs.getInt("idBill");
				int idFood = rs.getInt("idFood");
				int quantity = rs.getInt("quantity");

				BillDetail detail = new BillDetail();
				detail.setId(idBillDetail);
				detail.setIdBill(idBill);
				detail.setIdFood(idFood);
				detail.setQuantity(quantity);

				list.add(detail);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}

	// Lay list menu de hien thi cac mon an/thuc uong
	public static List<Menu> GetListMenu(Connection cnn, int id) {
		String sql = "{CALL USP_GetListMenu(" + id + ")}";
		List<Menu> list = new ArrayList<Menu>();
		cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				String nameFood = rs.getString(1);
				int soluong = rs.getInt(2);
				int price = rs.getInt(3);
				int total = rs.getInt(4);

				Menu m = new Menu();
				m.setName(nameFood);
				m.setCount(soluong);
				m.setPrice(price);
				m.setTotal(total);

				list.add(m);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}

	// Load Food Category
	public static ObservableList<FoodCategory> LoadFoodCategoryList() {
		String sql = "{CALL USP_GetFoodCategory}";
		Connection cnn = MyConnection.getConnection();
		ObservableList<FoodCategory> list = FXCollections.observableArrayList();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String nameCategory = rs.getString("nameCategory");

				FoodCategory foodcategory = new FoodCategory();
				foodcategory.setId(id);
				foodcategory.setNameCategory(nameCategory);

				list.add(foodcategory);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}

	// Load Food By Category
	public static ObservableList<Food> LoadFoodByIdCategory(int idCategory) {
		String sql = "{CALL USP_GetFoodByCategory(" + idCategory + ")}";
		Connection cnn = MyConnection.getConnection();
		ObservableList<Food> list = FXCollections.observableArrayList();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String nameFood = rs.getString("nameFood");
				int price = rs.getInt("price");
				int idCategoryFood = rs.getInt("idCategory");

				Food food = new Food();
				food.setId(id);
				food.setNameFood(nameFood);
				food.setPrice(price);
				food.setIdCategory(idCategoryFood);

				list.add(food);
			}
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return list;
	}

	public static void InserBill(int idTable) {
		String sql = "{CALL USP_InsertBill(" + idTable + ")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();

		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}

	public static void InserBillDetail(int idBill, int idFood, int quantity) {
		String sql = "{CALL USP_InsertBillDetail(" + idBill + "," + idFood + "," + quantity + ")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.execute();
			cnn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void SubBillDetail(int idBill, int idFood, int quantity) {
		String sql = "{CALL USP_UpdateBillDetail(" + idBill + "," + idFood + "," + quantity + ")}";
		Connection cnn = MyConnection.getConnection();
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.executeUpdate();
			cnn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean checkIDFoodInBillDetail(int idBill, int idFood) {
		int count = 0;
		Connection cnn = MyConnection.getConnection();
		String sql = "{CALL USP_CheckIDFoodInFoodDetail(" + idFood + "," + idBill + ")}";
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			ResultSet rs = cstm.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count <= 0) {
				return false;
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			System.err.println("Loi: " + e.getMessage());
		}
		return true;
	}

	public static void switchTable(int idTable1, int idTable2) {
		Connection cnn = MyConnection.getConnection();
		String sql = "{CALL USP_SwitchTable(" + idTable1 + "," + idTable2 + ")}";
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.execute();
			cnn.close();
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public static int getIDBillMax() {
		int idmax = 0;
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT MAX(id) FROM dbo.Bill";
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				idmax = rs.getInt(1);
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			return 1;
		}
		return idmax;
	}

	public static int getIDFood(String nameFood) {
		int idFood = 0;
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT id FROM dbo.Food WHERE nameFood = N'" + nameFood + "'";
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				idFood = rs.getInt(1);
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			return -1;
		}
		return idFood;
	}

	public static void CheckOutBill(int idBill, int totalPrice) {
		Connection cnn = MyConnection.getConnection();
		String sql = "{CALL USP_UpdateBillTotal(" + idBill + ", " + totalPrice + ")}";
		try {
			CallableStatement cstm = cnn.prepareCall(sql);
			cstm.execute();
			cnn.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
	}

	public static int getIDTable(String nameTable) {
		int id = 0;
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT id FROM dbo.TableFood WHERE nameTable = N'" + nameTable + "'";
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

	public static String getStatusTable(int id) {
		String status = "";
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT tableStatus FROM dbo.TableFood WHERE id = " + id;
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				status = rs.getString(1);
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return status;
	}

	public static String getNameTable(int id) {
		String name = "";
		Connection cnn = MyConnection.getConnection();
		String sql = "SELECT nameTable FROM dbo.TableFood WHERE id = " + id;
		try {
			PreparedStatement pstm = cnn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				name = rs.getString(1);
			}
			cnn.close();
			rs.close();
		} catch (Exception e) {
			System.err.println("Loi : " + e);
		}
		return name;
	}
}
