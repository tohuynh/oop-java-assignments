// JCount.java

/*
 Basic GUI/Threading exercise.
 */

import javax.swing.*;
import java.awt.event.*;

public class JCount extends JPanel {
	public static final long DEFAULT_BOUND = 1000000000;
	private JTextField bound;
	private JLabel status;
	private Worker worker;
	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		bound = new JTextField(Long.toString(DEFAULT_BOUND));
		status = new JLabel(Long.toString(0));

		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (worker != null) { 
					worker.interrupt();
					worker = null;
				}
				worker = new Worker(Long.parseLong(bound.getText()));
				worker.start();
			}
		});

		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(worker != null) {
					worker.interrupt();
					worker = null;
				}
			}
		});

		add(bound);
		add(status);
		add(startButton);
		add(stopButton);
		add(Box.createVerticalStrut(40));

		// YOUR CODE HERE
	}

	public class Worker extends Thread implements Runnable{
		private long bound;

		public Worker(long bound) {
			this.bound = bound;
		}

		@Override
		public void run() {
			for(long i = 0; i <= bound; i++) {
				if(isInterrupted()) break;

				if(i % 1000000 == 0) {
					final long newStatus = i;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							status.setText(Long.toString(newStatus));
						}
					});
				}
			}

		}

	}

	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

