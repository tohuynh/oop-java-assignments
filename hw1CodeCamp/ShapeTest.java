import junit.framework.TestCase;

public class ShapeTest extends TestCase {

	public void testCross() {
		Shape a = new Shape("0 0 0 1 1 1 1 0");
		Shape b = new Shape("10 10 10 11 11 11 11 10");
		Shape c = new Shape("0.5 0.5 0.5 -10 1.5 0");
		Shape d = new Shape("0.5 0.5 0.75 0.75 0.75 0.2");
		
		assertEquals(false , a.cross(b));
		assertEquals(true , a.cross(c));
		assertEquals(false , a.cross(d));
	}
	
	public void testEncircle() {
		Shape a = new Shape("0 0 0 1 1 1 1 0");
		Shape b = new Shape("10 10 10 11 11 11 11 10");
		Shape c = new Shape("0.5 0.5 0.5 -10 1.5 0");
		Shape d = new Shape("0.5 0.5 0.75 0.75 0.75 0.2");
		
		assertEquals(0 , a.encircle(b));
		assertEquals(1 , a.encircle(c));
		assertEquals(2 , a.encircle(d));
	}

}
