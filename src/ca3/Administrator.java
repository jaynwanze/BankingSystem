package ca3;

public class Administrator {

	private String adminPassword;

	// 2. First method - Constructor - ALWAYS REQUIRED - create a blank object
	public Administrator() {

		this.adminPassword = "";

	}

	// Constructor 2 - create a new customer object with data
	public Administrator(String pass) {

		this.adminPassword = pass;

	}

	public String getPassword() {
		return this.adminPassword;
	}

	// update the customers password
	public void setPassword(String pass) {
		this.adminPassword = pass;
	}
}
