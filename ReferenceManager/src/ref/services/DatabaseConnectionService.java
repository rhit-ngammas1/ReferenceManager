package ref.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionService {
	private final String SampleURL = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${password}}";

	private Connection connection = null;

	private String databaseName;
	private String serverName;

	public DatabaseConnectionService(String serverName, String databaseName) {
		this.serverName = serverName;
		this.databaseName = databaseName;
	}

	public boolean connect(String user, String pass) {
		//Build connection string
		String finalUrl = SampleURL 
				.replace("${dbServer}", "titan.csse.rose-hulman.edu")
				.replace("${dbName}", "ResearchReferenceManager")
				.replace("${user}", user)
				.replace("${password}", pass);
		System.out.println(finalUrl);
		try {
			//create connection to database
			connection = DriverManager.getConnection(finalUrl);
			System.out.println("Connected successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connection failed");
		return false;
	}
	

	public Connection getConnection() {
		return this.connection;
	}
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Connection closed");
		}
	}

}
