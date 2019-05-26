package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXComboBox;

import BEAN.Food;
import BEAN.FoodCategory;
import DAO.AdminFoodDAO;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AdminFoodController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnHome;

	@FXML
	private TableView<Food> tableFood;

	@FXML
	private TableColumn<Food, Integer> idFood;

	@FXML
	private TableColumn<Food, String> foodName;

	@FXML
	private TableColumn<Food, Integer> price;

	@FXML
	private TableColumn<Food, String> foodCategory;

	@FXML
	private TextField txtFindFood;

	@FXML
	private Button btnFindFood;

	@FXML
	private Button btnAddFood;

	@FXML
	private Button btnUpdateFood;

	@FXML
	private Button btnDeleteFood;

	@FXML
	private TextField txtIDFood;

	@FXML
	private TextField txtFoodName;

	@FXML
	private TextField txtPrice;

	@SuppressWarnings("rawtypes")
	@FXML
	private JFXComboBox cbFoodCategory;

	private ObservableList<FoodCategory> menuListFoodCategory;
	private ObservableList<Food> listFoodFind;
	private ObservableList<Food> listFood;

	@FXML
	void initialize() {
		LoadFood();
		LoadComboFoodCategory();
	}

	public void Reset() {
		txtFoodName.setText("");
		txtIDFood.setText("");
		txtPrice.setText("");
		cbFoodCategory.getSelectionModel().selectFirst();
	}

	@FXML
	void AddFood(ActionEvent event) {
		String nameFood = CheckDAO.chuanHoaDanhTuRieng(txtFoodName.getText());
		int idCategory = AdminFoodDAO.GetIDFoodCategory((String) cbFoodCategory.getValue());
		int price = -1;
		if (nameFood.equalsIgnoreCase("")) {
			String error = "Tên thức uống không được để trống!";
			showAlertWithoutHeaderText(error);
		} else if (!CheckPriceFood(txtPrice.getText())) {
			String error = "Đơn giá phải là một giá trị số phải lớn hơn 0!";
			showAlertWithoutHeaderText(error);
		} else {
			price = Integer.valueOf(txtPrice.getText());
			boolean check = AdminFoodDAO.CheckSameNameFood(nameFood);
			if (!check) {
				AdminFoodDAO.InserFood(nameFood, price, idCategory);
				String error = "Thêm thức uống thành công!";
				showAlertWithoutHeaderText(error);
				LoadFood();
				Reset();
			} else {
				String error = "Tên thức uống tồn tại! Vui lòng đặt tên khác!";
				showAlertWithoutHeaderText(error);
			}
		}
	}

	@FXML
	void DeleteFood(ActionEvent event) {
		Food selected = tableFood.getSelectionModel().getSelectedItem();
		if (selected != null) {
			int idFood = selected.getId();
			String error = "Bạn có muốn xóa thức uống này không?";
			String title = "Xóa thức uống";
			showAlertWithoutHeaderTextAnswer(title, error, idFood);
			Reset();
		} else {
			String error = "Vui lòng chọn thức uống!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void UpdateFood(ActionEvent event) {
		Food selected = tableFood.getSelectionModel().getSelectedItem();
		if (selected != null) {
			int idFood = selected.getId();
			String oldName = AdminFoodDAO.GetNameFood(idFood);
			String newName = CheckDAO.chuanHoaDanhTuRieng(txtFoodName.getText());
			int price = -1;
			int idCategory = AdminFoodDAO.GetIDFoodCategory((String) cbFoodCategory.getValue());
			if (newName.equalsIgnoreCase("")) {
				String error = "Tên thức uống không được để trống!";
				showAlertWithoutHeaderText(error);
			} else if (!CheckPriceFood(txtPrice.getText())) {
				String error = "Đơn giá phải là một giá trị số phải lớn hơn 0!";
				showAlertWithoutHeaderText(error);
			} else {
				price = Integer.valueOf(txtPrice.getText());
				//Ten cu = ten moi => cap nhat gia, idCategory
				//Nguoc lai ten cu != ten moi && checkSameName => cap nhat het
				if(oldName.equalsIgnoreCase(newName)) {
					AdminFoodDAO.UpdateFood(idFood, newName, price, idCategory);
					String error = "Cập nhật thức uống thành công!";
					showAlertWithoutHeaderText(error);
					LoadFood();
					Reset();
				}else {
					boolean checkSameName = AdminFoodDAO.CheckSameNameFood(newName);
					if (!checkSameName) {
						AdminFoodDAO.UpdateFood(idFood, newName, price, idCategory);
						String error = "Cập nhật thức uống thành công!";
						showAlertWithoutHeaderText(error);
						LoadFood();
						Reset();
					} else {
						String error = "Tên thức uống tồn tại! Vui lòng đặt lại tên khác!";
						showAlertWithoutHeaderText(error);
					}
				}
				
			}
		} else {
			String error = "Vui lòng chọn thức uống!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void ClickFindFood(ActionEvent event) {
		tableFood.getItems().clear();
		String findFood = txtFindFood.getText();
		List<Food> list = AdminFoodDAO.LoadListFoodFind(findFood);

		idFood.setCellValueFactory(new PropertyValueFactory<Food, Integer>("id"));
		foodName.setCellValueFactory(new PropertyValueFactory<Food, String>("nameFood"));
		price.setCellValueFactory(new PropertyValueFactory<Food, Integer>("price"));
		foodCategory.setCellValueFactory(new PropertyValueFactory<Food, String>("nameCategory"));
		listFoodFind = FXCollections.observableArrayList();
		if (!list.isEmpty()) {
			for (Food food : list) {
				listFoodFind.add(new Food(food.getId(), food.getNameFood(), food.getPrice(), food.getIdCategory(),
						food.getNameCategory()));
				tableFood.setItems(listFoodFind);
			}
			list.clear();
		} else {
			tableFood.getItems().clear();
		}
	}

	@FXML
	void clickItem(MouseEvent event) {
		tableFood.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				LoadTextField();
			}
		});
	}

	private void LoadTextField() {
		Food selected = tableFood.getSelectionModel().getSelectedItem();
		if (selected != null) {
			txtIDFood.setText(String.valueOf(selected.getId()));
			txtFoodName.setText(selected.getNameFood());
			txtPrice.setText(String.valueOf(selected.getPrice()));
			cbFoodCategory.getSelectionModel().select(selected.getIdCategory() - 1);
		} else {
			txtIDFood.setText("");
			txtFoodName.setText("");
			txtPrice.setText("");
			cbFoodCategory.getSelectionModel().selectFirst();
		}
	}

	private void LoadFood() {
		List<Food> list = AdminFoodDAO.LoadListFood();

		idFood.setCellValueFactory(new PropertyValueFactory<Food, Integer>("id"));
		foodName.setCellValueFactory(new PropertyValueFactory<Food, String>("nameFood"));
		price.setCellValueFactory(new PropertyValueFactory<Food, Integer>("price"));
		foodCategory.setCellValueFactory(new PropertyValueFactory<Food, String>("nameCategory"));
		listFood = FXCollections.observableArrayList();
		if (!list.isEmpty()) {
			for (Food food : list) {
				listFood.add(new Food(food.getId(), food.getNameFood(), food.getPrice(), food.getIdCategory(),
						food.getNameCategory()));
				tableFood.setItems(listFood);
			}
			list.clear();
		} else {
			tableFood.getItems().clear();
		}
	}

	@SuppressWarnings("unchecked")
	private void LoadComboFoodCategory() {
		cbFoodCategory.getItems().clear();
		menuListFoodCategory = HomeStaffDAO.LoadFoodCategoryList();
		for (FoodCategory foodCategory : menuListFoodCategory) {
			cbFoodCategory.getItems().addAll(foodCategory.getNameCategory());
		}
		cbFoodCategory.getSelectionModel().selectFirst();
	}

	@FXML
	void ClickHome(ActionEvent event) {
		String link = "/View/HomeAdmin.fxml";
		ChangeScene(event, link);
	}

	private boolean CheckPriceFood(String price) {
		boolean result = false;
		String NAMETABLE_PATTERN = "\\d*";
		Pattern pattern = Pattern.compile(NAMETABLE_PATTERN);
		Matcher matcher = pattern.matcher(price);
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

	private void showAlertWithoutHeaderTextAnswer(String title, String error, int idFood) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(error);
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == null) {

		} else if (option.get() == ButtonType.OK) {
			AdminFoodDAO.DeleteBillDetailByIdFood(idFood);
			AdminFoodDAO.DeleteFood(idFood);
			String error_1 = "Xóa thức uống thành công!";
			showAlertWithoutHeaderText(error_1);
			LoadFood();
		}
	}
}
