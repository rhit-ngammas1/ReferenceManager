
package ref.app;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ref.services.PaperService;
import ref.services.SavesService;
import ref.services.UserService;

public class Frame extends JFrame {
	private JMenu menu;
	private JMenuItem paperList;
	private JMenuItem saveList;



	private PaperByTopicPanel listPanel;
	private JPanel containerPanel;
	private SavesPanel savesPanel;
	private JPanel currentPanel = null;
	private int userid = -1;

	public Frame(PaperService pService, SavesService sService, int UserID, Note n) {
		setSize(500, 500);

		setResizable(false);
		setTitle("Reference Manager");
		this.containerPanel = new JPanel();
		this.userid = UserID;
		this.listPanel = new PaperByTopicPanel(pService, sService, UserID, n);
		this.savesPanel = new SavesPanel(sService, UserID);
		this.savesPanel.setVisible(false);
		this.currentPanel = this.listPanel;
		this.menu = new JMenu("Menu");
		this.paperList = new JMenuItem("Paper List");
		this.paperList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Frame.this.switchToPaperList();
			}
		});
		this.saveList = new JMenuItem("Saved List");
		this.saveList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Frame.this.switchToSaveList();
			}
		});
		this.menu.add(this.paperList);
		this.menu.add(this.saveList);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(this.menu);
		add(menuBar, "North");
		add(this.containerPanel, "Center");
		this.containerPanel.add(this.listPanel);
		this.containerPanel.add(this.savesPanel);
		setVisible(true);
		pack();
	}

	private void switchToPaperList() {
		if (this.listPanel == this.currentPanel) {
			return;
		}
		this.currentPanel = this.listPanel;
		this.listPanel.updateLists();
		switchPanel(this.savesPanel);
	}

	private void switchPanel(Component toRemove) {
		toRemove.setVisible(false);
		this.currentPanel.setVisible(true);
	}

	private void switchToSaveList() {
		if (this.savesPanel == this.currentPanel) {
			return;
		}
		this.currentPanel = this.savesPanel;
		this.savesPanel.updateLists(this.userid);
		switchPanel(this.listPanel);
	}
}
