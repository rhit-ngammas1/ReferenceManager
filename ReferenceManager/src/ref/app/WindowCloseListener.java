package ref.app;

 import java.awt.event.WindowEvent;
 import java.awt.event.WindowListener;

	import ref.services.DatabaseConnectionService;
 
 public class WindowCloseListener
   implements WindowListener
 {
   private DatabaseConnectionService dbService;
   
   public WindowCloseListener(DatabaseConnectionService dbService) {
     this.dbService = dbService;
   }
 
 
 
   
   public void windowOpened(WindowEvent e) {}
 
 
   
   public void windowClosing(WindowEvent e) {
     this.dbService.closeConnection();
   }
   
   public void windowClosed(WindowEvent e) {}
   
   public void windowIconified(WindowEvent e) {}
   
   public void windowDeiconified(WindowEvent e) {}
   
   public void windowActivated(WindowEvent e) {}
   
   public void windowDeactivated(WindowEvent e) {}
 }
