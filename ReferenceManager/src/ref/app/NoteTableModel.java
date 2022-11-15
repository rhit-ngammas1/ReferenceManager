package ref.app;

import javax.swing.table.AbstractTableModel;

public class NoteTableModel extends AbstractTableModel{
	private String[][] data = null;
	private String[] columnNames = new String[] { "Note","ID" };

	public NoteTableModel(String[][] data) {
		this.data = data;
	}
	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex == -1)
			return null;
		return (this.data[rowIndex][columnIndex]);
	}
	
	public String getColumnName(int col) {
	      return columnNames[col];
	    }

}
