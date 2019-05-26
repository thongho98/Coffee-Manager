package BEAN;

public class ReceipMonthOfYear {
	int month;
	int totalprice;

	public ReceipMonthOfYear() {

	}

	public ReceipMonthOfYear(int month, int totalprice) {
		super();
		this.month = month;
		this.totalprice = totalprice;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(int totalprice) {
		this.totalprice = totalprice;
	}

}
