package Controller;

import java.io.IOException;
import java.sql.Connection;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import DAO.CheckDAO;
import DAO.LoginDAO;
import DB.MyConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private JFXButton btnLogin;
	@FXML
	private Button btnQuit;
	@FXML
	private JFXTextField txtUsername;
	@FXML
	private JFXPasswordField txtPassword;

	@FXML
	private Hyperlink hyperlinkForgotPass;

	//Chuyen cac Scene
	private void ChangeScene(ActionEvent event, String title, String link) {
		try {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(link));
			Parent homestaff = loader.load();
			Scene scene = new Scene(homestaff);
			scene.getStylesheets().add("View/application.css");
			stage.setTitle(title);
			stage.setScene(scene);
			stage.setX(0);
			stage.setY(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showAlertWithoutHeaderText(String error) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông báo");
		// Header Text: null
		alert.setHeaderText(null);
		alert.setContentText(error);
		alert.showAndWait();
	}

	// Kiem tra txtusername or txtpassword is empty
	private boolean isEmpty(String username, String password) {
		if (username.equalsIgnoreCase(" ") && password.equalsIgnoreCase(" ")) {
			return false;
		}
		return true;
	}

	public void ClickLogin(ActionEvent event) throws IOException {
		String username = txtUsername.getText();
		String password = CheckDAO.md5(txtPassword.getText());
		Connection cnn = MyConnection.getConnection();
		if (isEmpty(username, password)) {
			if (LoginDAO.Login(username, password, cnn)) {
				LoginDAO.GetAccountByUsername(username, cnn);
				if(CheckTypeRightAdmin()) {
				String title = "Home";
				String linkScene = "/View/ChooseFunction.fxml";
				ChangeScene(event, title, linkScene);
				}else {
					String title = "Home";
					String linkScene = "/View/HomeStaff.fxml";
					ChangeScene(event, title, linkScene);
				}
			} else {
				String error = "Username or password wrong!!";
				showAlertWithoutHeaderText(error);
			}
		} else {
			String error = "Username or password is not empty!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void ClickHyperLink(ActionEvent event) {
		String error = "Vui lòng liên hệ với quản lí để lấy lại mật khẩu!";
		showAlertWithoutHeaderText(error);
	}

	public void ClickQuit(ActionEvent event) {
		System.exit(0);
	}
	
	private boolean CheckTypeRightAdmin() {
		String check = LoginDAO.acc.getTyperight();
		if (check.equalsIgnoreCase("Quản lí")) {
			return true;
		}
		return false;
	}
}