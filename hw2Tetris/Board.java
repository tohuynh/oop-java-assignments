import java.util.Arrays;

// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private int maxHeight;
	private boolean[][] grid;
	private boolean DEBUG = true;
	private int[] widths;
	private int[] heights;
	boolean committed;
	
	private int[] xWidths;
	private int[] xHeights;
	private int xMaxHeight;
	private boolean[][] xGrid;
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		maxHeight = 0;
		grid = new boolean[width][height];
		committed = true;
		
		widths = new int[height];
		heights = new int[width];
		
		xMaxHeight = 0;
		xGrid = new boolean[width][height];
		
		xWidths = new int[height];
		xHeights = new int[width];
		
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return maxHeight;
		//return 0; // YOUR CODE HERE
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			// YOUR CODE HERE
			int actualMaxHeight = 0;
			int[] actualHeights = new int[width];
			int[] actualWidths = new int[height];
			
			for (int x = 0; x < grid.length; x++) {
				for (int y = 0; y < grid[0].length; y++) {
					if (grid[x][y]) {
						actualWidths[y]++;
						if (y + 1 > actualHeights[x]) actualHeights[x] = y + 1;
						if (y + 1 > actualMaxHeight) actualMaxHeight = y + 1;
					}
					
				}
			}
			
			if (!Arrays.equals(actualHeights, heights)) {
				String actualHeightsStr = "actualHeights: " + Arrays.toString(actualHeights);
				String heightsStr = "heights: " + Arrays.toString(heights);
				throw new RuntimeException(actualHeightsStr + "\n" + heightsStr);
			}
			if (!Arrays.equals(actualWidths, widths)) {
				String actualWidthsStr = "actualWidths: " + Arrays.toString(actualWidths);
				String widthsStr = "widths: " + Arrays.toString(widths);
				throw new RuntimeException(actualWidthsStr + "\n" + widthsStr);
			}
			if (actualMaxHeight != maxHeight) {
				throw new RuntimeException("actualMaxHeight: " + actualMaxHeight + " maxHeight: " + maxHeight);
			}
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		//if (x < 0 || x + piece.getWidth() > width)
		//	return -1;
		int y = 0;
		
		for (int i = 0; i < piece.getWidth(); i++) {
			if (x + i < width) {
				int candidateY = heights[x + i] - piece.getSkirt()[i];
				if (candidateY > y) y = candidateY;
			}
			
		}
		return y;
		//return 0; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
		//return 0; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		return widths[y];
		 //return 0; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) return true;
		return grid[x][y];
		//return false; // YOUR CODE HERE
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		
		backUp();
		committed = false;
			
		int result = PLACE_OK;
		for (TPoint p: piece.getBody()) {
			int currentX = p.x + x;
			int currentY = p.y + y;
			
			if (currentX >= width || currentY >= height || currentX < 0 || currentY < 0 ) {
				return PLACE_OUT_BOUNDS;
			}
			
			if (grid[currentX][currentY]) {
				return PLACE_BAD;
			}
			
			grid[currentX][currentY] = true;
			
			if(heights[currentX] < currentY + 1) heights[currentX] = currentY + 1;
			if (heights[currentX] > maxHeight) maxHeight=heights[currentX];
			
			widths[currentY]++;
			
            if(widths[currentY]==width) result = PLACE_ROW_FILLED;
            
		}
		
		//if (result != PLACE_OUT_BOUNDS || result != PLACE_BAD) {
		sanityCheck();
		//}
		
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if (committed) {
			backUp();
			committed = false;
		}
		
		int rowsCleared = 0;
		// YOUR CODE HERE
		int last = -1; //index of empty row to shift down to
		for (int y = 0; y < maxHeight; y++) {
			if (widths[y] == width) {
				if (rowsCleared == 0) last = y;
				rowsCleared++;
			} else {
				if (rowsCleared > 0) {
					shiftDownRow(y, last);
					last++;
				}
			}
		}
		
		
		//for (int x = 0; x < width; x++) {
		//	grid[x][maxHeight - 1] = false;
			
		//}
		
		//widths[maxHeight - 1] = 0;
		
		for (int i = 1; i <= rowsCleared; i++) {
			for (int x = 0; x < grid.length; x++) {
				grid[x][maxHeight - i] = false;
			}
			widths[maxHeight - i] = 0;
		}
	
		/**for (int i = 0; i < width; i++) {
			int h = heights[i] - rowsCleared;
			heights[i] = h;
			for (int j = h - 1; j > -1; j--) {
				if (grid[i][j]) {
					heights[i] = j + 1;
					break;
				} 
			}
			//if (!grid[i][Math.max(0, h-1)]) heights[i] = Math.max(0, h-1);
			//else heights[i] = h;
			
		}*/
		//maxHeight -= rowsCleared; //fix
		updateHeights();
		sanityCheck();
		return rowsCleared;
	}
	
	private void updateHeights() {
		heights = new int[width];
		maxHeight = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (grid[x][y]) {
					heights[x] = y + 1;
					if (y + 1 > maxHeight) maxHeight = y + 1;
				}
			}
		}
		//System.out.println(Arrays.toString(heights));
	}
	
	private void shiftDownRow(int from, int to) {
		widths[to] = widths[from];
		for (int x = 0; x < width; x++) {
			grid[x][to] = grid[x][from];
			//heights[x] = to + 1;
		}
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (!committed) {
			int[] widthsTemp = widths;
			widths = xWidths;
			xWidths = widthsTemp;
			
			int[] heightsTemp = heights;
			heights = xHeights;
			xHeights = heightsTemp;
			
			//int maxHeightTemp = maxHeight;
			maxHeight = xMaxHeight;
			//xMaxHeight = maxHeightTemp;
			
			boolean[][] gridTemp = grid;
			grid = xGrid;
			xGrid = gridTemp;
			sanityCheck();
			commit();
		}
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
	
	private void backUp() {
		xMaxHeight = maxHeight;
		System.arraycopy(heights, 0, xHeights, 0, width);
		System.arraycopy(widths, 0, xWidths, 0, height);
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, xGrid[i], 0, height);
		}
	}
}


