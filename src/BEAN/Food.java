package BEAN;

public class Food {
	private int id;
	private String nameFood;
	private int price;
	private int idCategory;
	private String nameCategory;
	public Food() {
		
	}
	
	public Food(int id, String nameFood, int price, int idCategory,String nameCategory) {
		super();
		this.id = id;
		this.nameFood = nameFood;
		this.price = price;
		this.idCategory = idCategory;
		this.nameCategory = nameCategory;
	}

	public String getNameCategory() {
		return nameCategory;
	}

	public void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNameFood() {
		return nameFood;
	}
	public void setNameFood(String nameFood) {
		this.nameFood = nameFood;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getIdCategory() {
		return idCategory;
	}
	public void setIdCategory(int idCategory) {
		this.idCategory = idCategory;
	}
	
}
