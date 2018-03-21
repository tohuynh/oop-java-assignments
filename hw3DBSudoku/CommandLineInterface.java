// CommandLineInterface.java
/*
 Provided class that presents a command-line interface for the DB classes.
 It does simple i/o with the console and sends messages to the DB
 to do all the work. All the code is in main().
 Edit the code so it interfaces with the DB classes.
*/

import java.io.*;

public class CommandLineInterface {
	public static void main(String[] args) {

		DBTable db = new DBTable();	// DBTable to use for everything
		
		try {
			// Create reader for typed input on console
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line;
			
			while (true) {
				int length = db.getNumberOfRecords();
				int selectedLength = db.getNumberOfSelectedRecords();
				// YOUR CODE HERE
				System.out.println("\n" + length + " records (" + selectedLength + " selected)");
				System.out.println("r read, p print, sa select and, so select or, da ds du delete, c clear sel");
				System.out.print("db:");
				line = reader.readLine().toLowerCase();
				
				if (line.equals("r")) {
					System.out.println("read");
					String fname;
					System.out.print("Filename:");
					fname = reader.readLine();
					// YOUR CODE HERE
					try {
						db.addRecordsFromFile(fname);
					} catch (FileNotFoundException e) {
						System.out.println("sorry, file not found");
					}
				}
				else if (line.equals("p")) {
					System.out.println("print");
					// YOUR CODE HERE
					System.out.println(db.toString());
				}
				else if (line.equals("da")) {
				System.out.println("delete all");
					// YOUR CODE HERE
					db.deleteRecords();
				}
				else if (line.equals("ds")) {
					System.out.println("delete selected");
					// YOUR CODE HERE
					db.deleteRecords(true);
				}
				else if (line.equals("du")) {
					System.out.println("delete unselected");
					// YOUR CODE HERE
					db.deleteRecords(false);
				}
				else if (line.equals("c")) {
					System.out.println("clear selection");
					/// YOUR CODE HERE
					db.unSelectRecords();
				}
				else if (line.equals("so") || line.equals("sa")) {
					DBRecord.selectMode mode;
					if (line.equals("so")) { 
						System.out.println("select or");
						mode = DBRecord.selectMode.OR;
					} else {
						System.out.println("select and");
						mode = DBRecord.selectMode.AND;
					}
					
					System.out.print("Criteria record:");
					String text = reader.readLine();  // get text line from user
					db.selectRecords(text, mode);
					// YOUR CODE HERE
				}
				else if (line.equals("q") || line.equals("quit")) {
					System.out.println("quit");
					break;
				}
				else {
					System.out.println("sorry, don't know that command");
				}
			}
		}
		catch (IOException e) {
			System.err.println(e);
		}
	}
}
