package BEAN;

public class FoodCategory {
	private int id;
	private String nameCategory;
	public FoodCategory() {}
	
	public FoodCategory(int id, String nameCategory) {
		super();
		this.id = id;
		this.nameCategory = nameCategory;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNameCategory() {
		return nameCategory;
	}
	public void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}
	
	
	
	
}
