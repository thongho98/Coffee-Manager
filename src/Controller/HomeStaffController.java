package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import BEAN.Food;
import BEAN.FoodCategory;
import BEAN.Menu;
import BEAN.TableFood;
import DAO.HomeStaffDAO;
import DAO.LoginDAO;
import DB.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class HomeStaffController {
	@FXML
	private JFXButton btnInfo;
	@FXML
	private JFXButton btnLogout;
	@FXML
	private Button btnAddFood;
	@FXML
	private Button btnSubFood;
	@FXML
	private Button btnThanhToan;
	@FXML
	private Button btnSwitchTable;
	@FXML
	private JFXButton btnIntroSE;
	@FXML
	private FlowPane flPane;
	@SuppressWarnings("rawtypes")
	@FXML
	private JFXComboBox comboFoodCategory;
	@SuppressWarnings("rawtypes")
	@FXML
	private JFXComboBox comboFood;
	@SuppressWarnings("rawtypes")
	@FXML
	private JFXComboBox comboSwitchTable;
	@FXML
	private TableView<Menu> table;
	@FXML
	private TableColumn<Menu, String> nameColumn;
	@FXML
	private TableColumn<Menu, Integer> priceColumn;
	@FXML
	private TableColumn<Menu, Integer> countColumn;
	@FXML
	private TableColumn<Menu, Integer> totalColumn;
	@FXML
	private Label lbDisplayName;
	@FXML
	private Label lbType;
	@FXML
	private Label lbTable;
	@FXML
	private TextField txtTotal;
	@FXML
	private Spinner<Integer> spinnerCount;
	// ObservableList to load combobox
	private static ObservableList<Menu> menuList;
	private ObservableList<FoodCategory> menuListFoodCategory;
	private ObservableList<Food> menuListFood;
	private ObservableList<TableFood> menuListTableFood;
	private final double maxWidth = 127;
	private final double maxHeight = 127;
	public static int idTableClick = 0;
	private SpinnerValueFactory<Integer> valueFactory;

	@FXML
	void initialize() {
		LoadTable();
		LoadDisplayName();
		LoadComboFoodCategory();
		LoadSpinnerCount();
		LoadTableSwitch();
	}

	private void LoadSpinnerCount() {
		final int initialValue = 1;
		valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, initialValue);
		spinnerCount.setValueFactory(valueFactory);
	}

	public void LoadTable() {
		flPane.getChildren().clear();
		List<TableFood> list = HomeStaffDAO.LoadTableList();

		for (TableFood tableFood : list) {
			addButtonTable(tableFood.getId(), "Bàn " + tableFood.getName(), tableFood.getIdStatus());
		}
		list.clear();
	}

	public void addButtonTable(int TableID, String name, String status) {
		Button btn = new Button();
		btn.setPrefSize(maxWidth, maxHeight);
		btn.setText(name + '\n' + status);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				idTableClick = TableID;
				lbTable.setText(name);
				lbTable.setAlignment(Pos.CENTER_RIGHT);

				int check = HomeStaffDAO.GetUncheckBill(TableID);
				if (check > 0) {
					ShowBill(TableID);
				} else {
					table.getItems().clear();
					txtTotal.setText("0 VNĐ");
				}
			}
		});
		switch (status) {
		case "Trống":
//			btn.setStyle("-fx-background-color: Green");
			break;
		case "Đã đặt":
			btn.setStyle("-fx-background-color: Yellow");
			break;
		default:
			btn.setStyle("-fx-background-color: LightPink");
			break;
		}
		flPane.getChildren().add(btn);
		flPane.setHgap(25);
		flPane.setVgap(17);
	}

	@SuppressWarnings("unchecked")
	private void LoadTableSwitch() {
		menuListTableFood = HomeStaffDAO.LoadTableFood();
		for (TableFood table : menuListTableFood) {
			comboSwitchTable.getItems().addAll(table.getName());
		}
		comboSwitchTable.getSelectionModel().selectFirst();
	}

	static int total = 0;

	private void ShowBill(int TableID) {
		Connection cnn = MyConnection.getConnection();
		List<Menu> list = HomeStaffDAO.GetListMenu(cnn, TableID);
		DecimalFormat formatter = new DecimalFormat("###,###,###");
		int totalPrice = 0;
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
			txtTotal.setText(String.valueOf(formatter.format(totalPrice)));
			list.clear();
		} else {
			table.getItems().clear();
			txtTotal.setText(String.valueOf(0));
			LoadTable();
		}
		LoadTable();
	}

	private void LoadDisplayName() {
		lbDisplayName.setText(LoginDAO.acc.getDisplayname());
		lbType.setText(LoginDAO.acc.getTyperight());
	}

	@SuppressWarnings("unchecked")
	private void LoadComboFood(int idCategory) {
		comboFood.getItems().clear();
		menuListFood = HomeStaffDAO.LoadFoodByIdCategory(idCategory);
		for (Food food : menuListFood) {
			comboFood.getItems().addAll(food.getNameFood());
		}
		comboFood.getSelectionModel().selectFirst();
	}

	@SuppressWarnings("unchecked")
	private void LoadComboFoodCategory() {
		// Load Category
		menuListFoodCategory = HomeStaffDAO.LoadFoodCategoryList();
		for (FoodCategory foodCategory : menuListFoodCategory) {
			comboFoodCategory.getItems().addAll(foodCategory.getNameCategory());
		}
		comboFoodCategory.getSelectionModel().selectFirst();
		// Load Food dau tien
		LoadComboFood(1);
		// xu ly action
		comboFoodCategory.setOnAction(e -> {
			int id = comboFoodCategory.getSelectionModel().getSelectedIndex() + 1;
			LoadComboFood(id);
		});
	}

	public void AddFood(ActionEvent event) {
		int idTable = idTableClick;
		int idBill = HomeStaffDAO.GetUncheckBill(idTable);
		int idFood = HomeStaffDAO.getIDFood((String) comboFood.getValue());
		int quantity = spinnerCount.getValue();
		if (idTable > 0) {
			// Kiem tra bill co ton tai
			if (idBill <= 0) {
				HomeStaffDAO.InserBill(idTable);
				int idBillMax = HomeStaffDAO.getIDBillMax();// lay gia tri bill lon nhat
				HomeStaffDAO.InserBillDetail(idBillMax, idFood, quantity);
				LoadTable();
				ShowBill(idTable);
				valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
				spinnerCount.setValueFactory(valueFactory);
				ShowNotificationCheck();
			} else {
//			 Bill da ton tai
//			 + Neu mon ton tai thi add so luong ++;
//			 + Nguoc lai thi them mon vao bill
				HomeStaffDAO.InserBillDetail(idBill, idFood, quantity);
				ShowBill(idTable);
				valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
				spinnerCount.setValueFactory(valueFactory);
				ShowNotificationCheck();
			}
		} else {
			ShowNotificationError();
		}
	}

	public void SubFood(ActionEvent event) {
		int idTable = idTableClick;
		int idBill = HomeStaffDAO.GetUncheckBill(idTable);
		int idFood = HomeStaffDAO.getIDFood((String) comboFood.getValue());
		int quantity = spinnerCount.getValue();
		if (idTable > 0) {
			if (idBill > 0) {
				boolean check = HomeStaffDAO.checkIDFoodInBillDetail(idBill, idFood);
				if (check) {
					HomeStaffDAO.SubBillDetail(idBill, idFood, quantity);
					valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
					spinnerCount.setValueFactory(valueFactory);
					ShowNotificationCheck();
				} else {
					ShowNotificationError();
				}
				ShowBill(idTable);
			} else {
				String error = "Bill không tồn tại!";
				showAlertWithoutHeaderText(error);
			}
		} else {
			String error = "Vui lòng chọn bàn!";
			showAlertWithoutHeaderText(error);
		}

	}

	@FXML
	public void TotalBill(ActionEvent event) throws IOException {
		int idTable = idTableClick;
		int idBill = HomeStaffDAO.GetUncheckBill(idTable);
		int totalPrice = 0;
		int checkBillDetail = HomeStaffDAO.checkBillDetail(idBill);
		if (idBill > 0 && checkBillDetail > 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Notice");
			alert.setHeaderText("Bạn muốn thanh toán hóa đơn không?");
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == ButtonType.OK) {
				String title = "My Information";
				String linkScene = "/View/PrintBill.fxml";
				ShowNewWindow(event, title, linkScene);

				totalPrice = total;
				HomeStaffDAO.CheckOutBill(idBill, totalPrice);
				ShowBill(idTable);
				LoadTable();
			} else if (option.get() == ButtonType.CANCEL) {

			}
		} else {
			String link = "/images/icon_error.png";
			String title = "Notification!";
			String text = "Bill Not Found!!";
			Notification(title, text, link);
		}
	}

	public void clickSwitchTable(ActionEvent event) {
		int table_1 = idTableClick;
		int table_2 = HomeStaffDAO.getIDTable((String) comboSwitchTable.getValue());
		String nametable1 = HomeStaffDAO.getNameTable(table_1);
		String nametable2 = HomeStaffDAO.getNameTable(table_2);
		String status_tb1 = HomeStaffDAO.getStatusTable(table_1);
		String status_tb2 = HomeStaffDAO.getStatusTable(table_2);
		if (status_tb1.equalsIgnoreCase("Có người") && status_tb2.equalsIgnoreCase("Trống")) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Notice");
			alert.setHeaderText("Bạn có muốn chuyển từ bàn " + nametable1 + " sang bàn " + nametable2 + " không?");
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == ButtonType.OK) {
				HomeStaffDAO.switchTable(table_1, table_2);
				LoadTable();
				ShowBill(table_2);
			} else if (option.get() == ButtonType.CANCEL) {

			}
		} else if (table_1 == table_2) {
			String error = "Bàn bạn chọn trùng với bàn hiện tại! Vui lòng chọn bàn khác!";
			showAlertWithoutHeaderText(error);
		} else if (status_tb1.equalsIgnoreCase("Trống") && status_tb2.equalsIgnoreCase("Trống")) {
			String error = "Không thể chuyển 2 bàn ở trạng thái trống! Vui lòng chọn bàn khác!";
			showAlertWithoutHeaderText(error);
		} else if (status_tb1.equalsIgnoreCase("Trống") && status_tb2.equalsIgnoreCase("Có người")) {
			String error = "Bàn hiện tại đang trống! Vui lòng chọn bàn khác!";
			showAlertWithoutHeaderText(error);
		} else {
			String error = "Hai bàn bạn chọn đang có người ngồi! Vui lòng chọn bàn khác!";
			showAlertWithoutHeaderText(error);
		}
	}

	public void Logout(ActionEvent e) {
		if (CheckTypeRightAdmin()) {
			String linkScene = "/View/ChooseFunction.fxml";
			ChangeScene(e, linkScene);
		} else {
			String linkScene = "/View/Login.fxml";
			ChangeScene(e, linkScene);
		}

	}

	@FXML
	public void ClickOrderTable(ActionEvent event) {
		String linkScene = "/View/HomeOrderTable.fxml";
		ChangeScene(event, linkScene);
	}

	public void ClickAccountInformation(ActionEvent event) throws IOException {
		String title = "My Information";
		String linkScene = "/View/AccountInformation.fxml";
		ShowNewWindow(event, title, linkScene);
	}

	@FXML
	void ClickIntroSE(ActionEvent event) throws IOException {
		String title = "Introduce Software";
		String linkScene = "/View/IntroSoftware.fxml";
		ShowNewWindow(event, title, linkScene);
	}

	private void showAlertWithoutHeaderText(String error) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notification");
		alert.setHeaderText(null);
		alert.setContentText(error);
		alert.showAndWait();
	}

	private void Notification(String title, String text, String link) {
		Image image = new Image(link);
		Notifications notifications = Notifications.create().title(title).text(text).graphic(new ImageView(image))
				.hideAfter(Duration.millis(2 * 1000)).position(Pos.BOTTOM_LEFT).onAction((ect) -> {

				});
		notifications.darkStyle();
		notifications.show();
	}

	private void ShowNotificationCheck() {
		String link = "/images/icon_check.png";
		String title = "Notification!";
		String text = "Cập nhật thức ăn thành công!!";
		Notification(title, text, link);
	}

	private void ShowNotificationError() {
		String link = "/images/icon_error.png";
		String title = "Notification!";
		String text = "Cập nhật thức ăn thất bại!!";
		Notification(title, text, link);
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

	private void ShowNewWindow(ActionEvent event, String title, String link) throws IOException {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource(link));
			Scene scene = new Scene(fxmlLoader.load());
			scene.getStylesheets().add("View/application.css");
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(scene);
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}

	}

	private boolean CheckTypeRightAdmin() {
		String check = LoginDAO.acc.getTyperight();
		if (check.equalsIgnoreCase("Quản lí")) {
			return true;
		}
		return false;
	}

}
