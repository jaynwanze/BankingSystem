package ca3;

import java.util.UUID;

public class Customer {
	/**
	 * A description of new data type called customer.
	 * 
	 */

	// 1. Data about customer - Instance variables
	private String ppsNumber;
	private String customerFirstName;
	private String customerLastName;
	private String dateOfBirth;
	private String customerID;
	private String customerPassword;

	// 2. First method - Constructor - ALWAYS REQUIRED - create a blank object
	public Customer() {
		this.ppsNumber = "";
		this.customerFirstName = ""; // refering to an instance variable
		this.customerLastName = "";
		this.dateOfBirth = "";
		this.customerPassword = "";

	}

	// Constructor 2 - create a new customer object with data
	public Customer(String ppsNum, String firstName, String lastName, String dob) {
		this.ppsNumber = ppsNum;
		this.customerFirstName = firstName;
		this.customerLastName = lastName;
		this.dateOfBirth = dob;
		this.customerID = String.valueOf(UUID.randomUUID()).substring(24, 36);
		this.customerPassword = String.valueOf(UUID.randomUUID()).substring(0, 8);
		;

	}

	// returns customer pps number
	public String getPPSNumber() {
		return this.ppsNumber;
	}

	// return the customer's first name
	public String getCustomerFirstName() {
		return this.customerFirstName;
	}
	// return the customer's last name

	public String getCustomerLastName() {
		return this.customerLastName;
	}

	// return the customer's date of birth

	public String getDateOfBirth() {
		return this.dateOfBirth;

	}
	// return the customer's ID

	public String getCustomerID() {
		return this.customerID;
	}

	// return the customer's password
	public String getPassword() {
		return this.customerPassword;
	}

	// update the customer number to a new pps number
	public void setPPSNumber(String ppsNum) {
		this.ppsNumber = ppsNum;
	}

	// update the student's number to a new first name
	public void setCustomerFirstName(String firstName) {
		this.customerFirstName = firstName;
	}

	// update the student's number to a new last name
	public void setCustomerLastName(String lastName) {
		this.customerLastName = lastName;
	}

	// update the customers date of birth
	public void setDateOfBirth(String dob) {
		this.dateOfBirth = dob;
	}

	// update the customers password
	public void setPassword(String pass) {
		this.customerPassword = pass;
	}

	// Method/module to display all student details
	public String toString() {
		return "Customer PPS number: " + this.ppsNumber + "\n" + "Customer first name: " + this.customerFirstName + "\n"
				+ "Customer last name: " + this.customerLastName + "\n" + "Customer date of birth: " + this.dateOfBirth
				+ "\n" + "Customer ID: " + this.customerID;

	}

}
