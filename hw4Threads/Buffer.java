// Buffer.java

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/*
 Holds the transactions for the worker
 threads.
*/
public class Buffer {
	public static final int SIZE = 64;
	private List<Transaction> transactions;
	private Semaphore canAdd;
	private Semaphore canRemove;
	private Object changeTransactionsLock;
	
	public Buffer() {
		transactions = new ArrayList<Transaction>();
		canAdd = new Semaphore(SIZE);
		canRemove = new Semaphore(0);
		changeTransactionsLock = new Object();
	}
	
	// YOUR CODE HERE
	
	public void add(Transaction t) {
		//call transactions.add(t)
		//if transactions.size() > SIZE, block until space is available
		
		//use canAdd can Remove in Semaphores
		
		try {
			canAdd.acquire();
		} catch (InterruptedException ignored) {	
		}
		
		synchronized(changeTransactionsLock) {
			transactions.add(t);
		}
		canRemove.release();
		
	}
	
	public Transaction remove() {
		//call transactions.remove(0);
		//if empty, block until element is available, wake up and return element
		
		try {
			canRemove.acquire();
		} catch (InterruptedException ignored) {	
		}
		Transaction t;
		synchronized(changeTransactionsLock) {
			t = transactions.remove(0);
		}
		canAdd.release();
		
		return t;
	}
}
