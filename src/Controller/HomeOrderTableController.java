package Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import BEAN.OrderTable;
import BEAN.TableFood;
import DAO.CheckDAO;
import DAO.HomeOrderTableDAO;
import DAO.LoginDAO;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class HomeOrderTableController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXButton btnGoiMon;

	@FXML
	private JFXButton btnInfo;

	@FXML
	private JFXButton btnIntroSE;

	@FXML
	private Label lbDisplayName;

	@FXML
	private FlowPane flPane;

	@FXML
	private Label lbType;

	@FXML
	private TextField txtIDTable;

	@FXML
	private TextField txtFullName;

	@FXML
	private TextField txtPhone;

	@FXML
	private JFXDatePicker datePicker;

	@FXML
	private JFXTimePicker timePicker;

	@FXML
	private JFXDatePicker datePickerOrder;

	@FXML
	private JFXTimePicker timePickerOrder;

	@FXML
	private TextField txtTimeOrder;

	@FXML
	private TextField txtStatus;

	@FXML
	private Button btnDatBan;

	@FXML
	private Button btnHuyBan;

	private final double maxWidth = 127;
	private final double maxHeight = 127;
	private int idTableClick = 0;

	@FXML
	void initialize() {
		LoadTable();
		LoadDisplayName();
	}

	//import javafx.concurrent.Task;
	//
	//class Kiemtra extends Task<Long> {
//	  Kiemtra(thoi gian dat ban)
//		@Override
//		protected void call() throws Exception {
//			updateMessage("    Processing... ");
//			long result = checkTime();
//			updateMessage("    Done.  ");
//			\
//		}
	//
//		public boolean checkTime(String time) {
//			
//	lay thoi gian he thong
//	
//			
//		}
	//}Kiemtra ban10 = new Kiemtra()

	private void LoadTable() {
		flPane.getChildren().clear();
		List<TableFood> list = HomeOrderTableDAO.LoadTableList();
		for (TableFood tableFood : list) {
			addButtonTable(tableFood.getId(), "Bàn " + tableFood.getName(), tableFood.getIdStatus());
		}
	}

	private void addButtonTable(int TableID, String name, String status) {
		Button btn = new Button();
		btn.setPrefSize(maxWidth, maxHeight);
		btn.setText(name + '\n' + status);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				idTableClick = TableID;
				txtIDTable.setText(String.valueOf(name));
				txtStatus.setText(status);
				if (txtStatus.getText().equalsIgnoreCase("Đã đặt")) {
					LoadCustomer(idTableClick);
				} else {
					reset();
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
		case "Có người":
			btn.setStyle("-fx-background-color: LightPink");
			break;
		}
		flPane.getChildren().add(btn);
		flPane.setHgap(25);
		flPane.setVgap(17);
	}

	private void LoadCustomer(int idTable) {
		List<OrderTable> list = HomeOrderTableDAO.GetListCustomer(idTable);
		if (!list.isEmpty()) {
			for (OrderTable orderTable : list) {
				txtFullName.setText(orderTable.getName());
				txtPhone.setText(orderTable.getPhone());
				String datetime = orderTable.getDateCheckin();
				LocalDateTime localDateTime = LocalDateTime.parse(datetime);
				datePicker.setValue(localDateTime.toLocalDate());
				ChangeFormatDatePicker(datePicker);
				timePicker.setValue(localDateTime.toLocalTime());
				// Xu li thoi gian dat
				String datetimeOrder = orderTable.getTimeOrder();
				LocalDateTime localDateTimeOrder = LocalDateTime.parse(datetimeOrder);
				datePickerOrder.setValue(localDateTimeOrder.toLocalDate());
				ChangeFormatDatePicker(datePickerOrder);
				timePickerOrder.setValue(localDateTimeOrder.toLocalTime());
			}
		} else {
			reset();
		}
	}

	public void reset() {
		txtFullName.setText("");
		txtPhone.setText("");
		datePicker.setValue(LocalDate.now());
		timePicker.setValue(LocalTime.now());
		datePickerOrder.setValue(LocalDate.now());
		timePickerOrder.setValue(LocalTime.now());
		ChangeFormatDatePicker(datePicker);
		ChangeFormatDatePicker(datePickerOrder);
	}

	@FXML
	void ClickOrderTable(ActionEvent event) {
		String nameCustomer = txtFullName.getText();
		String phone = txtPhone.getText();
		String checkin = datePicker.getValue() + "T" + timePicker.getValue();
		int idTable = idTableClick;
		LocalTime lcTimeOrder = timePickerOrder.getValue().plusMinutes(60);
		if (nameCustomer.equalsIgnoreCase("")) {
			String error = "Tên khách hàng không để trống!";
			showAlertWithoutHeaderText(error);
		} else if (phone.equalsIgnoreCase("")) {
			String error = "Số điện thoại không để trống!";
			showAlertWithoutHeaderText(error);
		} else if (!CheckPhone(txtPhone.getText())) {
			String error = "Số điện thoại không hợp lệ!\nVui lòng kiểm tra lại đầu số mới!";
			showAlertWithoutHeaderText(error);
		} else if(txtStatus.getText().equalsIgnoreCase("Có người")) {
			String error = "Bàn đang có người!\nVui lòng chọn bàn khác!";
			showAlertWithoutHeaderText(error);
		}else if(txtStatus.getText().equalsIgnoreCase("Đã đặt")) {
			String error = "Bàn đã được đặt!\nVui lòng chọn bàn khác!";
			showAlertWithoutHeaderText(error);
		}else if ((datePicker.getValue().isEqual(datePickerOrder.getValue()))
				&& (timePicker.getValue().isAfter(timePickerOrder.getValue()))) {
			if (timePicker.getValue().compareTo(lcTimeOrder) == 0 || timePicker.getValue().compareTo(lcTimeOrder) < 0) {
				HomeOrderTableDAO.InserOrderTable(idTable, CheckDAO.chuanHoaDanhTuRieng(nameCustomer), phone, checkin);
				ShowNotificationCheck();
				LoadTable();
			} else {
				String error = "Thời gian đặt trong khoảng 1 giờ so với thời gian hiện tại!\n Vui lòng đặt lại thời gian!";
				showAlertWithoutHeaderText(error);
			}
		} else {
			String error = "Thời gian đặt không hợp lệ! Vui lòng kiểm tra lại!";
			showAlertWithoutHeaderText(error);
			ShowNotificationError();
		}
	}

	@FXML
	void CickDeleteOrderTable(ActionEvent event) {
		int idTable = idTableClick;
		String status = HomeOrderTableDAO.GetStatusTable(idTable);
		if (status.equalsIgnoreCase("Đã đặt")) {
			HomeOrderTableDAO.DeleteOrderTable(idTable);
			String error = "Xóa đặt bàn thành công!";
			showAlertWithoutHeaderText(error);
			LoadTable();
			reset();
		} else if (status.equalsIgnoreCase("Trống")) {
			String error = "Bàn hiện tại trống! Vui lòng chọn bàn khác!";
			showAlertWithoutHeaderText(error);
		} else {
			String error = "Bàn hiện tại có người! Vui lòng chọn bàn khác!";
			showAlertWithoutHeaderText(error);
		}
	}

	@FXML
	void ClickBackOrderFood(ActionEvent event) {
		String linkScene = "/View/HomeStaff.fxml";
		ChangeScene(event, linkScene);
	}

	@FXML
	void ClickIntroSE(ActionEvent event) throws IOException {
		String title = "Introduce Software";
		String linkScene = "/View/IntroSoftware.fxml";
		ShowNewWindow(event, title, linkScene);
	}

	public void ClickAccountInformation(ActionEvent event) throws IOException {
		String title = "My Information";
		String linkScene = "/View/AccountInformation.fxml";
		ShowNewWindow(event, title, linkScene);
	}

	private void LoadDisplayName() {
		lbDisplayName.setText(LoginDAO.acc.getDisplayname());
		lbType.setText(LoginDAO.acc.getTyperight());

	}

	private void ChangeFormatDatePicker(JFXDatePicker datePicker) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}
	
	private void showAlertWithoutHeaderText(String error) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notification");
		alert.setHeaderText(null);
		alert.setContentText(error);
		alert.showAndWait();
	}

	private void ChangeScene(ActionEvent event, String link) {
		try {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(link));
			Parent homestaff = loader.load();
			Scene scene = new Scene(homestaff);
			stage.setScene(scene);
			scene.getStylesheets().add("View/application.css");
			stage.setX(0);
			stage.setY(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ShowNotificationCheck() {
		String link = "/images/icon_check.png";
		String title = "Notification!";
		String text = "Order Table Success!!";
		Notification(title, text, link);
	}

	private void ShowNotificationError() {
		String link = "/images/icon_error.png";
		String title = "Notification!";
		String text = "Order Table Error!!";
		Notification(title, text, link);
	}

	private void Notification(String title, String text, String link) {
		Image image = new Image(link);
		Notifications notifications = Notifications.create().title(title).text(text).graphic(new ImageView(image))
				.hideAfter(Duration.millis(2 * 1000)).position(Pos.BOTTOM_LEFT).onAction((ect) -> {

				});
		notifications.darkStyle();
		notifications.show();
	}

	private boolean CheckPhone(String phone) {
		boolean result = false;
		String VIETTEL_PATTERN = "^(03[2|3|4|5|6|7|8|9])+([0-9]{7})$";
		String MOBILE_PATTERN = "^(07[0|6|7|8|9])+([0-9]{7})$";
		String VINAPHONE_PATTERN = "^(08[1|2|3|4|5])+([0-9]{7})$";
		String VIETNAMOBILE_PATTERN = "^(05[6|8])+([2|3|4|5|6|7|8|9])+([0-9]{6})$";
		String GMOBILE_PATTERN = "^(059[2|3|8|9])+([0-9]{6})$";

		Pattern patternViettel = Pattern.compile(VIETTEL_PATTERN);
		Pattern patternMobile = Pattern.compile(MOBILE_PATTERN);
		Pattern patternVinaphone = Pattern.compile(VINAPHONE_PATTERN);
		Pattern patternVietnamobile = Pattern.compile(VIETNAMOBILE_PATTERN);
		Pattern patternGmobile = Pattern.compile(GMOBILE_PATTERN);

		Matcher matcherViettel = patternViettel.matcher(phone);
		Matcher matcherMobile = patternMobile.matcher(phone);
		Matcher matcherVinaphone = patternVinaphone.matcher(phone);
		Matcher matcherVietnamobile = patternVietnamobile.matcher(phone);
		Matcher matcherGmobile = patternGmobile.matcher(phone);

		if (result == false) {
			result = matcherViettel.matches();
		}
		if (result == false) {
			result = matcherMobile.matches();
		}
		if (result == false) {
			result = matcherVinaphone.matches();
		}
		if (result == false) {
			result = matcherVietnamobile.matches();
		}
		if (result == false) {
			result = matcherGmobile.matches();
		}
		return result;
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
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}

	}
}
