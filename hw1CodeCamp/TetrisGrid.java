import java.util.Arrays;

//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {

	private boolean[][] grid;

	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}

	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		int last = -1; //index of empty row to shift down to
		int numberOfFullRows = 0;
		for (int row = 0; row < grid[0].length; row++) {
			if (isFullRow(row)) {
				if (numberOfFullRows == 0) last = row;
				numberOfFullRows++;
			} else {
				if (numberOfFullRows > 0) {
					shiftDownRow(row, last);
					last++;
				}
			}
		}
		
		addEmptyRows(numberOfFullRows);
	}
	

	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
	
	private void addEmptyRows(int numberOfEmptyRows) {
		for (int i = 1; i <= numberOfEmptyRows; i++) {
			for (int col = 0; col < grid.length; col++) {
				grid[col][grid[0].length - i] = false;
			}
		}
	}
	
	private void shiftDownRow(int sourceRow, int destRow) {
		for (int col = 0; col < grid.length; col++) {
			grid[col][destRow] = grid[col][sourceRow];
		}
	}

	private boolean isFullRow(int row) {
		for (int col = 0; col < grid.length; col++) {
			if (!grid[col][row]) return false;
		}

		return true;
	}

}
