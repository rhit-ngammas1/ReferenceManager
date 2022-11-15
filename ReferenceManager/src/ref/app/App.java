
package ref.app;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ref.services.DatabaseConnectionService;
import ref.services.NoteService;
import ref.services.PaperService;
import ref.services.SavesService;
import ref.services.UserService;

public class App {
	private String serverUsername = null;
	private String serverPassword = null;
	private DatabaseConnectionService dbService = null;
	private LoginPrompt lp = null;
	private WindowCloseListener wcl = null;
	private Frame f =null;
	
	public void runApplication(){
		this.serverUsername = "zhuz9";
		this.serverPassword = "zhz028338";
		this.dbService = new DatabaseConnectionService("titan.csse.rose-hulman.edu", "ResearchReferenceManager");
		UserService userService = new UserService(this.dbService);
		this.wcl = new WindowCloseListener(this.dbService);
		Note n = new Note()
		{
		public void noteClicked(int paperid, int userid) {
			App.this.noteClickedHelper(paperid, userid);	
			
		}
			
				};
		 Login lc = new Login()
		 {
         public void login(String u, String p) {
	           if (userService.login(u, p)) {
		           App.this.loginSucceeded(userService.getUserID(u),n);
			          } else {
				           JOptionPane.showMessageDialog(null, "Login unsuccessful.");
			          } 
			        }
			
				       
			       public void register() {
			    	   newUser(userService, wcl,n);
			       }
			     };
		if(!this.dbService.connect(serverUsername, serverPassword)) {
			JOptionPane.showMessageDialog(null, "Connection to database could not be made.");
		}else if (userService.useApplicationLogins()){
			this.lp = new LoginPrompt(lc);
			this.lp.addWindowListener(this.wcl);
		}
		else {
			loginSucceeded(1,n);
		}

	}
	
	public void loginSucceeded(int UserID,Note n) {
		if (this.lp != null) {
		    this.lp.setVisible(false);
	        this.lp.dispose();
	        } 
		PaperService pService = new PaperService(this.dbService);
		SavesService sService = new SavesService(this.dbService);
		JFrame newframe = new Frame(pService,sService,UserID,n);
		newframe.setDefaultCloseOperation(3);
		newframe.addWindowListener(this.wcl);
	}
	public void noteClickedHelper(int paperid, int userid) {
		PaperService pService = new PaperService(this.dbService);
		NoteService nService = new NoteService(this.dbService);
		JFrame noteframe = new PaperNoteViewFrame(paperid,userid,pService,nService);
		noteframe.setDefaultCloseOperation(3);
		noteframe.addWindowListener(this.wcl);
	}
	
	public void newUser(UserService u, WindowCloseListener wcl2,Note n) {
		JFrame frame = new JFrame("Register");
		
		JPanel p = new JPanel();
		JTextField uname = new JTextField();
		JTextField password = new JPasswordField(); 
		JTextField fname = new JTextField();
		JTextField lname = new JTextField();
		JTextField specialty = new JTextField();
		JTextField email = new JTextField();
		JTextField position = new JTextField();
		JComboBox<String> institution = new JComboBox<>();
		populateInstitution(u, institution);
		JButton register = new JButton("Register");
		GridLayout layout = new GridLayout(9, 2);
		layout.setHgap(30);
		layout.setVgap(30);
		p.setLayout(layout);
		p.add(new JLabel("UserName: "));
		p.add(uname);
		p.add(new JLabel("Password: "));
		p.add(password);
		p.add(new JLabel("FirstName: "));
		p.add(fname);
		p.add(new JLabel("LastName: "));
		p.add(lname);
		p.add(new JLabel("Specialty: "));
		p.add(specialty);
		p.add(new JLabel("Email: "));
		p.add(email);
		p.add(new JLabel("Institution: "));
		p.add(institution);
		p.add(new JLabel("Position: "));
		p.add(position);
		register.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean result;
				result = u.AddUser(uname.getText(), fname.getText(), lname.getText(), specialty.getText(), email.getText(), 
						institution.getSelectedItem().toString(), position.getText(), password.getText());
				if(result) {
					String ID = uname.getText();
					loginSucceeded(u.getUserID(ID),n);
					frame.setVisible(false);
					frame.dispose();
				}else {
					JOptionPane.showMessageDialog(null, "Register unsuccessful.");
				}
				
			}
		});
		p.add(register);
		frame.add(p);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(wcl2);
	}
	private void populateInstitution(UserService u ,JComboBox<String> box) {
	     box.addItem("None");
		 ArrayList<String> tmp = new ArrayList<>();
	     for (String s : u.getInstitution()) {
					if(tmp.contains(s)) {
						continue;
					}
					tmp.add(s);
	     box.addItem(s);
	     }
	   }
}

