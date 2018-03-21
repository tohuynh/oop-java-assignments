import java.util.Arrays;

import junit.framework.TestCase;


public class BoardTest extends TestCase {
	private Piece st1, st2, st3;
	private Piece l1, l2, l3, l4, l5;
	private Piece la1, la2, la3, la4, la5;
	private Piece s1, s2, s3;
	private Piece sa1, sa2, sa3;
	private Piece sq1, sq2;
	private Piece pyr1, pyr2, pyr3, pyr4, pyr5;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		
		st1 = new Piece(Piece.STICK_STR);
		st2 = st1.computeNextRotation();
		
		l1 = new Piece(Piece.L1_STR);
		l2 = l1.computeNextRotation();
		l3 = l2.computeNextRotation();
		l4 = l3.computeNextRotation();
		
		la1 = new Piece(Piece.L2_STR);
		la2 = la1.computeNextRotation();
		la3 = la2.computeNextRotation();
		la4 = la3.computeNextRotation();
		
		s1 = new Piece(Piece.S1_STR);
		s2 = s1.computeNextRotation();
		
		sa1 = new Piece(Piece.S2_STR);
		sa2 = sa1.computeNextRotation();
		
		sq1 = new Piece(Piece.SQUARE_STR);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
	}
	
	public void testPlaceEdge() {
		Board b = new Board(5,3);
		int result = b.place(st2, 0, 0);
		assertEquals(Board.PLACE_OK, result);
		b.commit();
		
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(2));
		assertEquals(1, b.getColumnHeight(3));
		assertEquals(0, b.getColumnHeight(4));
		
		assertEquals(4, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		
		result = b.place(st2, 0, 1);
		assertEquals(Board.PLACE_OK, result);
		b.commit();
		
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getColumnHeight(2));
		assertEquals(2, b.getColumnHeight(3));
		assertEquals(0, b.getColumnHeight(4));
		
		assertEquals(4, b.getRowWidth(0));
		assertEquals(4, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		
		result = b.place(st2, 0, 2);
		assertEquals(Board.PLACE_OK, result);
		b.commit();
		
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(3, b.getColumnHeight(3));
		assertEquals(0, b.getColumnHeight(4));
		
		assertEquals(4, b.getRowWidth(0));
		assertEquals(4, b.getRowWidth(1));
		assertEquals(4, b.getRowWidth(2));
		
		result = b.place(st2, 0, 3);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		
	}
	
	public void testDropHeight() {
		Board b = new Board(3,6);
		b.place(pyr1, 0, 0);
		b.commit();
		int y = b.dropHeight(s2, 1);
		assertEquals(1, y);
		y = b.dropHeight(s2, 0);
		assertEquals(2, y);
		y = b.dropHeight(s2, 3);
		assertEquals(0,y);
		y = b.dropHeight(s2, 2);///fix
		assertEquals(0, y);
		int result = b.place(s2, 2, y);
		b.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		y = b.dropHeight(st1, 0);
		b.place(st1, 0, y);
		b.commit();
		
		y = b.dropHeight(st1, 0);
		result = b.place(st1, 0, y);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
			
	}
	
	public void testDropHeight2() {
		Board b = new Board(3,6);
		b.place(pyr4, 0, 0);
		int y = b.dropHeight(l3, 1);
		assertEquals(0, y);
		y = b.dropHeight(st1, 1);
		assertEquals(2, y);
		
		b.undo();
		b.place(s1, 0, 0);
		y = b.dropHeight(l4, 0);
		assertEquals(1, y);
		
		b.undo();
		b.place(s2, 0, 0);
		y = b.dropHeight(l3, 0);
		assertEquals(2, y);
		
		b.undo();
		b.place(la4, 0, 0);
		y = b.dropHeight(pyr3, 0);
		assertEquals(1, y);
	}
	
	public void testPlace() {
		Board b = new Board(3,4);
		int result = b.place(pyr1, 0, 0);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		
		b.undo();
		
		result = b.place(pyr2, 0, 0);
		assertEquals(Board.PLACE_OK, result);
		
		b.commit();
		
		result = b.place(st1, 0, 2);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		
		b.undo();
		
		result = b.place(st1, 0, 0);
		assertEquals(Board.PLACE_BAD, result);
		
		b.undo();
		
		result = b.place(st2, 0, 0);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();
		
		result = b.place(st2, 5, 5);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		
	}
	
	public void testClearRows1RowStart() {
		Board b = new Board(3,7);
		b.place(pyr1, 0, 0);
		b.commit();
		b.place(st1, 0, 1);
		b.clearRows();
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
	}
	
	public void testClearRows1RowBetween() {
		Board b = new Board(3,7);
		b.place(pyr3, 0, 0);
		b.commit();
		b.place(s1, 0, 2);
		b.clearRows();
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(3, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
	}
	
	
	
	public void testClearRows1RowEnd() {
		Board b = new Board(3,2);
		b.place(pyr3, 0, 0);
		b.clearRows();
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(1, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
	}
	
	
	public void testClearRows2RowsNonCont() {
		Board b = new Board(3,7);
		//two rows non continous
		b.place(pyr3, 0, 0);
		b.commit();
		b.place(s1, 0, 2);
		b.commit();
		b.place(pyr1, 0, 4);
		b.commit();
		b.place(la2, 0, 5);
		b.clearRows();
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(4, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getRowWidth(2));
		assertEquals(2, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
	}
	
	public void testClearRows2RowsCont() {
		//two rows continuous
		//end row
		Board b = new Board(3,10);
		b.place(pyr1, 0, 0);
		b.commit();
		b.place(pyr1, 0, 2);
		b.commit();
		b.place(st1, 0, 3);
		b.commit();
		b.place(st1, 2, 3);
		b.commit();
		b.place(pyr1, 0, 7);
		b.commit();
		b.place(la2, 0, 8);
		b.clearRows();
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(5, b.getColumnHeight(1));
		assertEquals(5, b.getColumnHeight(2));
		assertEquals(5, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getRowWidth(2));
		assertEquals(2, b.getRowWidth(3));
		assertEquals(2, b.getRowWidth(4));
		assertEquals(0, b.getRowWidth(5));
		
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		Board b = new Board(3,6);
		int result = b.place(pyr1, 0, 0);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(2));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		
		b.clearRows();
		
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(1, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		
		b.commit();
		String before = b.toString();
		
		result = b.place(st1, 0, 0);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
		
		b.undo();
		
		assertEquals(before, b.toString());
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(1, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		
	}
	
	public void testBoardEge() {
		Board b = new Board(3,2);
		String before = b.toString();
		b.place(st2, 0, 0);
		b.commit();
		b.place(st2, 0, 1);
		//b.commit();
		//b.place(st2,  0, 2);
		b.clearRows();
		b.commit();
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getMaxHeight());
		
		assertEquals(before, b.toString());
	}
	
	public void testAdvanced() {
		Board b = new Board(3,6);
		int y = b.dropHeight(pyr1, 0);
		assertEquals(0, y);
		b.place(pyr1, 0, y);
		b.clearRows();
		b.commit(); //A1
		y = b.dropHeight(st1, 0);
		assertEquals(0, y);
		int result = b.place(st1, 0, y);
		assertEquals(Board.PLACE_OK, result);
		b.clearRows();
		
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
		assertEquals(4, b.getMaxHeight());
		
		b.undo(); //back to A1
		
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(1, b.getMaxHeight());
		
		y = b.dropHeight(s2, 1);
		assertEquals(0, y);
		b.place(s2, 1, y);
		//b.clearRows();
		b.commit();
		
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(2, b.getColumnHeight(2));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(3, b.getMaxHeight());
		
		y = b.dropHeight(st1, 0);
		assertEquals(0, y);
		b.place(st1, 0, y);
		b.clearRows();
		
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(2, b.getMaxHeight());
		
		b.commit();
		
		y = b.dropHeight(pyr2, 1);
		assertEquals(0, y);
		result = b.place(pyr2, 1, y);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		b.clearRows();
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(2));
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(1, b.getMaxHeight());
		b.commit();
		
		assertTrue(b.getGrid(2, 0));
		assertFalse(b.getGrid(1, 0));
		assertFalse(b.getGrid(0, 0));
		
	}
	
	public void testUndo() {
		Board b = new Board(3,4);
		String before = b.toString();
		b.place(pyr1, 0, 0);
		b.undo();
		assertEquals(before, b.toString());
		
		b.place(pyr1, 0, 0);
		b.clearRows();
		b.undo();
		assertEquals(before, b.toString());
		
		
	}
	
	// Makre  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	
}
