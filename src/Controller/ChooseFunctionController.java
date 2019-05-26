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

public class ChooseFunctionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAdmin;

    @FXML
    private Button btnOrder;

    @FXML
    private Button btnHome;

    @FXML
    void initialize() {
       
    }
    
    @FXML
    void ClickAdmin(ActionEvent event) {
    	String linkScene = "/View/HomeAdmin.fxml";
		ChangeScene(event, linkScene);
    }

    @FXML
    void ClickHome(ActionEvent event) {
    	String linkScene = "/View/Login.fxml";
		ChangeScene(event, linkScene);
    }

    @FXML
    void ClickOrder(ActionEvent event) {
    	String linkScene = "/View/HomeStaff.fxml";
		ChangeScene(event, linkScene);
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
			if (link.equalsIgnoreCase("/View/Login.fxml")) {
				stage.setX(400);
				;
				stage.setY(100);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

