package ref.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ref.app.PaperByTopic;

public class PaperService {
	private DatabaseConnectionService dbService = null;

	public PaperService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public boolean addPaper(int authorid, String name, String topic, Date YearPublished) {
		try {
			CallableStatement stmt = dbService.getConnection().prepareCall("{? = call AddPaper(?, ?, ?, ?)}");
			int i = 1;
			stmt.registerOutParameter(i++, Types.INTEGER);
			stmt.setInt(i++, authorid);
			stmt.setString(i++, name);
			stmt.setString(i++, topic);
			stmt.setDate(i++, YearPublished);
			stmt.execute();
			int returnCode = stmt.getInt(1);
			if (returnCode == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: Invalid authorID");
			} else if (returnCode == 2) {
				JOptionPane.showMessageDialog(null, "ERROR: Invalid date");
			} else {
				JOptionPane.showMessageDialog(null, "Successfully added note!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Paper getPaperByID(int paperID) {
		Paper p = new Paper("", "", "", "");
		String query = "SELECT [Topic], [ID], [Name], [Link] "
				+ "FROM Paper "
				+ "WHERE Paper.ID = ?";
		try {
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			stmt.setInt(1, paperID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String topic = rs.getString("Topic");
				String id = rs.getString("ID");
				String title = rs.getString("Name");
				String link = rs.getString("Link");
				p = new Paper(topic, id, title, link);
			}
			return p;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public ArrayList<PaperByTopic> getPapers(String topic) {
		//TODO: Task 3 and Task 4
		try {
			String query = "SELECT Name, Topic, YearPublished, ID \nFROM Paper\n";
			if (topic != null) {
				query += "where Topic = ?";
			}
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			int i = 1;
			if(topic!=null) {
				stmt.setString(i++, topic);
			}
			ResultSet rs = stmt.executeQuery();
			return parseResults(rs);
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to retrieve paper.");
			ex.printStackTrace();
			return new ArrayList<PaperByTopic>();
		}
	}
	private ArrayList<PaperByTopic> parseResults(ResultSet rs) {
		try {
			ArrayList<PaperByTopic> paperByTopic = new ArrayList<PaperByTopic>();
			int paperNameIndex = rs.findColumn("Name");
			int topicIndex = rs.findColumn("Topic");
			int yearPublishedIndex = rs.findColumn("YearPublished");
			int IDIndex = rs.findColumn("ID");
			while (rs.next()) {
				paperByTopic.add(new PaperByTopic(rs.getString(paperNameIndex), rs.getString(topicIndex),
						rs.getString(yearPublishedIndex),rs.getString(IDIndex)));
			}
			System.out.println(paperByTopic.size());
			return paperByTopic;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"An error ocurred while retrieving paper.");
			ex.printStackTrace();
			return new ArrayList<PaperByTopic>();
		}

	}
	public ArrayList<String> getTopic() {	
		//TODO: Task 2 
		ArrayList<String> topic = new ArrayList<String>();
		Connection connection = dbService.getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("Select Topic from Paper");
			while(results.next()) {
				topic.add(results.getString("Topic"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return topic;
	}
	public String getCitedBy(int Cited) {	
		//TODO: Task 2 
		try {
			String query = "Select CitedByLink from Paper where ID = ?";
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			int i = 1;
				stmt.setInt(i++, Cited);
			ResultSet rs = stmt.executeQuery();
			return parseResult(rs);
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to retrieve cited by links.");
			ex.printStackTrace();
			return new String();
		}
	}
	private String parseResult(ResultSet rs) {
		try {
			ArrayList<String> citedByLink = new ArrayList<String>();
			int citedByLinkIndex = rs.findColumn("CitedByLink");
			while (rs.next()) {
				citedByLink.add(rs.getString(citedByLinkIndex));
			}
			System.out.println(citedByLink.size());
			return citedByLink.get(0);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"An error ocurred while retrieving sodas by restaurants. See printed stack trace.");
			ex.printStackTrace();
			return "";
		}
	}
}
