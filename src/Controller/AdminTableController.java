package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXTextField;

import BEAN.TableFood;
import DAO.AdminTableDAO;
import DAO.CheckDAO;
import DAO.HomeStaffDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AdminTableController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnHome;

	@FXML
	private TableView<TableFood> table;

	@FXML
	private TableColumn<TableFood, Integer> columIdTable;

	@FXML
	private TableColumn<TableFood, String> columTableName;

	@FXML
	private TableColumn<TableFood, String> columStatus;

	@FXML
	private Button btnAddTable;

	@FXML
	private Button btnUpdateTable;

	@FXML
	private Button btnDeleteTable;

	@FXML
	private JFXTextField txtIDTable;

	@FXML
	private JFXTextField txtNameTable;

	@FXML
	private JFXTextField txtStatus;

	private ObservableList<TableFood> menuList;

	@FXML
	void initialize() {
		LoadTable();
	}

	@FXML
	void ClickAddTableFood(ActionEvent event) {
		String nameTable = CheckDAO.chuanHoaDanhTuRieng(txtNameTable.getText());
		if (nameTable.equalsIgnoreCase("")) {
			String error = "Tên bàn không được để trống!";
			showAlertWithoutHeaderText(error);
		} else if (!CheckNameTable(nameTable)) {
			String error = "Tên bàn là một số!";
			showAlertWithoutHeaderText(error);
		} else {
			boolean checkSameName = AdminTableDAO.CheckSameNameTable(nameTable);
			if (!checkSameName) {
				AdminTableDAO.InserTable(nameTable);
				String error = "Thêm bàn thành công!";
				showAlertWithoutHeaderText(error);
				LoadTable();
				txtIDTable.setText("");
				txtNameTable.setText("");
				txtStatus.setText("");
			} else {
				String error = "Tên bàn tồn tại!";
				showAlertWithoutHeaderText(error);
			}
		}
	}

	@FXML
	void ClickDeleteTableFood(ActionEvent event) {
		TableFood selected = table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			int idTable = selected.getId();
			String tableStatus = selected.getIdStatus();
			if (tableStatus.equalsIgnoreCase("Trống")) {
				AdminTableDAO.DeleteTableFood(idTable);
				String error = "Xóa bàn thành công!";
				showAlertWithoutHeaderText(error);
				LoadTable();
			} else {
				String error = "Bàn đang có khách ngồi! Vui lòng chọn bàn khác";
				showAlertWithoutHeaderText(error);
			}
		} else {
			String error = "Vui lòng chọn bàn!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void ClickUpdateTableFood(ActionEvent event) {
		TableFood selected = table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			int idTable = selected.getId();
			String newNameTable = txtNameTable.getText();
			if (!CheckNameTable(newNameTable)) {
				String error = "Tên bàn là một số!";
				showAlertWithoutHeaderText(error);
			} else {
				boolean checkSameName = AdminTableDAO.CheckSameNameTable(newNameTable);
				if (!checkSameName) {
					AdminTableDAO.UpdateNameTable(idTable, newNameTable);
					String error = "Cập nhật bàn thành công!";
					showAlertWithoutHeaderText(error);
					LoadTable();
					txtIDTable.setText("");
					txtNameTable.setText("");
					txtStatus.setText("");
				} else {
					String error = "Tên bàn tồn tại! Vui lòng đặt lại tên khác!";
					showAlertWithoutHeaderText(error);
				}
			}
		} else {
			String error = "Vui lòng chọn bàn!";
			showAlertWithoutHeaderText(error);
		}

	}

	private void LoadTable() {
		List<TableFood> list = HomeStaffDAO.LoadTableList();
		columIdTable.setCellValueFactory(new PropertyValueFactory<TableFood, Integer>("id"));
		columTableName.setCellValueFactory(new PropertyValueFactory<TableFood, String>("name"));
		columStatus.setCellValueFactory(new PropertyValueFactory<TableFood, String>("idStatus"));
		menuList = FXCollections.observableArrayList();
		if (!list.isEmpty()) {
			for (TableFood tableFood : list) {
				menuList.add(new TableFood(tableFood.getId(), tableFood.getName(), tableFood.getIdStatus()));
				table.setItems(menuList);
			}
			list.clear();
		} else {
			table.getItems().clear();
		}
	}

	private void LoadTextField() {
		TableFood selected = table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			txtIDTable.setText(String.valueOf(selected.getId()));
			txtNameTable.setText(selected.getName());
			txtStatus.setText(selected.getIdStatus());
		} else {
			txtIDTable.setText("");
			txtNameTable.setText("");
			txtStatus.setText("");
		}
	}

	@FXML
	public void clickItem(MouseEvent event) {
		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				LoadTextField();
			}
		});
	}

	@FXML
	void ClickHome(ActionEvent event) {
		String link = "/View/HomeAdmin.fxml";
		ChangeScene(event, link);
	}

	private boolean CheckNameTable(String maSachKT) {
		boolean result = false;
		String NAMETABLE_PATTERN = "\\d*";
		Pattern pattern = Pattern.compile(NAMETABLE_PATTERN);
		Matcher matcher = pattern.matcher(maSachKT);
		result = matcher.matches();
		return result;
	}

	private void ChangeScene(ActionEvent event, String link) {
		try {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(link));
			Parent homestaff = loader.load();
			Scene scene = new Scene(homestaff);
			scene.getStylesheets().add("View/application.css");
			stage.setScene(scene);
			stage.setX(0);
			stage.setY(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showAlertWithoutHeaderText(String error) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notification");
		alert.setHeaderText(null);
		alert.setContentText(error);
		alert.showAndWait();
	}
}
