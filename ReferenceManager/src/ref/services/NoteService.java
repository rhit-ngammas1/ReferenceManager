package ref.services;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class NoteService {
	private DatabaseConnectionService dbService = null;
	
	public NoteService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	
	public boolean addNote(int userid, int paperid, String noteText) { 
		try {
			CallableStatement stmt = dbService.getConnection().prepareCall("{? = call AddNotes(?, ?, ?)}");
			int i = 1;
			stmt.registerOutParameter(i++, Types.INTEGER);
			stmt.setInt(i++, userid);
			stmt.setInt(i++, paperid);
			stmt.setString(i++, noteText);
			stmt.execute();
			int returnCode = stmt.getInt(1);
			if (returnCode == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: PaperID can''t be null");
			} else if (returnCode == 2){
				JOptionPane.showMessageDialog(null, "ERROR: UserID can''t be null");
			} else if (returnCode == 3){
				JOptionPane.showMessageDialog(null, "ERROR: UserID not exist");
			} else if (returnCode == 4){
				JOptionPane.showMessageDialog(null, "ERROR: PaperID not exist");
			} else if (returnCode == 5){
				JOptionPane.showMessageDialog(null, "ERROR: Some notes need to be added");
			} else {
				JOptionPane.showMessageDialog(null, "Successfully added note!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean editNote(int userid, int paperid, int noteid, String noteText) {
		try {
			CallableStatement stmt = dbService.getConnection().prepareCall("{? = call EditNote(?, ?, ?, ?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, userid);
			stmt.setInt(3, paperid);
			stmt.setInt(4, noteid);
			stmt.setString(5, noteText);
			stmt.execute();
			int returnCode = stmt.getInt(1);
			if (returnCode == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: User does not exist");
			} else if (returnCode == 2){
				JOptionPane.showMessageDialog(null, "ERROR: Paper does not exist");
			} else if (returnCode == 3){
				JOptionPane.showMessageDialog(null, "ERROR: Note does not exist");
			} else if (returnCode == 4){
				JOptionPane.showMessageDialog(null, "ERROR: Current user does not have permission to edit this note");
			} else {
				JOptionPane.showMessageDialog(null, "Successfully edited note!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteNote (int id) {
		try {
			CallableStatement stmt = dbService.getConnection().prepareCall("{? = call DeleteNotes(?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, id);
			stmt.execute();
			int returnCode = stmt.getInt(1);
			if (returnCode == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: ID not exist");
			} else if (returnCode == 2){
				JOptionPane.showMessageDialog(null, "ERROR: ID cannot be null");
			} else {
				JOptionPane.showMessageDialog(null, "Successfully delete note!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Note> getNotes(int userID, int paperID) {
		ArrayList<Note> notes = new ArrayList<Note>();

		String query = "SELECT [Text], [ID] FROM [Notes] WHERE UserID = ? AND PaperID = ?";

		PreparedStatement stmt;
		try {
			stmt = this.dbService.getConnection().prepareStatement(query);
			int i = 1;
				stmt.setInt(i++, userID);
				stmt.setInt(i++, paperID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String ndata = rs.getString(rs.findColumn("Text"));
				int npaperID = paperID;
				int nuserID = userID;
				int nnoteID = rs.getInt(rs.findColumn("ID"));
				Note newNote = new Note(nnoteID, nuserID, npaperID, ndata);
				notes.add(newNote);
			}
			return notes;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return notes;
	}
}
