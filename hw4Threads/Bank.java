// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts
	private Account[] accounts;
	private Buffer buffer;
	private int limit;
	private boolean trackBadTrans = false;
	private List<String> badTrans;
	private Object addBadTransLock;
	
	public Bank() {
		buffer = new Buffer();
		accounts = new Account[ACCOUNTS];
	    for(int i = 0; i < ACCOUNTS; i++) {
	    	accounts[i] = new Account(this, i);
		}
	}
	
	public Bank(int limit) {
		this();
		this.limit = limit;
		trackBadTrans = true;
		badTrans = new ArrayList<String>();
		addBadTransLock = new Object();
	}
	
	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount
				
				// YOUR CODE HERE
				buffer.add(new Transaction(from, to, amount));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) {
		Thread[] workers = new Thread[numWorkers];
		for(int i = 0; i < numWorkers; i++) {
			workers[i] = new Thread(new Worker());
			workers[i].start();
		}

		readFile(file);
		for(int i = 0; i < numWorkers; i++) {
			buffer.add(null);
		}

		try {
			for(int i = 0; i < numWorkers; i++) {
				workers[i].join();
			}
		} catch (InterruptedException ignored) {
		}
	}
	
	public void transactionsSummary() {
		for(int i = 0; i < ACCOUNTS; i++) {
			System.out.println(accounts[i].toString());
		}

		if(trackBadTrans) {
			System.out.println("Bad transactions...");
			for(int i = 0; i < badTrans.size(); i++)
				System.out.println(badTrans.get(i));
		}
	}
	
	public void addBad(int from, int to, int amount, int badBalance) {
		if (trackBadTrans) {
			synchronized(addBadTransLock) {
				badTrans.add("from:" + from + " to:" + to + " amt:" + amount + " bal:" + badBalance);
			}
		}
	}
	
	public Account[] getAccounts() {
		return accounts;
	}

	
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			System.exit(1);
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}
		int limit = 0;
		if (args.length >= 3) {
			limit = Integer.parseInt(args[2]);
		}
		
		// YOUR CODE HERE
		long start = System.currentTimeMillis();
		Bank bank;
		if (args.length == 3) {
			bank = new Bank(limit);
		} else {
			bank = new Bank();
		}
		bank.processFile(file, numWorkers);
		long end = System.currentTimeMillis();
		bank.transactionsSummary();

		System.out.println("Elapsed time: " + (end - start) + "ms");
	}
	
	public class Worker implements Runnable{

		@Override
		public void run() {
			Transaction t;
			while ((t = buffer.remove()) != null) {
				int newBalance = accounts[t.from].changeBalance(-t.amount);
				Bank bank = accounts[t.from].getBank();
				if (bank.trackBadTrans && (newBalance < bank.limit)) {
					bank.addBad(t.from, t.to, t.amount, newBalance);
				}
				accounts[t.to].changeBalance(t.amount);
				
			}
			
		}
		//loop
		//try to get trans
		//withdraw from deposit to
		//if null exit loop
	}
}

