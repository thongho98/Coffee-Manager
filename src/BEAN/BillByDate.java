package BEAN;

public class BillByDate {
	int id;
	String nameTable;
	String dateCheckIn;
	String dateCheckOut;
	int totalPrice;

	public BillByDate() {
	}

	public BillByDate(int id, String nameTable, String dateCheckIn, String dateCheckOut, int totalPrice) {
		super();
		this.id = id;
		this.nameTable = nameTable;
		this.dateCheckIn = dateCheckIn;
		this.dateCheckOut = dateCheckOut;
		this.totalPrice = totalPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNameTable() {
		return nameTable;
	}

	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}

	public String getDateCheckIn() {
		return dateCheckIn;
	}

	public void setDateCheckIn(String dateCheckIn) {
		this.dateCheckIn = dateCheckIn;
	}

	public String getDateCheckOut() {
		return dateCheckOut;
	}

	public void setDateCheckOut(String dateCheckOut) {
		this.dateCheckOut = dateCheckOut;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
