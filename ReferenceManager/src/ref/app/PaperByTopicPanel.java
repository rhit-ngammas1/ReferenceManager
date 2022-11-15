
package ref.app;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ref.services.DatabaseConnectionService;
import ref.services.PaperService;
import ref.services.SavesService;
import ref.services.UserService;

public class PaperByTopicPanel extends JPanel {
	private JComboBox<String> topicComboBox = null;
	private PaperService pService = null;
	private JButton filterButton = null;
	public JTable paperByTopicTable = null;
	private JButton savesButton = null;
	private JButton citedByButton = null;
	private JButton notesButton = null;
	private SavesService sService = null;

	private int userid = -1;
	private DatabaseConnectionService dService = null;
	private Note n;

	public PaperByTopicPanel(PaperService pService, SavesService sService, int UserID, Note n) {
		this.pService = pService;
		this.sService = sService;
		this.userid = UserID;
		this.n = n;
		JPanel filterPanel = generateFilterUiItems();
		setLayout(new BorderLayout());
		add(filterPanel, "North");
		JScrollPane tablePane = generatePaperByTopicTable();
		add(tablePane, "Center");
		JPanel optionPanel = generateOptions(n);
		add(optionPanel, "South");
	}

	private JScrollPane generatePaperByTopicTable() {
		this.paperByTopicTable = new JTable(search());
		this.paperByTopicTable.getColumnModel().getColumn(3).setWidth(0);
		this.paperByTopicTable.getColumnModel().getColumn(3).setMinWidth(0);
		this.paperByTopicTable.getColumnModel().getColumn(3).setMaxWidth(0);
		JScrollPane scrollPane = new JScrollPane(this.paperByTopicTable);
		this.paperByTopicTable.setFillsViewportHeight(true);
		return scrollPane;
	}

	private JPanel generateFilterUiItems() {
		JPanel filterPanel = new JPanel();
		this.topicComboBox = new JComboBox<>();
		populateTopics();
		FlowLayout layout = new FlowLayout();
		layout.setHgap(15);
		filterPanel.setLayout(layout);
		filterPanel.add(new JLabel("Topic"));
		filterPanel.add(this.topicComboBox);
		this.filterButton = new JButton("Search");
		filterPanel.add(this.filterButton);
		this.filterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaperByTopicPanel.this.paperByTopicTable.setModel(PaperByTopicPanel.this.search());
				PaperByTopicPanel.this.paperByTopicTable.getColumnModel().getColumn(3).setWidth(0);
				PaperByTopicPanel.this.paperByTopicTable.getColumnModel().getColumn(3).setMinWidth(0);
				PaperByTopicPanel.this.paperByTopicTable.getColumnModel().getColumn(3).setMaxWidth(0);
			}
		});
		return filterPanel;
	}

	private JPanel generateOptions(Note n) {
		JPanel optionPanel = new JPanel();
		this.savesButton = new JButton("Saves");
		optionPanel.add(this.savesButton);
		this.citedByButton = new JButton("CitedBy");
		optionPanel.add(this.citedByButton);
		this.notesButton = new JButton("Notes");
		optionPanel.add(this.notesButton);
		this.savesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = PaperByTopicPanel.this.paperByTopicTable.getSelectedRow();
				String id = (String) PaperByTopicPanel.this.paperByTopicTable.getValueAt(row, 3);
				if(id == null) {
					JOptionPane.showMessageDialog(null, "Please select a paper");
					return;
				}
				int i = Integer.parseInt(id);

				PaperByTopicPanel.this.sService.addSave(PaperByTopicPanel.this.userid, i);
			}
		});
		this.citedByButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = PaperByTopicPanel.this.paperByTopicTable.getSelectedRow();
				String id = (String) PaperByTopicPanel.this.paperByTopicTable.getValueAt(row, 3);
				if(id == null) {
					JOptionPane.showMessageDialog(null, "Please select a paper");
					return;
				}
				int i = Integer.parseInt(id);
				String link = PaperByTopicPanel.this.pService.getCitedBy(i);
				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI(link));
					} catch (Exception ex) {
						System.out.println("error connecting: invalid uri");
					}
				}
			}
		});
		this.notesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = PaperByTopicPanel.this.paperByTopicTable.getSelectedRow();
				String id = (String) PaperByTopicPanel.this.paperByTopicTable.getValueAt(row, 3);
				if(id == null) {
					JOptionPane.showMessageDialog(null, "Please select a paper");
					return;
				}
				int i = Integer.parseInt(id);
				n.noteClicked(i, PaperByTopicPanel.this.userid);

			}
		});
		return optionPanel;
	}

	private PaperByTopicTableModel search() {
		String selectedTopic = (String) this.topicComboBox.getSelectedItem();
		String topicSearch = (selectedTopic == "None") ? null : selectedTopic;
		ArrayList<PaperByTopic> data = this.pService.getPapers(topicSearch);
		return new PaperByTopicTableModel(data);
	}

	private void populateTopics() {
		this.topicComboBox.addItem("None");
		ArrayList<String> tmp = new ArrayList<>();
		for (String s : this.pService.getTopic()) {
			if (tmp.contains(s)) {
				continue;
			}
			tmp.add(s);
			this.topicComboBox.addItem(s);
		}
	}

	public void updateLists() {
		String curTopic = (String) this.topicComboBox.getSelectedItem();
		this.topicComboBox.removeAllItems();
		populateTopics();
		findAndSelectItem(this.topicComboBox, curTopic);
		this.paperByTopicTable.setModel(search());
		PaperByTopicPanel.this.paperByTopicTable.getColumnModel().getColumn(3).setWidth(0);
		PaperByTopicPanel.this.paperByTopicTable.getColumnModel().getColumn(3).setMinWidth(0);
		PaperByTopicPanel.this.paperByTopicTable.getColumnModel().getColumn(3).setMaxWidth(0);
	}

	private void findAndSelectItem(JComboBox<String> box, String item) {
		for (int i = 0; i < box.getItemCount(); i++) {
			if (((String) box.getItemAt(i)).equals(item)) {
				box.setSelectedItem(box.getItemAt(i));
				return;
			}
		}
	}
}
