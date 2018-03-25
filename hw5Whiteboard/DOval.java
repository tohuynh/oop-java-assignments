import java.awt.Graphics;
import java.awt.Rectangle;

public class DOval extends DShape{

	public DOval(DShapeModel model, Canvas canvas) {
		super(model, canvas);
	}

	public DOval(Canvas canvas) {
		super(new DOvalModel(), canvas);
	}

	public void draw(Graphics g, boolean selected) {
		g.setColor(model.getColor());
		Rectangle bounds = model.getBounds();
		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);

		if (selected) {
			drawKnobs(g);
		}
	}

}