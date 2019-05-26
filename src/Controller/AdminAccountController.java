package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import BEAN.Account;
import BEAN.TypeRight;
import DAO.AdminAccountDAO;
import DAO.CheckDAO;
import DAO.LoginDAO;
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

public class AdminAccountController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnHome;

	@FXML
	private TableView<Account> tableAccount;

	@FXML
	private TableColumn<Account, Integer> idAccount;

	@FXML
	private TableColumn<Account, String> displayName;

	@FXML
	private TableColumn<Account, String> username;

	@FXML
	private TableColumn<Account, String> typeRight;

	@FXML
	private Button btnAddAccount;

	@FXML
	private Button btnUpdateAccount;

	@FXML
	private Button btnDeleteAccount;

	@FXML
	private Button btnResetPassword;

	@FXML
	private JFXTextField txtIdAccount;

	@FXML
	private JFXTextField txtDisplayName;

	@FXML
	private JFXTextField txtUsername;

	@FXML
	private JFXPasswordField password;

	@SuppressWarnings("rawtypes")
	@FXML
	private JFXComboBox cbTypeRight;

	private ObservableList<Account> menuList;
	private ObservableList<TypeRight> menuListTypeRight;

	@FXML
	void initialize() {
		LoadAccount();
		LoadComboTypeRight();
	}

	private void LoadAccount() {
		List<Account> list = AdminAccountDAO.LoadListAccount();
		idAccount.setCellValueFactory(new PropertyValueFactory<Account, Integer>("id"));
		displayName.setCellValueFactory(new PropertyValueFactory<Account, String>("displayname"));
		username.setCellValueFactory(new PropertyValueFactory<Account, String>("username"));
		typeRight.setCellValueFactory(new PropertyValueFactory<Account, String>("typeright"));
		menuList = FXCollections.observableArrayList();
		if (!list.isEmpty()) {
			for (Account acc : list) {
				menuList.add(new Account(acc.getId(), acc.getDisplayname(), acc.getUsername(), acc.getPassword(),
						acc.getTyperight()));
				tableAccount.setItems(menuList);
			}
			list.clear();
		} else {
			tableAccount.getItems().clear();
		}
	}

	@SuppressWarnings("unchecked")
	private void LoadComboTypeRight() {
		cbTypeRight.getItems().clear();
		menuListTypeRight = AdminAccountDAO.LoadTypeRightList();
		for (TypeRight typeRight : menuListTypeRight) {
			cbTypeRight.getItems().addAll(typeRight.getTypeName());
		}
		cbTypeRight.getSelectionModel().selectFirst();
	}

	private void LoadTextField() {
		Account selected = tableAccount.getSelectionModel().getSelectedItem();
		if (selected != null) {
			txtIdAccount.setText(String.valueOf(selected.getId()));
			txtDisplayName.setText(selected.getDisplayname());
			txtUsername.setText(selected.getUsername());
			if (selected.getTyperight().equalsIgnoreCase("Quản lí")) {
				cbTypeRight.getSelectionModel().selectFirst();
			} else {
				cbTypeRight.getSelectionModel().selectLast();
			}
		} else {
			txtIdAccount.setText("");
			txtDisplayName.setText("");
			txtUsername.setText("");
			cbTypeRight.getSelectionModel().selectFirst();
		}
	}

	@FXML
	public void clickItem(MouseEvent event) {
		tableAccount.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				LoadTextField();
			}
		});
	}

	@FXML
	void ClickAddAccount(ActionEvent event) {
		String displayName = CheckDAO.chuanHoaDanhTuRieng(txtDisplayName.getText());
		String username = txtUsername.getText();
		String pass = "123";
		int typeRight = AdminAccountDAO.GetIDTypeRight((String) cbTypeRight.getValue());
		if (displayName.equalsIgnoreCase("")) {
			String error = "Tên hiển thị tài khoản không được để trống!";
			showAlertWithoutHeaderText(error);
		} else if (username.equalsIgnoreCase("")) {
			String error = "Username không được để trống!";
			showAlertWithoutHeaderText(error);
		} else {
			boolean check = AdminAccountDAO.CheckSameUsername(username);
			if (!check) {
				AdminAccountDAO.InserAccount(displayName, username, pass, typeRight);
				String error = "Thêm tài khoản thành công!";
				showAlertWithoutHeaderText(error);
				LoadAccount();
				txtIdAccount.setText("");
				txtDisplayName.setText("");
				txtUsername.setText("");
				cbTypeRight.getSelectionModel().selectFirst();
			} else {
				String error = "Username tồn tại! Vui lòng chọn tên khác";
				showAlertWithoutHeaderText(error);
			}
		}
	}

	@FXML
	void ClickDeleteAccount(ActionEvent event) {
		Account selected = tableAccount.getSelectionModel().getSelectedItem();
		if (selected != null) {
			int id = selected.getId();
			if (LoginDAO.acc.getId() == id) {
				String error = "Tài khoản hiện đang đăng nhập! Vui lòng chọn tài khoản khác!";
				showAlertWithoutHeaderText(error);
			} else {
				AdminAccountDAO.DeleteAccount(id);
				String error = "Xóa tài khoản thành công!";
				showAlertWithoutHeaderText(error);
				LoadAccount();
				txtIdAccount.setText("");
				txtDisplayName.setText("");
				txtUsername.setText("");
				cbTypeRight.getSelectionModel().selectFirst();
			}
		} else {
			String error = "Vui lòng chọn tài khoản!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void ClickUpdateAccount(ActionEvent event) {
		Account selected = tableAccount.getSelectionModel().getSelectedItem();
		if (selected != null) {
			int id = selected.getId();
			String newDisplayName = CheckDAO.chuanHoaDanhTuRieng(txtDisplayName.getText());
			String newUsername = txtUsername.getText();
			int typeRight = AdminAccountDAO.GetIDTypeRight((String) cbTypeRight.getValue());
			if (newDisplayName.equalsIgnoreCase("")) {
				String error = "Tên hiển thị tài khoản không được để trống!";
				showAlertWithoutHeaderText(error);
			} else if (newUsername.equalsIgnoreCase("")) {
				String error = "Username không được để trống!";
				showAlertWithoutHeaderText(error);
			} else {
				boolean checkSameName = AdminAccountDAO.CheckSameUsernameAndId(id, newUsername);
				if (checkSameName) {
					AdminAccountDAO.UpdateAccount(id, newDisplayName, newUsername, typeRight);
					String error = "Cập nhật tài khoản thành công!";
					showAlertWithoutHeaderText(error);
					LoadAccount();
					txtIdAccount.setText("");
					txtDisplayName.setText("");
					txtUsername.setText("");
					cbTypeRight.getSelectionModel().selectFirst();
				} else {
					String error = "Username không tồn tại!";
					showAlertWithoutHeaderText(error);
				}
			}
		} else {
			String error = "Vui lòng chọn tài khoản!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void ClickResetPassword(ActionEvent event) {
		Account selected = tableAccount.getSelectionModel().getSelectedItem();
		if (selected != null) {
			int id = selected.getId();
			if (id > 0) {
				AdminAccountDAO.ResetPassword(id);
				String error = "Đặt lại mật khẩu thành công!";
				showAlertWithoutHeaderText(error);
				LoadAccount();
				txtIdAccount.setText("");
				txtDisplayName.setText("");
				txtUsername.setText("");
				cbTypeRight.getSelectionModel().selectFirst();
			} else {
				String error = "Đặt lại mật khẩu thất bại!";
				showAlertWithoutHeaderText(error);
			}
		} else {
			String error = "Vui lòng chọn tài khoản!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void ClickHome(ActionEvent event) {
		String link = "/View/HomeAdmin.fxml";
		ChangeScene(event, link);
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
