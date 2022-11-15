package ref.app;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import ref.services.Note;
import ref.services.NoteService;
import ref.services.PaperService;

public class PaperNoteViewFrame extends JFrame{
	
	public NoteService ns;
	public PaperService ps;
	private JButton editNoteButton = null;
	private JButton addNoteButton = null;
	private JButton deletNoteButton = null;
	private JTable notesDisplay = null;
	private int paperID = 0;
	private int userID = 0;
	private JScrollPane scrollPane = null;
	private NoteTableModel model = null;
	
	public PaperNoteViewFrame(int paperID, int userID, PaperService pService, NoteService nService) {
//		System.out.println("dcs: " + dcs.getConnection());
		this.ns = nService;
		this.ps = pService;
		this.paperID = paperID;
		this.userID = userID;
		//end
		Engine engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
                .licenseKey("1BNDHFSC1G1T7A568FKNSNOAGXMDSSNPDD7Q5F4V1R3YAVMEP1CHG88SRQRFMEHJRZQJ5F ")
                .build());
		Browser browser = engine.newBrowser();

	
		
		String paperlink = ps.getPaperByID(paperID).link;
//		System.out.println(ps.getPaperByID(paperID).papertitle);
//		System.out.println("link: " + paperlink);
		
		
		JFrame frame = new JFrame();
		frame.setSize( 1380, 960 );
		browser.navigation().loadUrl(paperlink);
//		browser.navigation().loadUrl("https://books.google.com/books?hl=en&lr=&id=vhvqCAAAQBAJ&oi=fnd&pg=PR9&dq=Evolution+and+Development&ots=PAtCibmv6h&sig=zHm7Yr72iSW5imjy__S3-23dz2A%22");
		
		BrowserView view = BrowserView.newInstance(browser);
		scrollPane = generateNoteTable();
		JPanel noteWithButtons = new JPanel();
		JPanel buttons = nodeButtonPanel(paperID,userID);
		noteWithButtons.add(scrollPane);
		noteWithButtons.add(buttons);
		
		frame.setVisible(true);
		frame.add(noteWithButtons, BorderLayout.EAST);
		frame.add(view, BorderLayout.WEST);
		
	}
	
	private JPanel nodeButtonPanel(int paperID, int userID) {
		JPanel buttonPanel = new JPanel();
		this.editNoteButton = new JButton("Edit");
		buttonPanel.add(this.editNoteButton);
		this.addNoteButton = new JButton("Add");
		buttonPanel.add(this.addNoteButton);
		this.deletNoteButton = new JButton("Delete");
		buttonPanel.add(this.deletNoteButton);
		GridLayout layout = new GridLayout(3, 1);
		layout.setHgap(30);
		layout.setVgap(30);
		buttonPanel.setLayout(layout);
		this.editNoteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = PaperNoteViewFrame.this.notesDisplay.getSelectedRow();
				String id = (String) PaperNoteViewFrame.this.notesDisplay.getValueAt(row, 1);
				if(id == null) {
					JOptionPane.showMessageDialog(null, "Invalid note");
					return;
				}
				String oldText = (String) PaperNoteViewFrame.this.notesDisplay.getValueAt(row, 0);
				int i = Integer.parseInt(id);
				JFrame edit = new EditeNoteFrame(ns, paperID, userID, i, PaperNoteViewFrame.this, oldText);
				edit.setDefaultCloseOperation(3);
			}
		});
		this.addNoteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame add = new AddNoteFrame(ns, paperID, userID, PaperNoteViewFrame.this);
				add.setDefaultCloseOperation(3);
			}
		});
		this.deletNoteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = PaperNoteViewFrame.this.notesDisplay.getSelectedRow();
				String id = (String) PaperNoteViewFrame.this.notesDisplay.getValueAt(row, 1);
				if(id == null) {
					JOptionPane.showMessageDialog(null, "Invalid note");
					return;
				}
				int i = Integer.parseInt(id);
				ns.deleteNote(i);
				updateTable();
			}
		});
		return buttonPanel;
		
	}
	
	private JScrollPane generateNoteTable() {
		this.model = new NoteTableModel(getData(this.userID, this.paperID));
		this.notesDisplay = new JTable(this.model);
		this.notesDisplay.getColumnModel().getColumn(1).setWidth(0);
		this.notesDisplay.getColumnModel().getColumn(1).setMinWidth(0);
		this.notesDisplay.getColumnModel().getColumn(1).setMaxWidth(0);
		JScrollPane scrollPane = new JScrollPane(this.notesDisplay);
		this.notesDisplay.setFillsViewportHeight(true);
		return scrollPane;
	}
	
	
	private String[][] getData(int userID, int paperID){
		ArrayList<Note> notesFromQuery = ns.getNotes(userID, paperID);
		System.out.println(notesFromQuery);
		String[][] notedata = new String[notesFromQuery.size()][2];
		for (int i = 0; i < notesFromQuery.size(); i++) {
			notedata[i][0] = notesFromQuery.get(i).data;
			notedata[i][1] = String.valueOf(notesFromQuery.get(i).noteID);
		}
		return notedata;
	}
	
	public void updateTable() {
		model = new NoteTableModel(getData(userID, paperID));
		notesDisplay.setModel(model);
		notesDisplay.getColumnModel().getColumn(1).setWidth(0);
		notesDisplay.getColumnModel().getColumn(1).setMinWidth(0);
		notesDisplay.getColumnModel().getColumn(1).setMaxWidth(0);
	}

	
	
}