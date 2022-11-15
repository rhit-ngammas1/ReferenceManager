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

public class EditeNoteFrame extends JFrame {
	public NoteService ns;
	private JTextField newText = null;
	private JButton edit = null;
	private PaperNoteViewFrame pf = null;
 
	public EditeNoteFrame(NoteService ns, int paperID, int userID, int noteID, PaperNoteViewFrame paperNoteViewFrame, String oldText) {
		JFrame frame = new JFrame();
		this.pf = paperNoteViewFrame;
		this.ns = ns;
		JPanel panel = new JPanel();
		this.newText = new JTextField();
		this.newText.setText(oldText);
		GridLayout layout = new GridLayout(2, 2);
		layout.setHgap(50);
		layout.setVgap(50);
		panel.setLayout(layout);
		panel.add(new JLabel("Edit Text:"));
		panel.add(this.newText);
		this.edit = new JButton("Edit");
		this.edit.setPreferredSize(new Dimension(150, 100));
		panel.add(this.edit);
		frame.add(panel);
		frame.pack();
		this.edit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(userID);
				ns.editNote(userID, paperID, noteID, EditeNoteFrame.this.newText.getText());
				pf.updateTable();
				frame.setVisible(false);
				frame.dispose();
			}
		});
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
