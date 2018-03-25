import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class WhiteboardTableModel extends AbstractTableModel  implements ModelListener {
	public static final String[] COLUMN_NAMES = {"X", "Y", "Width", "Height", "ID"};
	private List<DShapeModel> models;
	
	public WhiteboardTableModel() {
		super();
		models = new ArrayList<DShapeModel>();
	}

	public void modelChanged(DShapeModel model) {
		int rowIndex = models.indexOf(model);
		if (rowIndex > -1) {
			fireTableRowsUpdated(rowIndex, rowIndex);
		}
		
	}

	public int getRowCount() {
		return models.size();
	}

	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}
	
	public List<DShapeModel> getModels() {
		return models;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Rectangle bounds = models.get(rowIndex).getBounds();
		switch (columnIndex) {
			case 0: return bounds.x;
			case 1: return bounds.y;
			case 2: return bounds.width;
			case 3: return bounds.height;
			case 4: return models.get(rowIndex).getId();
			default: return null;
		}
	}

	public void addModel(DShapeModel model) {
		models.add(model);
		model.addListener(this);
		fireTableDataChanged();
		
	}
	
	public void removeModel(DShapeModel model) {
		model.doRemoveListener(this);
		models.remove(model);
		fireTableDataChanged();
	}
	
	public void moveModelTo(DShapeModel model, int index) {
		int rowIndex = models.indexOf(model);
		if (rowIndex > -1) {
			Collections.swap(models, rowIndex, index);
			fireTableDataChanged();
		}
	}
	
	@Override
    public String getColumnName(int colIndex) {
        return COLUMN_NAMES[colIndex];
        
	}
}
