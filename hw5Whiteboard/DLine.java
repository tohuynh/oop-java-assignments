import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class DLine extends DShape {
	public DLine(DShapeModel model, Canvas canvas) {
		super(model, canvas);
	}

	public void draw(Graphics g, boolean selected) {
		g.setColor(model.getColor());
		DLineModel model = (DLineModel) getModel();
		Point p1 = model.getP1();
		Point p2 = model.getP2();
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
		if (selected) {
			drawKnobs(g);
		}
	}

	@Override
	public boolean containsPoint(Point point) {
		Rectangle bounds = getBounds();
		Rectangle lineBounds;
		//return bounds.contains(point);

		if (bounds.width == 0) {
			lineBounds = new Rectangle(bounds.x - 3, bounds.y, 7, bounds.height);
			return lineBounds.contains(point);
		}

		if (bounds.height == 0) {
			lineBounds = new Rectangle(bounds.x, bounds.y - 3, bounds.width, 7);
			return lineBounds.contains(point);
		}

		//can be changed to be near the actual line, not within the bounds
		return super.containsPoint(point);

	}

	@Override
	public List<Point> getKnobs() {
		List<Point> knobs = new ArrayList<Point>();
		DLineModel model = (DLineModel) getModel();
		knobs.add(model.getP1());
		knobs.add(model.getP2());
		return knobs;
	}

}
