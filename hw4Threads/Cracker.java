// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private static byte[] target;
	private static int maxLength;
	private static boolean print;
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	public static void search(int numWorkers) {
		int segmentLength = CHARS.length/numWorkers;
		Thread[] workers = new Thread[numWorkers];
		for(int i = 0; i < numWorkers; i++) {
			int endIndex = (i < numWorkers - 1 ? (i+1) * segmentLength: CHARS.length);
			workers[i] = new Thread(new Worker(i*segmentLength, endIndex));
			workers[i].start();
		}
		
		try {
			for(int i = 0; i < numWorkers; i++) {
				workers[i].join();
			}
		} catch (InterruptedException ignored) {
		}

		System.out.println("all done");

	}
	
	
	public static class Worker implements Runnable {
		private MessageDigest md;
		private int startIndex;
		private int endIndex;
		
		public Worker(int startIndex, int endIndex) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			
			try {
				md = MessageDigest.getInstance("SHA");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
		}

		@Override
		public void run() {
			for(int i = startIndex; i < endIndex; i++ ) {
				searchHelper(Character.toString(CHARS[i]));
			}
			
		}
		
		private void searchHelper(String str) {
			if(str.length() <=maxLength) {
				md.reset();
				md.update(str.getBytes());
				byte[] bytes = md.digest();
				if(print) {
					System.out.println(str + " " + hexToString(bytes));
				} else if(MessageDigest.isEqual(bytes, target))  {
					System.out.println("match:" + str + " " + hexToString(bytes));
				}
			}
			
			if (str.length() < maxLength) {
				for(int i = 0; i < CHARS.length; i++)
					searchHelper(str + CHARS[i]);
			}
			
		}

	}

	
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Args: target length [workers]");
			System.exit(1);
		}
		String targ = args[0];
		maxLength = Integer.parseInt(args[1]);
		int numWorkers = 1;
		if (args.length>2) {
			numWorkers = Integer.parseInt(args[2]);
		}
		
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
		// 2k7! c5e478e7da53b70f0fabcdefa082e1d1c5a2bc6d
		
		print = targ.equalsIgnoreCase("print");
		target = (print ? null : hexToArray(targ));
		
		// YOUR CODE HERE
		search(numWorkers);
	}
}
