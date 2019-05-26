package Controller;

import java.net.URL;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import BEAN.Menu;
import DAO.HomeStaffDAO;
import DB.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PrintBillController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnPrintBill;

	@FXML
	private TextField txtIdTable;

	@FXML
	private TextField txtTotal;

	@FXML
	private TableView<Menu> table;

	@FXML
	private TableColumn<Menu, String> nameColumn;

	@FXML
	private TableColumn<Menu, Integer> countColumn;

	@FXML
	private TableColumn<Menu, Integer> priceColumn;

	@FXML
	private TableColumn<Menu, Integer> totalColumn;

	private static ObservableList<Menu> menuList;

	@FXML
	void initialize() {
		LoadPrintBill();
	}

	private void LoadPrintBill() {
		int idTable = HomeStaffController.idTableClick;
		String nameTable = HomeStaffDAO.getNameTable(idTable);
		ShowBill(idTable);
		txtIdTable.setText(nameTable);
	}

	static int total = 0;
	private void ShowBill(int TableID) {
		Locale lc = new Locale("nv","VN");
		NumberFormat nf = NumberFormat.getInstance(lc);
		
		Connection cnn = MyConnection.getConnection();
		List<Menu> list = HomeStaffDAO.GetListMenu(cnn, TableID);
		int  totalPrice = 0;
		nameColumn.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
		countColumn.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("count"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("price"));
		totalColumn.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("total"));

		menuList = FXCollections.observableArrayList();
		if (!list.isEmpty()) {
			for (Menu menu : list) {
				menuList.add(new Menu(menu.getName(), menu.getPrice(), menu.getCount(), menu.getTotal()));
				totalPrice += menu.getTotal();
				total = totalPrice;
				table.setItems(menuList);
			}
			txtTotal.setText(String.valueOf(nf.format(totalPrice)));
			list.clear();
		} else {
			table.getItems().clear();
			txtTotal.setText(String.valueOf(nf.format(0)));
		}
	}

	@FXML
	void ClickPrintBill(ActionEvent event) {
		String error = "In hóa đơn thành công!";
		showAlertWithoutHeaderText(error);
		Stage stage = (Stage) btnPrintBill.getScene().getWindow();
		stage.close();
	}

	private void showAlertWithoutHeaderText(String error) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notification");
		alert.setHeaderText(null);
		alert.setContentText(error);
		alert.showAndWait();
	}

}
