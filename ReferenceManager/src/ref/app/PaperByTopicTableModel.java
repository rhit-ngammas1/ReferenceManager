package ref.app;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class PaperByTopicTableModel extends AbstractTableModel {
	private ArrayList<PaperByTopic> data = null;
	private String[] columnNames = new String[] { "PaperName", "Topic", "YearPublished", "ID" };

	public PaperByTopicTableModel(ArrayList<PaperByTopic> data) {
		this.data = data;
	}

	public int getRowCount() {
		return this.data.size();
	}

	public int getColumnCount() {
		return this.columnNames.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex == -1)
			return null;
		return ((PaperByTopic) this.data.get(rowIndex)).getValue(this.columnNames[columnIndex]);
	}

	public String getColumnName(int index) {
		return this.columnNames[index];
	}
}
