import java.io.FileNotFoundException;

import junit.framework.TestCase;

public class DBTableTest extends TestCase{
	public void testSelections() throws FileNotFoundException {
		DBTable db = new DBTable();
		assertEquals(0, db.getNumberOfRecords());
		assertEquals(0, db.getNumberOfSelectedRecords());
		
		db.addRecordsFromFile("movies.txt");
		assertEquals(5, db.getNumberOfRecords());
		assertEquals(0, db.getNumberOfSelectedRecords());
		
		db.selectRecords("stars:kotto, stars:stanton", DBRecord.selectMode.AND);
		assertEquals(5, db.getNumberOfRecords());
		assertEquals(1, db.getNumberOfSelectedRecords());
		db.unSelectRecords();
		assertEquals(5, db.getNumberOfRecords());
		assertEquals(0, db.getNumberOfSelectedRecords());
		
		db.selectRecords("stars:kotto, stars:stanton", DBRecord.selectMode.OR);
		assertEquals(5, db.getNumberOfRecords());
		assertEquals(3, db.getNumberOfSelectedRecords());
		
		db.deleteRecords(true);
		assertEquals(2, db.getNumberOfRecords());
		assertEquals(0, db.getNumberOfSelectedRecords());
		
		db.deleteRecords();
		assertEquals(0, db.getNumberOfRecords());
		assertEquals(0, db.getNumberOfSelectedRecords());
		
		db.addRecordsFromFile("movies.txt");
		assertEquals(5, db.getNumberOfRecords());
		assertEquals(0, db.getNumberOfSelectedRecords());
		
		db.selectRecords("stars:kotto, stars:stanton", DBRecord.selectMode.AND);
		assertEquals(5, db.getNumberOfRecords());
		assertEquals(1, db.getNumberOfSelectedRecords());
		
		db.deleteRecords(false);
		assertEquals(1, db.getNumberOfRecords());
		assertEquals(1, db.getNumberOfSelectedRecords());
		
		assertEquals("*name:Alien, stars:Yaphet Kotto, stars:Sigourney Weaver, stars:Harry Dean Stanton", db.toString());
		
	}

}
