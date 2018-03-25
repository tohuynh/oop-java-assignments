import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class DLineModel extends DShapeModel {
	private Point p1;
	private Point p2;
	
	public DLineModel() {
		this(0, 0, 10, 10);
	}
	
	public DLineModel(int x, int y, int width, int height) {
		super(x, y, width, height, Color.GRAY);
		this.p1 = new Point(getBounds().x, getBounds().y);
		this.p2 = new Point(getBounds().x + getBounds().width, getBounds().y + getBounds().height);
	}
	
	public void mimic(DShapeModel other) {
		super.mimic(other);
		setP1(((DLineModel) other).getP1());
		setP2(((DLineModel) other).getP2());
	}
	
	public Point getP1() {
		return p1;
	}
	
	public Point getP2() {
		return p2;
	}
	
	public void setP1(Point p) {
		p1 = new Point(p);
		notifyListeners();
	}
	
	public void setP2(Point p) {
		p2 = new Point(p);
		notifyListeners();
	}
	
	@Override
	public void moveBy(int dx, int dy) {
		p1.x += dx;
		p1.y += dy;
		p2.x += dx;
		p2.y += dy;
		super.moveBy(dx, dy);
	}
	
	@Override
	public void resizeBy(Point anchorPoint, Point movingPoint) {
		p1 = new Point(anchorPoint);
		p2 = new Point(movingPoint);
		super.resizeBy(anchorPoint, movingPoint);
	}

}
