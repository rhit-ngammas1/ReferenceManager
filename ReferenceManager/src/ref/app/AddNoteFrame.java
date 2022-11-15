package ref.app;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import ref.services.NoteService;

public class AddNoteFrame extends JFrame {
	public NoteService ns;
	private JTextField newText = null;
	private JButton add = null;
	private PaperNoteViewFrame pf = null;
	
	public AddNoteFrame(NoteService ns, int paperID, int userID, PaperNoteViewFrame paperNoteViewFrame) {
		JFrame frame = new JFrame();
		this.ns = ns;
		this.pf = paperNoteViewFrame;
		JPanel panel = new JPanel();
		this.newText = new JTextField();
		GridLayout layout = new GridLayout(2, 2);
		layout.setHgap(50);
		layout.setVgap(50);
		panel.setLayout(layout);
		panel.add(new JLabel("New Text:"));
		panel.add(this.newText);
		this.add = new JButton("Add");
		this.add.setPreferredSize(new Dimension(150, 50));
		panel.add(this.add);
		this.add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ns.addNote(userID, paperID, newText.getText());
				pf.updateTable();
				frame.setVisible(false);
				frame.dispose();
			}
		});
		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
