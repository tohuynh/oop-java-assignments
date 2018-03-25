import java.awt.Graphics;
import java.awt.Rectangle;

public class DRect extends DShape{

	public DRect(DShapeModel model, Canvas canvas) {
		super(model, canvas);
	}

	public DRect(Canvas canvas) {
		super(new DRectModel(), canvas);
	}

	public void draw(Graphics g, boolean selected) {
		g.setColor(model.getColor());
		Rectangle bounds = model.getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		if (selected) {
			drawKnobs(g);
		}
	}
}
