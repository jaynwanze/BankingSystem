package ca3;

import java.util.UUID;

public class CustomerAccount {
	/**
	 * A description of new data type called Customer Account.
	 * 
	 */

	// 1. Data about student - Instance variables
	private String accountNumber;
	private double accountBalance;

	// 2. First method - Constructor - ALWAYS REQUIRED - create a blank object
	public CustomerAccount() {
		this.accountNumber = ""; // Referring to an instance variable
		this.accountBalance = 0.00;

	}

	// Constructor 2 - create a new customer account object with data
	public CustomerAccount(double balance) {
		this.accountNumber = String.valueOf(UUID.randomUUID()).substring(9, 18);
		this.accountBalance = balance;

	}

	// return the customer account number
	public String getAccountNumber() {
		return this.accountNumber;
	}

	// return the customer account balance
	public double getAccountBalance() {
		return this.accountBalance;
	}

	// update the customer account number to a new number
	public void setAccountNumber(String number) {
		this.accountNumber = number;
	}

	// update the customer accounts balance to a new balance
	public void setAccountBalance(double balance) {
		this.accountBalance = balance;
	}

	// update the customer accounts balance to a new balance from a withdrawal
	public double withdrawal(double amount) {
		double balance;

		balance = this.accountBalance - amount;

		if (balance < 0) {
			return -1;
		} else {
			this.accountBalance = balance;
			return this.accountBalance;
		}

	}

	// update the customer accounts balance to a new balance from a lodgement
	public double lodgement(double amount) {
		double balance;

		balance = this.accountBalance + amount;

		if (balance < 0) {
			return -1;
		} else {
			this.accountBalance = balance;
			return this.accountBalance;
		}

	}

	// Method/module to display all customer account details
	public String toString() {
		return "Customer Account number: " + this.accountNumber + "\n" + "Customer Account balance: "
				+ this.accountBalance + "\n";

	}
}
