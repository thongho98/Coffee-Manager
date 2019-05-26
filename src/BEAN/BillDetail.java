package BEAN;

public class BillDetail {
	int id;
	int idBill;
	int idFood;
	int quantity;

	public BillDetail() {

	}

	public BillDetail(int id, int idBill, int idFood, int quantity) {
		super();
		this.id = id;
		this.idBill = idBill;
		this.idFood = idFood;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdBill() {
		return idBill;
	}

	public void setIdBill(int idBill) {
		this.idBill = idBill;
	}

	public int getIdFood() {
		return idFood;
	}

	public void setIdFood(int idFood) {
		this.idFood = idFood;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
