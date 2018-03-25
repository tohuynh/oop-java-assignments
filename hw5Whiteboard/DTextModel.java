import java.awt.Color;

public class DTextModel extends DShapeModel {
	private String text;
	private String fontName;

	public DTextModel() {
		this(0,0, 10, 10);
	}

	public DTextModel(int x, int y, int width, int height) {
		super(x, y, width, height, Color.GRAY);
		this.text = "Hello";
		this.fontName = "Dialog";
	}

	@Override
	public void mimic(DShapeModel other) {
		super.mimic(other);
		setText(((DTextModel) other).getText());
		setFontName(((DTextModel) other).getFontName());
		System.out.println("called dtextmodel mimic");
		notifyListeners();
	}

	public String getText() {
		return text;
	}

	public String getFontName() {
		return fontName;
	}

	public void setText(String text) {
		this.text = text;
		notifyListeners();
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
		notifyListeners();
	}

}
