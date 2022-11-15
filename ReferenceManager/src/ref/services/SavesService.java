package ref.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ref.app.Saves;
import ref.app.Saves;
import ref.services.DatabaseConnectionService;

public class SavesService {
	private DatabaseConnectionService dbService = null;
	
	public SavesService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	
	public boolean addSave(int UserID, int PaperID) {
		try {
			CallableStatement cs = this.dbService.getConnection().prepareCall("{? = call addSaves(?,?)}");
			int i = 1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, UserID);
			cs.setInt(i++, PaperID);
			cs.execute();
			int returnCode = cs.getInt(1);
			if (returnCode == 0) {
				JOptionPane.showMessageDialog(null, "Success!");
			}
			else if(returnCode == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: user id not exist");
			}
			else if(returnCode == 2) {
				JOptionPane.showMessageDialog(null, "ERROR: user id cannot be null");
			}
			else if(returnCode == 3) {
				JOptionPane.showMessageDialog(null, "ERROR: paper id not exist");
			}
			else if(returnCode == 4) {
				JOptionPane.showMessageDialog(null, "ERROR: paper id cannot be null");
			}
			else if(returnCode == 5) {
				JOptionPane.showMessageDialog(null, "ERROR: already saves");
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	

	public ArrayList<Saves> getSaves(int userid) {	

		ArrayList<String> saves = new ArrayList<String>();
		Connection connection = dbService.getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("Select UserID, PaperID from Saves");
			while(results.next()) {
				saves.add(results.getString("UserID"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String query = "select p.Name as PaperName, p.Topic, p.YearPublished, p.ID\r\n"
					+ "from Saves s\r\n"
					+ "join Paper p on p.ID = s.PaperID\r\n"
					+ "where s.UserID = ?";
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			int i = 1;
				stmt.setInt(i++, userid);
			ResultSet rs = stmt.executeQuery();
			return parseResults(rs);
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to retrieve paper.");
			ex.printStackTrace();
			return new ArrayList<Saves>();
		}
		
	}
	private ArrayList<Saves> parseResults(ResultSet rs) {
		try {
			ArrayList<Saves> saves = new ArrayList<Saves>();
			int paperNameIndex = rs.findColumn("PaperName");
			int topicIndex = rs.findColumn("Topic");
			int yearPublishedIndex = rs.findColumn("YearPublished");
			int IDIndex = rs.findColumn("ID");
			while (rs.next()) {
				saves.add(new Saves(rs.getString(paperNameIndex), rs.getString(topicIndex),
						rs.getString(yearPublishedIndex),rs.getString(IDIndex)));
			}
			System.out.println(saves.size());
			return saves;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"An error ocurred while retrieving paper.");
			ex.printStackTrace();
			return new ArrayList<Saves>();
		}
	}
}
