import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

public class ChunkListTest extends TestCase{

	public void testBasic() {
		Collection<String> c = new ChunkList<String>();
		c.add("a");
		assertEquals(1, c.size());
		
		Iterator<String> it = c.iterator();
		assertEquals("a", it.next());
		it.remove();
		assertEquals(0, c.size());
		
		c.add("a");
		c.add("b");
		c.add("c");
		c.add("d");
		
		it = c.iterator();
		while(it.hasNext()) {
			String e = it.next();
			if (e.equals("a")) {
				it.remove();
				assertEquals(3, c.size());
				Collection<String> after = new ChunkList<String>();
				after.add("b");
				after.add("c");
				after.add("d");
				assertTrue(ChunkListSuperTest.sameCollection(c, after));
			} else if (e.equals("c")) {
				it.remove();
				assertEquals(2, c.size());
				Collection<String> after = new ChunkList<String>();
				after.add("b");
				after.add("d");
				assertTrue(ChunkListSuperTest.sameCollection(c, after));
			} else if (e.equals("d")) {
				it.remove();
				assertEquals(1, c.size());
				Collection<String> after = new ChunkList<String>();
				after.add("b");
				assertTrue(ChunkListSuperTest.sameCollection(c, after));
			}
		}
		
	}

}
