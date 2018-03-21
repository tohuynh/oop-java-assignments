// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	public static final int INITIAL_BALANCE = 1000;
	private int id;
	private int balance;
	private int transactions;
	
	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private Bank bank;  
	public Account(Bank bank, int id) {
		this(bank, id, INITIAL_BALANCE);
	}
	
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}
	
	public synchronized int getBalance() {
		return balance;
	}
	
	public synchronized int changeBalance(int amount) {
		transactions++;
		balance += amount;
		return balance;
	}
	
	public synchronized int getNumberOfTransactions() {
		return transactions;
	}
	
	public Bank getBank() {
		return bank;
	}
	
	public String toString() {
		return "acct:" + id + " bal:" + balance + " trans:" + transactions;
	}
	
}
