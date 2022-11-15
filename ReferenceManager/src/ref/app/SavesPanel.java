package ref.app;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ref.services.SavesService;

public class SavesPanel extends JPanel{
private SavesService sService = null;
public JTable savesTable = null;
	public SavesPanel(SavesService sService, int userid) {
	    this.sService = sService;
	    setLayout(new BorderLayout());
	    JScrollPane tablePane = generateSavesTable(userid);
	    add(tablePane, "Center");
	  }
	  private JScrollPane generateSavesTable(int userid) {
		  
		    this.savesTable = new JTable(new SavesTableModel(this.sService.getSaves(userid)));
		    this.savesTable.getColumnModel().getColumn(3).setWidth(0);
		    this.savesTable.getColumnModel().getColumn(3).setMinWidth(0);
		    this.savesTable.getColumnModel().getColumn(3).setMaxWidth(0);
		    JScrollPane scrollPane = new JScrollPane(this.savesTable);
		    this.savesTable.setFillsViewportHeight(true);
		    return scrollPane;
		  }
	   public void updateLists(int userid) {
		     this.savesTable.setModel(new SavesTableModel(this.sService.getSaves(userid)));
		     SavesPanel.this.savesTable.getColumnModel().getColumn(3).setWidth(0);
		     SavesPanel.this.savesTable.getColumnModel().getColumn(3).setMinWidth(0);
		     SavesPanel.this.savesTable.getColumnModel().getColumn(3).setMaxWidth(0);
		   }
}
