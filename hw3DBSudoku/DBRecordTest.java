import junit.framework.TestCase;

public class DBRecordTest extends TestCase{
	public void testConstructor() {
		DBRecord r = new DBRecord("abc:def, ghi:jkl");
		assertEquals("abc:def, ghi:jkl", r.toString());
	}
	public void testMatches() {
		DBRecord b = new DBRecord("ab:cd, ef:gh");
		//empty criteria record
		DBRecord ecr = new DBRecord("");
		DBRecord b3 = new DBRecord("ab:cd");
		DBRecord b4 = new DBRecord("ab:cd, aa:bb");
		assertTrue(b.matches(ecr, DBRecord.selectMode.AND));
		assertTrue(b.matches(b3, DBRecord.selectMode.AND));
		assertFalse(b.matches(b4, DBRecord.selectMode.AND));
		assertFalse(b.matches(ecr, DBRecord.selectMode.OR));
		assertTrue(b.matches(b3, DBRecord.selectMode.OR));
		
	}

}
