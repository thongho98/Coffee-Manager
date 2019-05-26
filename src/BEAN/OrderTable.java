package BEAN;

public class OrderTable {
	private int id;
	private String name;
	private String phone;
	private String dateCheckin;
	private String timeOrder;
	private int idTable;

	public OrderTable() {
	}

	public OrderTable(int id, String name, String phone, String dateCheckin, String timeOrder, int idTable) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.dateCheckin = dateCheckin;
		this.timeOrder = timeOrder;
		this.idTable = idTable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDateCheckin() {
		return dateCheckin;
	}

	public void setDateCheckin(String dateCheckin) {
		this.dateCheckin = dateCheckin;
	}

	public String getTimeOrder() {
		return timeOrder;
	}

	public void setTimeOrder(String timeOrder) {
		this.timeOrder = timeOrder;
	}

	public int getIdTable() {
		return idTable;
	}

	public void setIdTable(int idTable) {
		this.idTable = idTable;
	}

}
