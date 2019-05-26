package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import BEAN.FoodCategory;
import DAO.AdminFoodCategoryDAO;
import DAO.CheckDAO;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AdminFoodCategoryController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnHome;

	@FXML
	private TableView<FoodCategory> tableCategory;

	@FXML
	private TableColumn<FoodCategory, Integer> idCategory;

	@FXML
	private TableColumn<FoodCategory, String> foodCategoryName;

	@FXML
	private Button btnAddFoodCategory;

	@FXML
	private Button btnUpdateFoodCategory;

	@FXML
	private Button btnDeleteFoodCategory;

	@FXML
	private TextField txtIDCategory;

	@FXML
	private TextField txtNameCategory;

	private ObservableList<FoodCategory> menuList;

	@FXML
	void initialize() {
		LoadFoodCategory();
		LoadTextField();
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

	private void LoadFoodCategory() {
		List<FoodCategory> list = AdminFoodCategoryDAO.LoadListFoodCategory();
		idCategory.setCellValueFactory(new PropertyValueFactory<FoodCategory, Integer>("id"));
		foodCategoryName.setCellValueFactory(new PropertyValueFactory<FoodCategory, String>("nameCategory"));
		menuList = FXCollections.observableArrayList();
		if (!list.isEmpty()) {
			for (FoodCategory foodCategory : list) {
				menuList.add(new FoodCategory(foodCategory.getId(), foodCategory.getNameCategory()));
				tableCategory.setItems(menuList);
			}
			list.clear();
		} else {
			tableCategory.getItems().clear();
		}
	}

	private void LoadTextField() {
		FoodCategory selected = tableCategory.getSelectionModel().getSelectedItem();
		if (selected != null) {
			txtIDCategory.setText(String.valueOf(selected.getId()));
			txtNameCategory.setText(selected.getNameCategory());
		} else {
			txtIDCategory.setText("");
			txtNameCategory.setText("");
		}
	}

	@FXML
	public void clickItem(MouseEvent event) {
	    tableCategory.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	LoadTextField();
	        }
	    });
	}
	
	@FXML
	void ClickAddFoodCategory(ActionEvent event) {
		String nameCategory = CheckDAO.chuanHoaDanhTuRieng(txtNameCategory.getText());
		if (nameCategory.equalsIgnoreCase("")) {
			String error = "Tên loại thức uống không được để trống!";
			showAlertWithoutHeaderText(error);
		} else {
			boolean checkSameName = AdminFoodCategoryDAO.CheckSameNameCategory(nameCategory);
			if(!checkSameName) {
				AdminFoodCategoryDAO.InserFoodCategory(nameCategory);
				String error = "Thêm loại thức uống thành công!";
				showAlertWithoutHeaderText(error);
				LoadFoodCategory();
				txtIDCategory.setText("");
				txtNameCategory.setText("");
			}
			else {
				String error = "Tên loại thức uống tồn tại! Vui lòng chọn tên khác!";
				showAlertWithoutHeaderText(error);
			}
			
		}
	}

	@FXML
	void ClickDeleteFoodCategory(ActionEvent event) {
		FoodCategory selected = tableCategory.getSelectionModel().getSelectedItem();
		if (selected != null) {
			int idCategory = selected.getId();
			AdminFoodCategoryDAO.DeleteFoodByIDCategory(idCategory);
			AdminFoodCategoryDAO.DeleteFoodCategory(idCategory);
			String error = "Xóa loại thức uống thành công!";
			showAlertWithoutHeaderText(error);
			LoadFoodCategory();
			txtIDCategory.setText("");
			txtNameCategory.setText("");
		} else {
			String error = "Vui lòng chọn loại thức uống!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void ClickUpdateFoodCategory(ActionEvent event) {
		FoodCategory selected = tableCategory.getSelectionModel().getSelectedItem();
		if(selected != null) {
			int idCategory = selected.getId();
			String newName = CheckDAO.chuanHoaDanhTuRieng(txtNameCategory.getText());
			boolean checkSameName = AdminFoodCategoryDAO.CheckSameNameCategory(newName);
			if(!checkSameName) {
				AdminFoodCategoryDAO.UpdateNameCategory(idCategory, newName);
				String error = "Cập nhật loại thức uống thành công!";
				showAlertWithoutHeaderText(error);
				LoadFoodCategory();
				txtIDCategory.setText("");
				txtNameCategory.setText("");
			}
			else {
				String error = "Tên loại thức uống tồn tại! Vui lòng chọn tên khác!";
				showAlertWithoutHeaderText(error);
			}
		}
		else {
			String error = "Vui lòng chọn loại thức uống!";
			showAlertWithoutHeaderText(error);
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
