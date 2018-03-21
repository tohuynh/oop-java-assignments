import junit.framework.TestCase;

public class BankTest extends TestCase {
	public void test5k() {
		Bank bank = new Bank(0);
		bank.processFile("5k.txt", 4);
		Account[] accounts = bank.getAccounts();
		for (int i = 0; i < Bank.ACCOUNTS; i++) {
			assertEquals(Account.INITIAL_BALANCE, accounts[i].getBalance());
		}
	}
	
	public void test100k() {
		Bank bank = new Bank();
		bank.processFile("100k.txt", 256);
		Account[] accounts = bank.getAccounts();
		for (int i = 0; i < Bank.ACCOUNTS; i++) {
			assertEquals(Account.INITIAL_BALANCE, accounts[i].getBalance());
		}
	}

}
