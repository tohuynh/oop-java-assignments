import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece st1, st2, st3;
	private Piece l1, l2, l3, l4, l5;
	private Piece la1, la2, la3, la4, la5;
	private Piece s1, s2, s3;
	private Piece sa1, sa2, sa3;
	private Piece sq1, sq2;
	private Piece pyr1, pyr2, pyr3, pyr4, pyr5;

	protected void setUp() throws Exception {
		super.setUp();
		
		st1 = new Piece(Piece.STICK_STR);
		st2 = st1.computeNextRotation();
		st3 = st2.computeNextRotation(); //same as st1
		
		l1 = new Piece(Piece.L1_STR);
		l2 = l1.computeNextRotation();
		l3 = l2.computeNextRotation();
		l4 = l3.computeNextRotation();
		l5 = l4.computeNextRotation(); // same as l1
		
		la1 = new Piece(Piece.L2_STR);
		la2 = la1.computeNextRotation();
		la3 = la2.computeNextRotation();
		la4 = la3.computeNextRotation();
		la5 = la4.computeNextRotation(); // same as la1
		
		s1 = new Piece(Piece.S1_STR);
		s2 = s1.computeNextRotation();
		s3 = s2.computeNextRotation(); //same as s1
		
		sa1 = new Piece(Piece.S2_STR);
		sa2 = sa1.computeNextRotation();
		sa3 = sa2.computeNextRotation(); //same as sa1
		
		sq1 = new Piece(Piece.SQUARE_STR);
		sq2 = sq1.computeNextRotation(); //same as sq1
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		pyr5 = pyr4.computeNextRotation(); //same as pyr1
		
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		//STICK
		assertEquals(1, st1.getWidth());
		assertEquals(4, st1.getHeight());
		
		assertEquals(4, st2.getWidth());
		assertEquals(1, st2.getHeight());
		
		assertEquals(1, st3.getWidth());
		assertEquals(4, st3.getHeight());
		
		//L1
		assertEquals(2, l1.getWidth());
		assertEquals(3, l1.getHeight());
		
		assertEquals(3, l2.getWidth());
		assertEquals(2, l2.getHeight());
		
		assertEquals(2, l3.getWidth());
		assertEquals(3, l3.getHeight());
		
		assertEquals(3, l4.getWidth());
		assertEquals(2, l4.getHeight());
		
		assertEquals(2, l5.getWidth());
		assertEquals(3, l5.getHeight());
		
		//L2
		assertEquals(2, la1.getWidth());
		assertEquals(3, la1.getHeight());
		
		assertEquals(3, la2.getWidth());
		assertEquals(2, la2.getHeight());
		
		assertEquals(2, la3.getWidth());
		assertEquals(3, la3.getHeight());
		
		assertEquals(3, la4.getWidth());
		assertEquals(2, la4.getHeight());
		
		assertEquals(2, la5.getWidth());
		assertEquals(3, la5.getHeight());
		
		//S1
		assertEquals(3, s1.getWidth());
		assertEquals(2, s1.getHeight());
		
		assertEquals(2, s2.getWidth());
		assertEquals(3, s2.getHeight());
		
		assertEquals(3, s3.getWidth());
		assertEquals(2, s3.getHeight());
		
		//S2
		assertEquals(3, sa1.getWidth());
		assertEquals(2, sa1.getHeight());
		
		assertEquals(2, sa2.getWidth());
		assertEquals(3, sa2.getHeight());
		
		assertEquals(3, sa3.getWidth());
		assertEquals(2, sa3.getHeight());
		
		//SQUARE
		assertEquals(2, sq1.getWidth());
		assertEquals(2, sq1.getHeight());
		
		assertEquals(2, sq2.getWidth());
		assertEquals(2, sq2.getHeight());
		
		//PYRAMID
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		assertEquals(3, pyr3.getWidth());
		assertEquals(2, pyr3.getHeight());
		
		assertEquals(2, pyr4.getWidth());
		assertEquals(3, pyr4.getHeight());
		
		assertEquals(3, pyr5.getWidth());
		assertEquals(2, pyr5.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		
		//STICK
		assertTrue(Arrays.equals(new int[] {0}, st1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0 ,0, 0}, st2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0}, st3.getSkirt()));
		
		//L1
		assertTrue(Arrays.equals(new int[] {0, 0}, l1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, l2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {2, 0}, l3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1, 1}, l4.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, l5.getSkirt()));
		
		//L2
		assertTrue(Arrays.equals(new int[] {0, 0}, la1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 1, 0}, la2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 2}, la3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, la4.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, la5.getSkirt()));
		
		//S1
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, s2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s3.getSkirt()));
		
		//S1
		assertTrue(Arrays.equals(new int[] {1, 0, 0}, sa1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1}, sa2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 0}, sa3.getSkirt()));
		
		//SQUARE
		assertTrue(Arrays.equals(new int[] {0, 0}, sq1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, sq2.getSkirt()));
		
		//PYRAMID
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, pyr2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1}, pyr4.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr5.getSkirt()));
	}
	
	public void testPieceEquals() {
		assertTrue(st1.equals(st3));
		assertFalse(st1.equals(st2));
		assertFalse(st2.equals(st3));
		
		assertTrue(l1.equals(l5));
		assertFalse(l1.equals(l2));
		assertFalse(l2.equals(l3));
		assertFalse(l3.equals(l4));
		
		assertTrue(la1.equals(la5));
		assertFalse(la1.equals(la2));
		assertFalse(la2.equals(la3));
		assertFalse(la3.equals(la4));
		assertFalse(la4.equals(la5));
		
		assertTrue(s1.equals(s3));
		assertFalse(s1.equals(s2));
		assertFalse(s2.equals(s3));
		
		assertTrue(sa1.equals(sa3));
		assertFalse(sa1.equals(sa2));
		assertFalse(sa2.equals(sa3));
		
		assertTrue(sq1.equals(sq2));
		
		assertTrue(pyr1.equals(pyr5));
		assertFalse(pyr1.equals(pyr2));
		assertFalse(pyr2.equals(pyr3));
		assertFalse(pyr3.equals(pyr4));
		assertFalse(pyr4.equals(pyr5));
	}
	
	public void testGetPieces() {
		Piece[] pieces = Piece.getPieces();
		assertTrue(pieces[Piece.STICK].equals(st1));
		assertTrue(pieces[Piece.L1].equals(l1));
        assertTrue(pieces[Piece.S1].equals(s1));
        assertTrue(pieces[Piece.L2].equals(la1));
        assertTrue(pieces[Piece.SQUARE].equals(sq1));
        assertTrue(pieces[Piece.PYRAMID].equals(pyr1));
        
        assertTrue(pieces[Piece.STICK].fastRotation().equals(st2));
        assertTrue(pieces[Piece.STICK].fastRotation().fastRotation().equals(st3));
        
        assertTrue(pieces[Piece.L1].fastRotation().equals(l2));
        assertTrue(pieces[Piece.L1].fastRotation().fastRotation().equals(l3));
        assertTrue(pieces[Piece.L1].fastRotation().fastRotation().fastRotation().equals(l4));
        assertTrue(pieces[Piece.L1].fastRotation().fastRotation().fastRotation().fastRotation().equals(l5));
        
        assertTrue(pieces[Piece.L2].fastRotation().equals(la2));
        assertTrue(pieces[Piece.L2].fastRotation().fastRotation().equals(la3));
        assertTrue(pieces[Piece.L2].fastRotation().fastRotation().fastRotation().equals(la4));
        assertTrue(pieces[Piece.L2].fastRotation().fastRotation().fastRotation().fastRotation().equals(la5));
        
        assertTrue(pieces[Piece.S1].fastRotation().equals(s2));
        assertTrue(pieces[Piece.S1].fastRotation().fastRotation().equals(s3));
        
        assertTrue(pieces[Piece.S2].fastRotation().equals(sa2));
        assertTrue(pieces[Piece.S2].fastRotation().fastRotation().equals(sa3));
        
        assertTrue(pieces[Piece.SQUARE].fastRotation().equals(sq2));
        
        assertTrue(pieces[Piece.PYRAMID].fastRotation().equals(pyr2));
        assertTrue(pieces[Piece.PYRAMID].fastRotation().fastRotation().equals(pyr3));
        assertTrue(pieces[Piece.PYRAMID].fastRotation().fastRotation().fastRotation().equals(pyr4));
        assertTrue(pieces[Piece.PYRAMID].fastRotation().fastRotation().fastRotation().fastRotation().equals(pyr5));
	}
	
}
