import junit.framework.TestCase;

public class DBBindingTest extends TestCase{
	public void testConstructor() {
		DBBinding b = new DBBinding(" ab 1 ab : cd 2 cd  ");
		assertEquals("ab 1 ab", b.getKey());
		assertEquals("cd 2 cd", b.getValue());
		assertEquals("ab 1 ab:cd 2 cd", b.toString());
	}
	
	public void testEquals() {
		DBBinding criterion = new DBBinding("ab:cd");
		DBBinding b = new DBBinding("ab:cddd124    24efefefef");
		assertTrue(criterion.equals(b));
		
		DBBinding b1 = new DBBinding("ab:cddd124    24efefefef");
		DBBinding b2 = new DBBinding("cd:cddd124    24efefefef");
		assertFalse(b1.equals(b2));
		assertTrue(b1.equals(b1));
		
		DBBinding b3 = new DBBinding("ab:CD");
		assertTrue(criterion.equals(b3));
	}

}
