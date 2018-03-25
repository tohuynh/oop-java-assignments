import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class DShapeModel {
	private Rectangle bounds;
	private Color color;
	public int id;
	private List<ModelListener> listeners;
	public DShapeModel() {
		this(0, 0, 0, 0, Color.GRAY);
	}

	public DShapeModel(int x, int y, int width, int height, Color color) {
		bounds = new Rectangle(x, y, width, height);
		this.color = color;

		listeners = new ArrayList<ModelListener>();
	}

	public void mimic(DShapeModel other) {
		setId(other.getId());
		setBounds(other.getBounds());
		setColor(other.getColor());
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(int x, int y, int width, int height) {
		this.bounds = new Rectangle(x, y, width, height);
		notifyListeners();
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = new Rectangle(bounds);
		notifyListeners();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		notifyListeners();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		notifyListeners();
	}

	public void addListener(ModelListener ml) {
		listeners.add(ml);
	}

	public void notifyListeners() {
		for (ModelListener ml: listeners) {
			ml.modelChanged(this);;
		}
	}

	public void doRemoveListener(ModelListener listener) {
		listeners.remove(listener);
	}

	public void moveBy(int dx, int dy) {
		bounds.x += dx;
		bounds.y += dy;
		notifyListeners();
	}

	public void resizeBy(Point anchorPoint, Point movingPoint) {
		int x = anchorPoint.x < movingPoint.x ? anchorPoint.x : movingPoint.x;
		int y = anchorPoint.y < movingPoint.y ? anchorPoint.y : movingPoint.y;
		int width = Math.abs(anchorPoint.x - movingPoint.x) + 1;
		int height = Math.abs(anchorPoint.y - movingPoint.y) + 1;
		setBounds(x, y, width, height);
	}

}
