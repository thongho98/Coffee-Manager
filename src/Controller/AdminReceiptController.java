package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;

import BEAN.BillByDate;
import BEAN.ReceipMonthOfYear;
import DAO.AdminReceiptDAO;
import DB.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AdminReceiptController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXDatePicker dateCheckin;

	@FXML
	private Button btnReceipt;

	@FXML
	private JFXDatePicker dateCheckout;

	@FXML
	private TableView<BillByDate> tableReceipt;
	@FXML
	private TableColumn<BillByDate, Integer> idReceipt;
	@FXML
	private TableColumn<BillByDate, String> nameTable;
	@FXML
	private TableColumn<BillByDate, String> dateCheckIn;
	@FXML
	private TableColumn<BillByDate, String> dateCheckOut;
	@FXML
	private TableColumn<BillByDate, Integer> totalPrice;
	@FXML
	private Label lbReceipt;
	@FXML
	private Label lbTotalPrice;
	@FXML
	private LineChart<String, Number> lineChart;

//	@FXML
//	private JFXComboBox<String> cbYear;
	private ObservableList<BillByDate> menuList;
	XYChart.Series<String, Number> series;

	@FXML
	void initialize() {

	}

	@FXML
	void ClickReceipt(ActionEvent event) {
		LocalDate lcdateCheckInInput = dateCheckin.getValue();
		LocalDate lcdateCheckOutInput = dateCheckout.getValue();
		if (lcdateCheckInInput.isAfter(lcdateCheckOutInput)) {
			String error = "Ngày sau phải lớn hơn ngày trước";
			showAlertWithoutHeaderText(error);
		} else {
			String dateCheckInInput = "'" + lcdateCheckInInput + "'";
			String dateCheckOutInput = "'" + lcdateCheckOutInput + "'";
			ShowBillReceipt(dateCheckInInput, dateCheckOutInput);
		}
	}

	@FXML
	void ClickHome(ActionEvent event) {
		String link = "/View/HomeAdmin.fxml";
		ChangeScene(event, link);
	}

	private void ShowBillReceipt(String dateCheckInInput, String dateCheckOutInput) {
		int total = 0;
		int totalBill = 0;
		DecimalFormat formatter = new DecimalFormat("###,###,###");
		Connection cnn = MyConnection.getConnection();
		List<BillByDate> list = AdminReceiptDAO.GetListBillByDate(cnn, dateCheckInInput, dateCheckOutInput);
		totalBill = AdminReceiptDAO.GetCountBillByDate(cnn, dateCheckInInput, dateCheckOutInput);
		idReceipt.setCellValueFactory(new PropertyValueFactory<BillByDate, Integer>("id"));
		nameTable.setCellValueFactory(new PropertyValueFactory<BillByDate, String>("nameTable"));
		dateCheckIn.setCellValueFactory(new PropertyValueFactory<BillByDate, String>("DateCheckIn"));
		dateCheckOut.setCellValueFactory(new PropertyValueFactory<BillByDate, String>("DateCheckOut"));
		totalPrice.setCellValueFactory(new PropertyValueFactory<BillByDate, Integer>("totalPrice"));
		menuList = FXCollections.observableArrayList();
		if (!list.isEmpty()) {
			for (BillByDate billByDate : list) {
				menuList.add(new BillByDate(billByDate.getId(), billByDate.getNameTable(), billByDate.getDateCheckIn(),
						billByDate.getDateCheckOut(), billByDate.getTotalPrice()));
				total += billByDate.getTotalPrice();
				lbTotalPrice.setText(String.valueOf(formatter.format(total))+ " VNĐ");
				lbReceipt.setText(String.valueOf(totalBill));
				tableReceipt.setItems(menuList);
			}
			list.clear();
		} else {
			tableReceipt.getItems().clear();
		}
	}

	@SuppressWarnings("unchecked")
	private void LoadLineChart(String year) {
		List<ReceipMonthOfYear> list = AdminReceiptDAO.GetReceiptMonthOfYear(year);
		if (!list.isEmpty()) {
			series = new XYChart.Series<>();
			series.setName("Doanh thu năm " + year);
			for (ReceipMonthOfYear receipMonthOfYear : list) {
				XYChart.Data<String, Number> month = new XYChart.Data<>("Tháng " + receipMonthOfYear.getMonth(),
						receipMonthOfYear.getTotalprice());
				series.getData().addAll(month);
			}
			lineChart.getData().add(series);
		} else {
			series.getData().clear();
		}
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		cbYear.getItems().addAll("2018", "2019", "2020", "2021");
//		LoadComboYear();
		LoadLineChart("2019");
		ChangeFormatDatePicker(dateCheckin);
		ChangeFormatDatePicker(dateCheckout);
	}
}