import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

public class Canvas extends JPanel implements ModelListener {
	private boolean dirty;
	private Whiteboard board;
	private List<DShape> shapes;
	private DShape selected;
	private int lastX, lastY;
	private Point movingPoint, anchorPoint;
	public Canvas(final Whiteboard board) {
		dirty = false;
		shapes = new ArrayList<DShape>();
		selected = null;
		setMinimumSize(new Dimension(400,400));
		setPreferredSize(getMinimumSize());
		setBackground(Color.WHITE);
		this.board = board;

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selectShape(e.getPoint());
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (board.getMode() != Whiteboard.CLIENT) {
					int dx = e.getX() - lastX;
					int dy = e.getY() - lastY;
					lastX = e.getX();
					lastY = e.getY();

					if (movingPoint != null) {
						movingPoint.x += dx;
						movingPoint.y += dy;
						getSelected().doResize(anchorPoint, movingPoint);

					} else if (hasSelected()) {
						getSelected().doMove(dx, dy);
					}
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(DShape shape: shapes) {
			shape.draw(g, selected == shape);
		}
	}

	public void addShape(DShapeModel model) {
		if (board.getMode() != Whiteboard.CLIENT) {
			model.setId(board.getCurrentId());
			//maybe need to reconsider
		}

		DShape shape;
		if (model instanceof DRectModel) {
			shape = new DRect(model, this);
			shapes.add(shape);
			System.out.println("added drect");
		} else if (model instanceof DOvalModel) {
			shape = new DOval(model, this);
			shapes.add(shape);
			System.out.println("added doval");
		} else if (model instanceof DLineModel) {
			shape = new DLine(model, this);
			shapes.add(shape);
			System.out.println("added dline");
		} else if (model instanceof DTextModel) {
			shape = new DText(model, this);
			shapes.add(shape);
			System.out.println("added dtext");
		}
		System.out.println(shapes);
		model.addListener(this);
		board.getTableModel().addModel(model);
		repaint();

		if (board.getMode() == Whiteboard.SERVER) {
			System.out.println("sending add msg");
			board.doSend(Whiteboard.Message.ADD, model);
		}
	}

	public void removeShape(DShape shape) {
		DShapeModel model = shape.getModel();
		model.doRemoveListener(shape);
		model.doRemoveListener(this);
		board.getTableModel().removeModel(model);
		shapes.remove(shape);
		repaint(shape.getSelectedBounds());
		board.toggleShapeButtons(false);
		board.toggleTextShapeButtons(false);
		if (shape == selected) {
			selected = null;
		}

		if (board.getMode() == Whiteboard.SERVER) {
			board.doSend(Whiteboard.Message.REMOVE, model);
		}

	}

	public void moveTo(DShape shape, int index) {
		Collections.swap(shapes, index, shapes.indexOf(shape));
		board.getTableModel().moveModelTo(shape.getModel(), index);
		if (board.getMode() == Whiteboard.SERVER) {
			board.doSend(index == 0 ? Whiteboard.Message.BACK: Whiteboard.Message.FRONT, shape.getModel());
		}
		repaint();
	}

	public void save(File file ) {
		try { 
			XMLEncoder xmlOut = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file))); 
			DShapeModel[] modelArray = board.getTableModel().getModels().toArray(new DShapeModel[0]); 
			xmlOut.writeObject(modelArray);
			xmlOut.close(); 
			setDirty(false); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
	}

	public void open(File file) {
		DShapeModel[] modelArray = null;
		try {
			clearCanvas();

			XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
			modelArray = (DShapeModel[]) xmlIn.readObject();
			xmlIn.close();

			for (DShapeModel model: modelArray) {
				addShape(model);
			}
			setDirty(false);
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
	}

	public void saveIamge(File file) {
		DShape selectedShape = selected;
		selected = null;
		BufferedImage image = (BufferedImage) createImage(getWidth(), getHeight());
		Graphics g = image.getGraphics();
		paintAll(g);
		g.dispose();
		try {
			javax.imageio.ImageIO.write(image, "PNG", file);
			selected = selectedShape;
		} catch (IOException ex) {
			ex.printStackTrace(); 
		}
	}

	public void clearCanvas() {
		List<DShape> shapesCopy = new ArrayList<DShape>(shapes);
		for (DShape shape: shapesCopy) {
			removeShape(shape);
		}
	}

	public int getShapesSize() {
		return shapes.size();
	}

	public List<DShape> getShapes() {
		return shapes;
	}

	public void modelChanged(DShapeModel model) {
		if (board.getMode() == Whiteboard.SERVER) {
			board.doSend(Whiteboard.Message.CHANGE, model);
		}
	}

	public DShape getSelected() {
		return selected;
	}

	public boolean hasSelected() {
		return selected != null;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean getDirty() {
		return dirty;
	}

	public DShape getShapeById(int id) {
		DShape shape = null;
		for (DShape s: shapes) {
			if (s.getModel().getId() == id) {
				shape = s;
				break;
			}
		}

		return shape;
	}

	private void selectShape(Point point) {
		lastX = point.x;
		lastY = point.y;
		movingPoint = null;
		anchorPoint = null;

		if (hasSelected()) {
			int i = 0;
			for (Point knobCenter: selected.getKnobs()) {
				Rectangle knobBounds = new Rectangle(knobCenter.x - DShape.KNOB_SIZE/2, knobCenter.y - DShape.KNOB_SIZE/2, DShape.KNOB_SIZE, DShape.KNOB_SIZE);
				if (knobBounds.contains(point)) {
					movingPoint = new Point(knobCenter);
					if (getSelected() instanceof DLine) {
						anchorPoint = new Point(selected.getKnobs().get((i + 1) % 2));
					} else {
						anchorPoint = new Point(selected.getKnobs().get((i + 2) % 4));
					}
					System.out.println("clicked on knob");
					break;
				}
				i++;
			}
		}

		if (movingPoint == null) {
			for (DShape shape: shapes) {
				if (shape.containsPoint(point)) {
					selected = shape;
					board.toggleShapeButtons(true);
					if (selected instanceof DText) {
						board.toggleTextShapeButtons(true);
						board.setTextField(((DTextModel) selected.getModel()).getText());
					} else {
						board.toggleTextShapeButtons(false);
					}
				}
			}
		}

		repaint();
	}
}
