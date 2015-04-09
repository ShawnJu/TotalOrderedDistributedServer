package main;

public class Account {
	private int accountID;
	private int balance = 0;
	private String firstname;
	private String lastname;
	private String address;

	public Account (String firstname, String lastname, String address) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
	}

	public synchronized boolean deposit (int value) {	
		this.balance += value;
		return true;
	}

	public synchronized boolean withdraw (int value) {
		if (value > this.balance)
			return false;
		this.balance -= value;
		return true;
	}
	
	public int getBalance() { return this.balance; }

	public int getAccountID() { return this.accountID; }
	
	public String getFirstname() { return this.firstname; }
	
	public String getLastname() { return this.lastname; }

	public String getAddress() { return this.address; }
}
