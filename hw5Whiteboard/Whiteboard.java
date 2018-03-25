import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Whiteboard extends JFrame{

	public static final int NORMAL    = 0;
	public static final int SERVER = 1;
	public static final int CLIENT  = 2;
	private int mode = NORMAL;

	private int currentId = 0;
	private Canvas canvas;
	private JTextField textField;
	private JComboBox fontField;
	private JButton rectButton;
	private JButton ovalButton;
	private JButton lineButton;
	private JButton textButton;
	private JButton setColor;
	private JButton moveToFront;
	private JButton moveToBack;
	private JButton removeShape;
	private JTable table;
	private WhiteboardTableModel tableModel;
	private JButton save;
	private JButton open;
	private JButton saveImage;
	private JFileChooser fc = new JFileChooser();
	private JButton serverButton;
	private JButton clientButton;
	private JLabel status;

	private ClientHandler clientHandler;
	private ServerAccepter serverAccepter;
	private List<ObjectOutputStream> outputs = new ArrayList<ObjectOutputStream>(); 

	public Whiteboard() {
		super("Whiteboard");
		setupWhiteboard();
		toggleAddShapeButtons(true);
		toggleShapeButtons(false);
		toggleTextShapeButtons(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void setupWhiteboard() {
		setLayout(new BorderLayout());
		canvas = new Canvas(this);

		JPanel westPanel = new JPanel();
		add(westPanel, BorderLayout.WEST);
		add(canvas, BorderLayout.CENTER);
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

		JPanel addPanel = new JPanel();
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
		addPanel.add(new JLabel("Add"));
		rectButton = new JButton("Rect");
		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked drect");
				DRectModel model = new DRectModel();
				model.setBounds(10,10,20,20);
				canvas.addShape(model);
			}
		});
		ovalButton = new JButton("Oval");
		ovalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked doval");
				DOvalModel model = new DOvalModel();
				model.setBounds(10,10,20,20);
				canvas.addShape(model);
			}
		});
		lineButton = new JButton("Line");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked dline");
				DLineModel model = new DLineModel(10, 10, 30, 0);
				canvas.addShape(model);
			}
		});

		textButton = new JButton("Text");
		textButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked dtext");
				DTextModel model = new DTextModel(10, 10, 20, 20);
				canvas.addShape(model);
			}

		});
		addPanel.add(rectButton);
		addPanel.add(ovalButton);
		addPanel.add(lineButton);
		addPanel.add(textButton);
		westPanel.add(addPanel);

		setColor = new JButton("Set Color");
		setColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked set color");
				DShape selected = canvas.getSelected();
				if (selected != null) {
					Color newColor = JColorChooser.showDialog(Whiteboard.this, "Pick a color", selected.getColor());
					if (newColor != null && selected.getColor() != newColor) {
						selected.getModel().setColor(newColor);
					}
				}
			}

		});

		westPanel.add(setColor);

		JPanel textPanel = new JPanel();
		textField = new JTextField("");
		textField.setMaximumSize(new Dimension(200, 20));
		textField.setPreferredSize(new Dimension(200, 20));
		textField.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				updateTextField(e);
			}

			public void removeUpdate(DocumentEvent e) {
				updateTextField(e);
			}

			public void changedUpdate(DocumentEvent e) {
				updateTextField(e);
			}

			private void updateTextField(DocumentEvent e) {
				if (canvas.hasSelected() && canvas.getSelected() instanceof DText) {
					((DText) canvas.getSelected()).doSetText(textField.getText());
				}
			}

		});
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String fonts[] = ge.getAvailableFontFamilyNames();
		fontField = new JComboBox(fonts);

		fontField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (canvas.hasSelected() && canvas.getSelected() instanceof DText) {
					((DText) canvas.getSelected()).doSetFontName((String)fontField.getSelectedItem());
				}	
			}

		});
		textPanel.add(textField);
		textPanel.add(fontField);
		westPanel.add(textPanel);

		JPanel alterPanel = new JPanel();
		alterPanel.setLayout(new BoxLayout(alterPanel, BoxLayout.X_AXIS));
		moveToFront = new JButton("Move to Front");
		moveToBack = new JButton("Move to Back");
		removeShape = new JButton("Remove Shape");

		removeShape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canvas.hasSelected()) {
					System.out.println("clicked remove");
					canvas.removeShape(canvas.getSelected());
				}
			}
		});

		moveToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canvas.hasSelected()) {
					System.out.println("clicked move to front");
					canvas.moveTo(canvas.getSelected(), canvas.getShapesSize() - 1);
				}
			}
		});

		moveToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canvas.hasSelected()) {
					System.out.println("clicked move to back");
					canvas.moveTo(canvas.getSelected(), 0);
				}
			}
		});

		alterPanel.add(moveToFront);
		alterPanel.add(moveToBack);
		alterPanel.add(removeShape);

		westPanel.add(alterPanel);

		tableModel = new WhiteboardTableModel();
		table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(300, 200));
		westPanel.add(scrollpane);

		for (Component comp: westPanel.getComponents()) {
			((JComponent)comp).setAlignmentX(Box.LEFT_ALIGNMENT);
		}

		JPanel filePanel = new JPanel();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
		westPanel.add(filePanel);

		save = new JButton("Save");
		open = new JButton("Open");
		saveImage = new JButton("Save Image");

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked save");
				int returnVal = fc.showOpenDialog(Whiteboard.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					canvas.save(file);
				}
			}

		});

		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked open");
				int returnVal = fc.showOpenDialog(Whiteboard.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					canvas.open(file);
				}
			}

		});

		saveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked save image");
				int returnVal = fc.showOpenDialog(Whiteboard.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					canvas.saveIamge(file);
				}
			}

		});
		filePanel.add(save);
		filePanel.add(open);
		filePanel.add(saveImage);

		JPanel networkPanel = new JPanel();
		networkPanel.setLayout(new BoxLayout(networkPanel, BoxLayout.X_AXIS));
		westPanel.add(networkPanel);

		serverButton = new JButton("Start Server");
		clientButton = new JButton("Start Client");
		status = new JLabel("Normal mode");

		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doServer();
			}
		});

		clientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClient();
			}
		});

		networkPanel.add(serverButton);
		networkPanel.add(clientButton);
		networkPanel.add(status);




	}

	public void toggleAddShapeButtons(boolean enabled) {
		if (getMode() != CLIENT) {
			rectButton.setEnabled(enabled);
			ovalButton.setEnabled(enabled);
			lineButton.setEnabled(enabled);
			textButton.setEnabled(enabled);
		}
	}

	public void toggleShapeButtons(boolean enabled) {
		if (getMode() != CLIENT) {
			setColor.setEnabled(enabled);
			moveToFront.setEnabled(enabled);
			moveToBack.setEnabled(enabled);
			removeShape.setEnabled(enabled);
		}
	}

	public void toggleTextShapeButtons(boolean enabled) {
		if (getMode() != CLIENT) {
			textField.setEnabled(enabled);
			fontField.setEnabled(enabled);
		}
	}

	public void clientModeButtons() {
		rectButton.setEnabled(false);
		ovalButton.setEnabled(false);
		lineButton.setEnabled(false);
		textButton.setEnabled(false);
		setColor.setEnabled(false);
		moveToFront.setEnabled(false);
		moveToBack.setEnabled(false);
		removeShape.setEnabled(false);
		textField.setEnabled(false);
		fontField.setEnabled(false);
		open.setEnabled(false);
	}



	public void setTextField(String text) {
		textField.setText(text);
	}

	public WhiteboardTableModel getTableModel() {
		return tableModel;
	}

	public void doSend(int verb, DShapeModel model) {
		Message msg = new Message();
		msg.setVerb(verb);
		msg.setModel(model);
		sendRemote(msg);
	}

	public synchronized void sendRemote(Message msg) {
		System.out.println("inside send remote");
		OutputStream memStream = new ByteArrayOutputStream(); 
		XMLEncoder encoder = new XMLEncoder(memStream);
		encoder.writeObject(msg);
		encoder.close();
		String xmlString = memStream.toString();
		Iterator<ObjectOutputStream> it = outputs.iterator();
		while (it.hasNext()) {
			ObjectOutputStream out = it.next();
			try {
				out.writeObject(xmlString);
				//System.out.println(xmlString);
				out.flush();
			} catch (Exception ex) {
				System.out.println("send remote err");
				ex.printStackTrace();
				it.remove();
			}
		}
	}

	public synchronized void addOutput(ObjectOutputStream out) {
		System.out.println("got a client");
		outputs.add(out);
	}

	public void invokeToGUI(final Message msg) {
		System.out.println("inside invoketoGui");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DShape shape = canvas.getShapeById(msg.getModel().getId());
				switch(msg.getVerb()) {
				case Message.ADD:
					System.out.println("calling add inside invoke");
					canvas.addShape(msg.getModel());
					break;
				case Message.REMOVE:
					System.out.println("calling remove inside invoke");
					if (shape != null) {
						canvas.removeShape(shape);
					} else {
						System.err.println("unable to find shape by id to remove in invoketogui");
					}
					break;
				case Message.BACK:
					System.out.println("calling back inside invoke");
					if (shape != null) {
						canvas.moveTo(shape, 0);
					}
					break;
				case Message.FRONT:
					System.out.println("calling front inside invoke");
					if (shape != null) {
						canvas.moveTo(shape, canvas.getShapesSize() - 1);
					}
				case Message.CHANGE:
					System.out.println("calling change inside invoke");
					if (shape != null) {
						shape.getModel().mimic(msg.getModel());
					}
					break;
				default:
					break;
				}


			}

		});
	}

	private class ClientHandler extends Thread {
		private String name;
		private int port;

		ClientHandler(String name, int port) {
			this.name = name;
			this.port = port;
		}
		public void run() {
			try {
				Socket toServer = new Socket(name, port);
				ObjectInputStream in = new ObjectInputStream(toServer.getInputStream());
				while (true) {
					String xmlString = (String) in.readObject();
					XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
					Message msg = (Message) decoder.readObject();
					invokeToGUI(msg);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private class ServerAccepter extends Thread {
		private int port;

		ServerAccepter(int port) {
			this.port = port;
		}

		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket(port); 
				while (true) {
					Socket toClient = null;
					toClient = serverSocket.accept();
					final ObjectOutputStream out = new ObjectOutputStream(toClient.getOutputStream());
					if (!outputs.contains(out)) {
						//one time series of add
						Thread adder = new Thread(new Runnable() {
							public void run() {
								for (DShape shape: canvas.getShapes()) {
									try {
										Message msg = new Message();
										msg.setVerb(Message.ADD);
										msg.setModel(shape.getModel());
										OutputStream memStream = new ByteArrayOutputStream(); 
										XMLEncoder encoder = new XMLEncoder(memStream);
										encoder.writeObject(msg);
										encoder.close();
										String xmlString = memStream.toString();
										out.writeObject(xmlString);
										out.flush();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}

							}

						});

						adder.start();
						addOutput(out);
					}

				}
			} catch (IOException ex) {
				System.out.println("server error");
				ex.printStackTrace();
			}
		}
	}

	public static class Message {
		public static final int ADD    = 0;
		public static final int REMOVE = 1;
		public static final int FRONT  = 2;
		public static final int BACK   = 3;
		public static final int CHANGE = 4;

		public int verb;
		public DShapeModel model;

		public Message() {
			verb = -1;
			model = null;
		}

		public int getVerb() {
			return verb;
		}

		public DShapeModel getModel() {
			return model;
		}

		public void setVerb(int verb) {
			this.verb = verb;
		}

		public void setModel(DShapeModel model) {
			this.model = model;
		}
	}

	public void doServer() {
		String result = JOptionPane.showInputDialog("Run server on port", "8001");
		if (result != null) {
			status.setText("Server mode");
			setMode(SERVER);
			String[] parts = result.split(":");
			serverAccepter = new ServerAccepter(Integer.parseInt(result.trim())); 
			serverAccepter.start();
		}
	}

	public void doClient() {
		String result = JOptionPane.showInputDialog("Connect to host:port", "127.0.0.1:8001");
		if (result != null) {
			canvas.clearCanvas();
			status.setText("Client mode");
			setMode(CLIENT);
			clientModeButtons();
			String[] parts = result.split(":");
			clientHandler = new ClientHandler(parts[0].trim(), Integer.parseInt(parts[1].trim()));
			clientHandler.start();
		}
	}

	public int getCurrentId() {
		return currentId++;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		new Whiteboard();
		new Whiteboard();
		//new Whiteboard();
	}

}
