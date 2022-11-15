
package ref.app;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPrompt extends JFrame
 {
   private JTextField usernameBox = null;
   private JTextField passwordBox = null;
   private JButton loginButton = null;
   private JButton registerButton = null;
   
   public LoginPrompt(Login complete) {
     add(getPanel(complete));
     pack();
     setDefaultCloseOperation(3);
     setVisible(true);
   }
   
   private JPanel getPanel(final Login complete) {
     JPanel panel = new JPanel();
     this.usernameBox = new JTextField();
     this.passwordBox = new JPasswordField();
     GridLayout layout = new GridLayout(3, 2);
     layout.setHgap(25);
     layout.setVgap(25);
     panel.setLayout(layout);
     panel.add(new JLabel("Username:"));
     panel.add(this.usernameBox);
     panel.add(new JLabel("Password:")); 
     panel.add(this.passwordBox);
     this.loginButton = new JButton("Login");
    this.registerButton = new JButton("Register");
     
    
     this.loginButton.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e) {
             complete.login(LoginPrompt.this.usernameBox.getText(), LoginPrompt.this.passwordBox.getText());
           }
         });
     
     this.registerButton.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e) {
    	   complete.register();
    	   setVisible(false);
    	   dispose();
       }
     });
     

    panel.add(this.loginButton);
     panel.add(this.registerButton);
    return panel;
   }
 }