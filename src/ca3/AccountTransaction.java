package ca3;

public class AccountTransaction {

	// 1. Data about account transaction - Instance variables
	private String transactionDate;
	private String transactionType;
	private double transactionAmount;
	private double culmativeBalance;

	// 2. First method - Constructor - ALWAYS REQUIRED - create a blank object
	public AccountTransaction() {
		this.transactionDate = ""; // refering to an instance variable
		this.transactionType = "";
		this.transactionAmount = 0.00;

	}

	// Constructor 2 - create a new account transaction object with data
	public AccountTransaction(String date, String type, double amount, double bal) {
		this.transactionDate = date;
		this.transactionType = type;
		this.transactionAmount = amount;
		this.culmativeBalance = bal;

	}

	// return the customers transaction date
	public String getTransactionDate() {
		return this.transactionDate;
	}

	// return the customers transaction type
	public String getTransactionType() {
		return this.transactionType;
	}

	// return the customers transaction amount
	public double getTransactionAmount() {
		return this.transactionAmount;
	}

	// update the customers transaction date
	public void setTransactionDate(String date) {
		this.transactionDate = date;
	}

	// update the customers transaction type
	public void setTransactionType(String type) {
		this.transactionType = type;
	}

	// update the customers transaction amount
	public void setTransactionAmount(double amount) {
		this.transactionAmount = amount;
	}

	// Method/module to display all account transaction details
	public String toString() {
		return "Transaction date: " + this.transactionDate + "\n" + "Transaction type - " + this.transactionType + "\n"
				+ "Transaction amount - " + this.transactionAmount + "\n" + "Culmative balance - "
				+ this.culmativeBalance + "\n\n";

	}
}
