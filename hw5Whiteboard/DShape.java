import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DShape implements ModelListener {
	public static final int KNOB_SIZE = 9;
	public static final Color KNOB_COLOR = Color.BLACK;

	protected DShapeModel model;
	protected Canvas canvas;

	public DShape(Canvas canvas) {
		this(new DShapeModel(), canvas);

	}

	public DShape(DShapeModel model, Canvas canvas) {
		this.model = model;
		this.canvas = canvas;
		model.addListener(this);
	}
	public void modelChanged(DShapeModel model) {
		if (model == this.model) {
			canvas.repaint(getSelectedBounds());
		}

	}

	abstract public void draw(Graphics g, boolean selected);

	public boolean containsPoint(Point point) {
		return getBounds().contains(point);
	}

	public Rectangle getBounds() {
		return model.getBounds();
	}

	public Rectangle getSelectedBounds() {
		Rectangle bounds = getBounds();
		return new Rectangle(bounds.x - KNOB_SIZE/2, bounds.y - KNOB_SIZE/2, bounds.width + KNOB_SIZE, bounds.height + KNOB_SIZE);
	}

	public Color getColor() {
		return model.getColor();
	}

	public DShapeModel getModel() {
		return model;
	}

	public void doMove(int dx, int dy) {
		canvas.repaint(getSelectedBounds());
		model.moveBy(dx,dy);
	}

	public void doResize(Point anchorPoint, Point movingPoint) {
		canvas.repaint(getSelectedBounds());
		model.resizeBy(anchorPoint, movingPoint);

	}

	public List<Point> getKnobs() {
		List<Point> knobs = new ArrayList<Point>();
		Rectangle bounds = getBounds();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				knobs.add(new Point(bounds.x + bounds.width*i, bounds.y + bounds.height*j));
			}
		}
		Collections.swap(knobs, 2, 3);
		return knobs;
	}

	protected void drawKnobs(Graphics g) {
		g.setColor(KNOB_COLOR);
		for (Point p: getKnobs()) {
			g.fillRect(p.x - KNOB_SIZE/2, p.y - KNOB_SIZE/2, KNOB_SIZE, KNOB_SIZE);
		}
	}

}
