import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class WebFrame extends JFrame{

	private int numRunningWorkers;
	private int numCompletedWorkers;
	private long launchStartTime;


	private Semaphore canAddWorker;
	private Object canCreateWorkerLock;
	private DefaultTableModel model;
	private JTable table;
	private JButton singleFetch;
	private JButton concurrentFetch;
	private JTextField numWorkersField;
	private JLabel runningLabel;
	private JLabel completedLabel;
	private JLabel elapsedLabel;
	private JButton stopButton;
	private JProgressBar progressBar;

	private List<Thread> workers;
	private Thread launcher;


	public WebFrame(String fileName) {
		super("Web Frame");
		canCreateWorkerLock = new Object();
		workers = new ArrayList<Thread>();

		numRunningWorkers = 0;
		numCompletedWorkers = 0;

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		createTable(fileName);
		createFetchButtons();
		createNumWorkersField();
		createSummaryLabels();
		createStopButton();
		createProgressBar();
		
		setGUIReadyState();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public class Launcher extends Thread {
		@Override
		public void run() {
			workers.clear();
			signalWorkerStarted();
			for(int i = 0; i < model.getRowCount(); i++) {
				try {
					canAddWorker.acquire();
				} catch (InterruptedException e) {
					break;
				}
				synchronized(canCreateWorkerLock) {
					Thread worker = new WebWorker(model.getValueAt(i, 0).toString(), i, WebFrame.this);
					workers.add(worker);
					worker.start();
				}
			}

			signalWorkerEnded(false);

		}

	}

	public void updateTable(int row, String value) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				model.setValueAt(value, row, 1);
			}	
		});

	}

	public void signalWorkerStarted() {
		numRunningWorkers++;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				runningLabel.setText("Running:" + numRunningWorkers);
			}	
		});
	}

	public void signalWorkerEnded(boolean webWorker) {
		numRunningWorkers--;
		
		if (webWorker) {
			canAddWorker.release();
			numCompletedWorkers++;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				runningLabel.setText("Running:" + numRunningWorkers);
				completedLabel.setText("Completed:" + numCompletedWorkers);
				if (webWorker) progressBar.setValue(progressBar.getValue() + 1);
				
				if (numRunningWorkers == 0) {
					elapsedLabel.setText("Elapsed:" + (System.currentTimeMillis() - launchStartTime) + "ms");
					setGUIReadyState();
				}
			}	
		});


	}

	private void resetGUI() {
		numCompletedWorkers = 0;
		numRunningWorkers = 0;
		for (int i = 0; i < model.getRowCount(); i++) {
			model.setValueAt("", i, 1);
		}
		runningLabel.setText("Running:");
		completedLabel.setText("Completed:");
		elapsedLabel.setText("Elapsed:");
		setGUIReadyState();
		
	}

	private void setGUIReadyState() {
		singleFetch.setEnabled(true);
		concurrentFetch.setEnabled(true);
		numWorkersField.setEnabled(true);
		stopButton.setEnabled(false);
		progressBar.setValue(0);
	}

	private void setGUIFetchingState() {
		singleFetch.setEnabled(false);
		concurrentFetch.setEnabled(false);
		numWorkersField.setEnabled(false);
		stopButton.setEnabled(true);
	}

	private void launchWorkers(int numWorkers) {
		resetGUI();
		launcher = new Launcher();
		canAddWorker = new Semaphore(numWorkers);
		launchStartTime = System.currentTimeMillis();
		launcher.start();
		setGUIFetchingState();
	}

	private void createTable(String fileName) {
		model = new DefaultTableModel(new String[] { "url", "status"}, 0);
		addUrls(fileName);
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(600,300));
		add(scrollpane);
	}

	private void addUrls(String fileName) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				model.addRow(new String[] {line.trim(), ""});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try{
				if (reader != null) reader.close();
			}
			catch(IOException ignored) {}
		}
	}

	private void createFetchButtons() {
		singleFetch = new JButton("SIngle Thread Fetch");
		singleFetch.setAlignmentX(Component.LEFT_ALIGNMENT);
		singleFetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launchWorkers(1);	
			}
		});

		add(singleFetch);

		concurrentFetch = new JButton("Concurrent Fetch");
		concurrentFetch.setAlignmentX(Component.LEFT_ALIGNMENT);
		concurrentFetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launchWorkers(Integer.parseInt(numWorkersField.getText()));
			}
		});

		add(concurrentFetch);
	}

	private void createNumWorkersField() {
		numWorkersField = new JTextField(Integer.toString(1));
		numWorkersField.setAlignmentX(Component.LEFT_ALIGNMENT);
		numWorkersField.setMaximumSize(new Dimension(100, numWorkersField.getHeight()));
		add(numWorkersField);
	}

	private void createSummaryLabels() {
		runningLabel = new JLabel("Running:");
		runningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(runningLabel);

		completedLabel = new JLabel("Completed:");
		completedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(completedLabel);

		elapsedLabel = new JLabel("Elapsed:");
		elapsedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(elapsedLabel);
	}

	private void createStopButton() {
		stopButton = new JButton("Stop");
		stopButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(canCreateWorkerLock) {
					launcher.interrupt();
					launcher = null;
					
					for (Thread worker: workers) {
						worker.interrupt();
						worker = null;
					}
					
				}
			}
		});

		stopButton.setEnabled(false);
		add(stopButton);
	}

	private void createProgressBar() {
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setMaximum(model.getRowCount());
		progressBar.setStringPainted(true);
		add(progressBar);
	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		String fileName = args[0].trim();

		new WebFrame(fileName);

	}
}
