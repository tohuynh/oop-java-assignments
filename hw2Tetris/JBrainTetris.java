import java.awt.Dimension;

import javax.swing.*;

public class JBrainTetris extends JTetris {

	private Brain brain;
	private boolean newPiece;
	private Brain.Move move;
	
	private JCheckBox brainMode;
	private JCheckBox animateMode;
	private JSlider  adversary;
	private JLabel adversaryStatus;
	
	JBrainTetris(int pixels) {
		super(pixels);
		brain = new DefaultBrain();
	}
	
	@Override
	public JComponent createControlPanel() {
		JPanel panel = (JPanel)super.createControlPanel();
		
		panel.add(new JLabel("Brain:"));
		brainMode = new JCheckBox("Brain active");
		panel.add(brainMode);
		
		animateMode = new JCheckBox("Animate Falling");
        animateMode.setSelected(true);
        panel.add(animateMode);
        
        JPanel little = new JPanel();
        adversaryStatus = new JLabel("ok");
        little.add(adversaryStatus);
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0); // min, max, current
        adversary.setPreferredSize(new Dimension(100,15));
        little.add(adversary); // now add little to panel of controls
        panel.add(little);
		
		return panel;
	}
	
	@Override
    public Piece pickNextPiece() {
		Piece nextPiece = super.pickNextPiece();
		
        if (random.nextInt(100) < adversary.getValue()) {
            adversaryStatus.setText("*ok*");
            double worstScore = 0.;
            for (Piece piece : pieces) {
                board.undo();
                Brain.Move nextMove = brain.bestMove(board, piece, board.getHeight(), null);
                if (nextMove != null && nextMove.score > worstScore)   {
                    nextPiece = piece;
                    worstScore = nextMove.score;
                }
            }
            return nextPiece;
        }

        adversaryStatus.setText("ok");
        return nextPiece;
}
	
	@Override
	public void tick(int verb) {
		if (brainMode.isSelected() && verb == DOWN) {
			if (!newPiece) {
				newPiece = !newPiece;
				board.undo();
				move = brain.bestMove(board, currentPiece, board.getHeight(), move);
				//System.out.println(move.x + " " + move.y);
			}
			
			if (move != null) {
				if (!move.piece.equals(currentPiece)) super.tick(ROTATE);
				if (move.x > currentX) {
					super.tick(RIGHT);
				} else if (move.x < currentX) {
					super.tick(LEFT);
				} else if (!animateMode.isSelected() && move.x == currentX && currentY > move.y) {
					super.tick(DROP);
				}	
			}
			
			
		}
		super.tick(verb);
		
	}
	
	@Override
	public void addNewPiece() {
		newPiece = false;
		super.addNewPiece();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		JTetris tetris = new JBrainTetris(16);
		JFrame frame = JTetris.createFrame(tetris);
		frame.setVisible(true);

	}

}
