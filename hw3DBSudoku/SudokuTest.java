


import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class SudokuTest extends TestCase {
	
	public void testGenerateValues() {
		Sudoku su = new Sudoku(Sudoku.easyGrid);
		Set<Integer> col = su.getColValues(0);
		Set<Integer> row = su.getRowValues(0);
		Set<Integer> sq = su.getSquareValues(4);
		
		assertEquals(5, col.size());
		assertEquals(4, row.size());
		assertEquals(4, sq.size());
		
		assertTrue(col.contains(1));
		assertTrue(col.contains(2));
		assertTrue(col.contains(4));
		assertTrue(col.contains(5));
		assertTrue(col.contains(8));
		
		assertTrue(row.contains(1));
		assertTrue(row.contains(6));
		assertTrue(row.contains(4));
		assertTrue(row.contains(2));
		
		assertTrue(sq.contains(6));
		assertTrue(sq.contains(1));
		assertTrue(sq.contains(2));
		assertTrue(sq.contains(9));
		
	}
	
	public void testSpots() {
		Sudoku su = new Sudoku(Sudoku.easyGrid);
		List<Sudoku.Spot> spots = su.getSpots();
		Collections.sort(spots);
		int previousNumberOfCandidates = 0;
		for (Sudoku.Spot sp: spots) {
			assertTrue(sp.getCandidateValues().size() >= previousNumberOfCandidates);
			previousNumberOfCandidates = sp.getCandidateValues().size();
		}
		
		Sudoku.Spot sp = su.new Spot(0, 3);
		assertTrue(sp.getCandidateValues().contains(7));
		assertEquals(1, sp.getCandidateValues().size());
		
		Sudoku.Spot sp2 = su.new Spot(8, 4);
		assertEquals(3, sp2.getCandidateValues().size());
		assertTrue(sp2.getCandidateValues().contains(1));
		assertTrue(sp2.getCandidateValues().contains(2));
		assertTrue(sp2.getCandidateValues().contains(3));
		
		assertTrue(sp.compareTo(sp2) < 0);
		
		sp2.setValue(1);
		assertEquals(2, sp2.getCandidateValues().size());
		assertFalse(sp2.getCandidateValues().contains(1));
		assertTrue(sp2.getCandidateValues().contains(2));
		assertTrue(sp2.getCandidateValues().contains(3));
		
		sp2.resetValue();
		assertEquals(3, sp2.getCandidateValues().size());
		assertTrue(sp2.getCandidateValues().contains(1));
		assertTrue(sp2.getCandidateValues().contains(2));
		assertTrue(sp2.getCandidateValues().contains(3));
	}
	
	

	

}
