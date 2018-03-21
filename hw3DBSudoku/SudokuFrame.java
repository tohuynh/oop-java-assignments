import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


public class SudokuFrame extends JFrame {
	private JTextArea puzzleArea;
	private JTextArea solutionArea;
	JButton checkButton;
	JCheckBox autoCheckBox;

	public SudokuFrame() {
		super("Sudoku Solver");

		// YOUR CODE HERE

		// Could do this:
		// setLocationByPlatform(true);
		setLayout(new BorderLayout(4, 4));

		//puzle
		puzzleArea = new JTextArea(15, 20);
		puzzleArea.setBorder(new TitledBorder("Puzzle"));


		puzzleArea.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if(autoCheckBox.isSelected()) update();
			}

			public void insertUpdate(DocumentEvent e) {
				if(autoCheckBox.isSelected()) update();
			}

			public void removeUpdate(DocumentEvent e) {
				if(autoCheckBox.isSelected()) update();
			}
		});

		add(puzzleArea, BorderLayout.WEST);

		//solution
		solutionArea = new JTextArea(15, 20);
		solutionArea.setBorder(new TitledBorder("Solution"));

		solutionArea.setEditable(false);

		add(solutionArea, BorderLayout.EAST);

		//options
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		checkButton = new JButton("Check");
		// add action listener to the check button
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});

		autoCheckBox = new JCheckBox("Auto Check");
		autoCheckBox.setSelected(true);
		autoCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((AbstractButton) e.getSource()).isSelected()) update();
			}
		});

		panel.add(checkButton);
		panel.add(autoCheckBox);
		add(panel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void update() {
		try {
			Sudoku sudoku = new Sudoku(puzzleArea.getText());
			int numberOfSolutions = sudoku.solve();
			if (numberOfSolutions > 0) {
				solutionArea.setText(sudoku.getSolutionText());
				solutionArea.append("\nsolutions: " + numberOfSolutions);
				solutionArea.append("\nelapsed: " + sudoku.getElapsed() + "ms");
			} else {
				solutionArea.setText("No solution found");
			}

		} catch (Exception e) {
			solutionArea.setText("Parsing problem");
		}
	}


	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
	}

}
