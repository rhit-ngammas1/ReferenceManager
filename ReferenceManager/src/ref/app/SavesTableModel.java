package ref.app;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class SavesTableModel extends AbstractTableModel{
	private ArrayList<Saves> data = null;
	private String[] columnNames = new String[] { "PaperName", "Topic", "YearPublished","ID"};
	public SavesTableModel(ArrayList<Saves> data) {
	    this.data = data;
	  }  
	 public int getRowCount() {
	   return this.data.size();
	  }

	 
	  public int getColumnCount() {
	    return this.columnNames.length;
	   }
	  public Object getValueAt(int rowIndex, int columnIndex) {
	     return ((Saves)this.data.get(rowIndex)).getValue(this.columnNames[columnIndex]);
	 }

	 
	   public String getColumnName(int index) {
	     return this.columnNames[index];
	 }
}
