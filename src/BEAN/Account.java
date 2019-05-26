package BEAN;

//Load danh sach account len table!!
public class Account {
	private int id;
	private String displayname;
	private String username;
	private String password;
	private String typeright;
	
	public Account() {
		
	}
	public Account(int id, String displayname, String username, String password, String typeright) {
		super();
		this.id = id;
		this.displayname = displayname;
		this.username = username;
		this.password = password;
		this.typeright = typeright;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTyperight() {
		return typeright;
	}
	public void setTyperight(String typeright) {
		this.typeright = typeright;
	}
	
}
