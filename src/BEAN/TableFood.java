package BEAN;

public class TableFood {
	private int id;
	private String name;
	private String idStatus;

	public TableFood() {
	}

	public TableFood(int id, String name, String idStatus) {
		super();
		this.id = id;
		this.name = name;
		this.idStatus = idStatus;
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

	public String getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(String idStatus) {
		this.idStatus = idStatus;
	}

}
