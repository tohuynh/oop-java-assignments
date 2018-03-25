import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

public class DText extends DShape implements ModelListener {
	private boolean heightChanged;
	private Font font;
	public DText(DShapeModel model, Canvas canvas) {
		super(model, canvas);
		heightChanged = true;
	}

	@Override
	public void draw(Graphics g, boolean selected) {
		Shape clip = g.getClip();
		g.setClip(clip.getBounds().createIntersection(getBounds()));

		if (heightChanged) {
			computeFont(g);
		}
		int fontOffset = (int) font.getLineMetrics(((DTextModel) model).getText(), ((Graphics2D) g).getFontRenderContext()).getDescent();
		int y = getBounds().y + getBounds().height - fontOffset;

		g.setColor(getColor());
		g.setFont(font);

		g.drawString(((DTextModel) model).getText(), getBounds().x, y);
		g.setClip(clip);
		if(selected) {
			drawKnobs(g);
		}

	}

	public void doSetText(String text) {
		canvas.repaint(getSelectedBounds());
		((DTextModel)getModel()).setText(text);
	}

	public void doSetFontName(String fontName) {
		canvas.repaint(getSelectedBounds());
		((DTextModel)getModel()).setFontName(fontName);
	}
	
	public void doResize(Point anchorPoint, Point movingPoint) {
		heightChanged = true;
		super.doResize(anchorPoint, movingPoint);
	}
	
	private void computeFont(Graphics g) {
		double size = 1.0;
		double lastSize = size;
		Font computedFont;
		while(true) {
			computedFont = new Font(((DTextModel) model).getFontName(), Font.PLAIN, (int) size);
			if(computedFont.getLineMetrics(((DTextModel) model).getText(), ((Graphics2D) g).getFontRenderContext()).getHeight() > getModel().getBounds().height)
				break;
			lastSize = size;
			size = (size * 1.10) + 1;
		}
		heightChanged = false;
		font = new Font(((DTextModel) model).getFontName(), Font.PLAIN, (int) lastSize);
	}
	
	public void modelChanged(DShapeModel model) {
		heightChanged = true;
		super.modelChanged(model);
	}

}
