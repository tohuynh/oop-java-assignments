import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	
	private static final Set<Integer> values = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
	private int[][] grid;
	private long elapsed;
	private HashMap<Integer, Set<Integer>> rowValues;
	private HashMap<Integer, Set<Integer>> colValues;
	private HashMap<Integer, Set<Integer>> squareValues;
	private List<Spot> spots;
	private int[][] solution;
	private int numberOfSolutions = 0;
	
	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = ints;
		generateValues();
		generateSpots();
	}
	
	public Sudoku(String text) {
		this(textToGrid(text));
	}
	
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		elapsed = System.currentTimeMillis();
		Collections.sort(spots);
		solveHelper(0);
		elapsed = System.currentTimeMillis() - elapsed;
		return numberOfSolutions; 
		// YOUR CODE HERE
	}

	public String getSolutionText() {
		if (numberOfSolutions > 0) {
			return toString(solution);
			
		}
		return "";
	}
	
	public long getElapsed() {
		return elapsed;
	}
	
	public Set<Integer> getRowValues(int row) {
		return rowValues.get(row);
	}
	
	public Set<Integer> getColValues(int col) {
		return colValues.get(col);
	}
	
	public Set<Integer> getSquareValues(int square) {
		return squareValues.get(square);
	}
	
	public List<Spot> getSpots() {
		return spots;
	}
	
	public String toString() {
		return toString(grid);
	}
	
	private void solveHelper(int position) {
		if (numberOfSolutions >= MAX_SOLUTIONS) return;
		if (position == spots.size()) {
			if (numberOfSolutions == 0) {
				saveSolution();
				
			}
			numberOfSolutions++;
			return;
		}
		
		Spot sp = spots.get(position);
		Set<Integer> candidateValues = sp.getCandidateValues();
		
		for (int value: candidateValues) {
			sp.setValue(value);
			solveHelper(position + 1);
			sp.resetValue();
		}
	}
	
	private void generateSpots() {
		spots = new ArrayList<Spot>();
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				if (grid[row][col] == 0) {
					Spot sp = new Spot(row, col);
					spots.add(sp);
				}
			}
		}
	}
	
	private void generateValues() {
		rowValues = new HashMap<Integer, Set<Integer>>();
		colValues = new HashMap<Integer, Set<Integer>>();
		squareValues = new HashMap<Integer, Set<Integer>>();
		
		for (int i = 0; i < SIZE; i++) {
			rowValues.put(i, new HashSet<Integer>());
			colValues.put(i, new HashSet<Integer>());
			squareValues.put(i, new HashSet<Integer>());
		}
		
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				int value = grid[row][col];
				if (value != 0) {
					rowValues.get(row).add(value);
					colValues.get(col).add(value);
					int squareNumber = PART * (row/PART) + (col/PART);
					squareValues.get(squareNumber).add(value);
				}
			}
		}
	}
	
	
	
	private void saveSolution() {
		solution = new int[SIZE][SIZE];
	    for(int i = 0; i < SIZE; i++)
	        System.arraycopy(grid[i], 0, solution[i], 0, SIZE);
	}
	
	private String toString(int[][] grid) {
		StringBuilder sb = new StringBuilder();
		
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				sb.append(grid[row][col]);
				if (col < SIZE - 1) sb.append(" ");
			}
			if (row < SIZE - 1) sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public class Spot implements Comparable<Spot>{
		private int row;
		private int col;
		private int square;
		
		
		public Spot(int row, int col) {
			this.row = row;
			this.col = col;
			square = squareNumber(row, col);
		}
		
		public int getRow() {
			return row;
		}
		
		public int getCol() {
			return col;
		}
		
		public int getSquare() {
			return square;
		}
		
		public void setValue(int value) {
			rowValues.get(row).add(value);
			colValues.get(col).add(value);
			squareValues.get(square).add(value);
			grid[row][col] = value;
		}
		
		public void resetValue() {
			int value = grid[row][col];
			rowValues.get(row).remove(value);
			colValues.get(col).remove(value);
			squareValues.get(square).remove(value);
			grid[row][col] = 0;
		}
		
		public Set<Integer> getCandidateValues() {
			Set<Integer> candidateValues = new HashSet<Integer>(values);
			candidateValues.removeAll(rowValues.get(row));
			candidateValues.removeAll(colValues.get(col));
			candidateValues.removeAll(squareValues.get(square));
			return candidateValues;
		}
		
		public int squareNumber(int row, int col) {
			return (col/PART) + (row/PART) * PART;
		}

		public int compareTo(Spot o) {
			return getCandidateValues().size() - o.getCandidateValues().size();
		}
	}

}