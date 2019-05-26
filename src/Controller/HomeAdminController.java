package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnDoanhThu;

    @FXML
    private Button btnAccount;

    @FXML
    private Button btnFoodCategory;

    @FXML
    private Button btnFood;

    @FXML
    private Button btnTable;

    @FXML
    private Button btnLogout;

    @FXML
    void initialize() {


    }
    
    @FXML
    void ClickAccount(ActionEvent event) {
    	String link = "/View/AdminAccount.fxml";
    	ChangeScene(event, link);
    }

    @FXML
    void ClickDoanhThu(ActionEvent event) {
    	String link = "/View/AdminReceipt.fxml";
    	ChangeScene(event, link);
    }

    @FXML
    void ClickFood(ActionEvent event) {
    	String link = "/View/AdminFood.fxml";
    	ChangeScene(event, link);
    }

    @FXML
    void ClickFoodCategory(ActionEvent event) {
    	String link = "/View/AdminFoodCategory.fxml";
    	ChangeScene(event, link);
    }

    @FXML
    void ClickTable(ActionEvent event) {
    	String link = "/View/AdminTable.fxml";
    	ChangeScene(event, link);
    }
    
    @FXML
    void ClickLogout(ActionEvent event) {
    	String link = "/View/ChooseFunction.fxml";
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
}
