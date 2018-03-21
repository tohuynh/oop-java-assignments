// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int minRow = grid.length;
		int minCol = minRow > 0 ? grid[0].length : 0;
		int maxRow = 0;
		int maxCol = 0;
		
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				if (grid[row][col] == ch) {
					if (minCol > col + 1) minCol = col + 1;
                    if (minRow > row + 1) minRow = row + 1;
                    if (maxRow < row + 1) maxRow = row + 1;
                    if (maxCol < col + 1) maxCol = col + 1;
				}
			}
			
		}
		
		return Math.max(0, maxRow - minRow + 1) * Math.max(0, maxCol - minCol + 1);
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int plusCount = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				if (isValidPlus(row, col)) plusCount++;
			}
		}
		return plusCount;
			
	}
	
	private boolean isValidPlus(int row, int col) {
		int leftCount = countRepeats(row, col, 0, -1);
		int rightCount = countRepeats(row, col, 0, 1);
		int downCount = countRepeats(row, col, -1, 0);
		int upCount = countRepeats(row, col, 1, 0);
		
		return leftCount != 0 && leftCount == rightCount  && leftCount == downCount && leftCount == upCount;
	}
	
	private int countRepeats(int row, int col, int rowShift, int colShift) {
		int nextRow = row + rowShift;
		int nextCol = col + colShift;
		int count = 0;
		
		while (isValidCell(nextRow, nextCol)) {
			if (grid[row][col] == grid[nextRow][nextCol]) {
				count++;
				row += rowShift;
				col += colShift;
				nextRow += rowShift;
				nextCol += colShift;
			} else {
				break;
			}
		}
		
		return count;
			
	}
	
	private boolean isValidCell(int row, int col) {
		return  row >=0 && col >=0 && row < grid.length && col < grid[0].length ;
	}
}
