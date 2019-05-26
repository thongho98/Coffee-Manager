package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import DAO.AccountInformationDAO;
import DAO.CheckDAO;
import DAO.LoginDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AccountInformationController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXTextField txtDisplayName;

	@FXML
	private JFXTextField txtUsername;

	@FXML
	private JFXPasswordField txtPassword;

	@FXML
	private JFXPasswordField txtNewPassword;

	@FXML
	private JFXPasswordField txtConfirmPassword;
	@FXML
	private JFXTextField txtTypeRight;
	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnQuit;

	@FXML
	void ClickUpdate(ActionEvent event) {
		String userName = txtUsername.getText();
		String pass = CheckDAO.md5(txtPassword.getText());
		String newPass = CheckDAO.md5(txtNewPassword.getText());
		String confirmPass = CheckDAO.md5(txtConfirmPassword.getText());
		if (pass.equalsIgnoreCase("d41d8cd98f00b204e9800998ecf8427e")) {
			String error_1 = "Vui lòng nhập mật khẩu!";
			showAlertWithoutHeaderText(error_1);
		} else if (newPass.equalsIgnoreCase("d41d8cd98f00b204e9800998ecf8427e")) {
			String error_2 = "Vui lòng nhập mật khẩu mới!";
			showAlertWithoutHeaderText(error_2);
		} else if (confirmPass.equals("d41d8cd98f00b204e9800998ecf8427e")) {
			String error_3 = "Vui lòng nhập xác nhận lại mật khẩu mới!";
			showAlertWithoutHeaderText(error_3);
		} else {
			
			if (AccountInformationDAO.CheckPass(userName, pass) && newPass.equals(confirmPass)
					&& (AccountInformationDAO.UpdateAccountInfo(userName, pass, newPass))) {
				
				String error = "Cập nhật thành công!";
				showAlertWithoutHeaderText(error);
				
				txtPassword.setText("");
				txtNewPassword.setText("");
				txtConfirmPassword.setText("");
				//Success -> ChangeScene to Login
				
			} else if(AccountInformationDAO.CheckPass(userName, pass) == false){
				String error_4 = "Vui lòng xác nhận mật khẩu cũ!";
				showAlertWithoutHeaderText(error_4);
			}
			else {
				String error_4 = "Vui lòng xác nhận mật khẩu mới!";
				showAlertWithoutHeaderText(error_4);
			}
		}
	}

	@FXML
	void ClickQuit(ActionEvent event) {
		Stage stage = (Stage) btnQuit.getScene().getWindow();
		stage.close();
	}

	@FXML
	void initialize() {
		LoadInformation();
	}

	private void LoadInformation() {
		txtDisplayName.setText(LoginDAO.acc.getDisplayname());
		txtUsername.setText(LoginDAO.acc.getUsername());
		txtTypeRight.setText(LoginDAO.acc.getTyperight());
	}

	private void showAlertWithoutHeaderText(String error) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notification");
		// Header Text: null
		alert.setHeaderText(null);
		alert.setContentText(error);
		alert.showAndWait();
	}
}