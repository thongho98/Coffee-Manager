package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class IntroSoftwareController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnQuit;

    @FXML
    void ClickQuit(ActionEvent event) {
    	Stage stage = (Stage) btnQuit.getScene().getWindow();
		stage.close();
    }

    @FXML
    void initialize() {
       
    }
}
