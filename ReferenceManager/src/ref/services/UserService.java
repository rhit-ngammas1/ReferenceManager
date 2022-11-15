package ref.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class UserService {
	private DatabaseConnectionService dbService = null;
	private static final Random RANDOM = new SecureRandom();
	private static final Base64.Encoder enc = Base64.getEncoder();
	private static final Base64.Decoder dec = Base64.getDecoder();
	
	public UserService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	public boolean useApplicationLogins() {
		return true;
	}

	public boolean AddUser(String uname, String fname, String lname, String specialty, String email, String institution, String position, String password) {
		try {
			CallableStatement stmt = dbService.getConnection().prepareCall("{? = call AddUser(?, ?, ?, ?, ?, ?, ?,?,?)}");
			byte[] salt = getNewSalt();
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, uname);
			stmt.setString(3, fname);
			stmt.setString(4, lname); 
			stmt.setString(5, specialty);
			stmt.setString(6, email);
			stmt.setString(7, institution);
			stmt.setString(8, position);
			stmt.setString(9, getStringFromBytes(salt));
			stmt.setString(10, hashPassword(salt, password));
			stmt.execute();
			int returnCode = stmt.getInt(1);
			if (returnCode == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: Institution does not exist");
				return false;
			} else if (returnCode == 2){
				JOptionPane.showMessageDialog(null, "ERROR: User already exists");
				return false;
			} else if (returnCode == 3){
				JOptionPane.showMessageDialog(null, "ERROR: Email is already in use");
				return false;
			} else if (returnCode == 4){
				JOptionPane.showMessageDialog(null, "ERROR: Invalid email");
				return false;
			} else {
				JOptionPane.showMessageDialog(null, "Successfully added user!");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean login(String username, String password) {
		String Hash = null;
		String Salt = null;
		try {
			String query = "SELECT UserName, PasswordSalt, PasswordHash FROM [User] WHERE UserName = ?";
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			String result = null;
			while(rs.next()) {
				result = rs.getString("UserName");
				Hash = rs.getString("PasswordHash");
				Salt = rs.getString("PasswordSalt");
			}
			if(result == null) {
				return false;
			}
			byte[] salt = dec.decode(Salt);
			String h = hashPassword(salt,password);
			System.out.println(Hash);
			if (Hash.equals(hashPassword(salt, password))) {
				return true;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<String> getInstitution(){
		ArrayList<String> institution = new ArrayList<String>();
		Connection connection = dbService.getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery("Select Name from Institution");
			while(results.next()) {
				institution.add(results.getString("Name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return institution;
	}
	
	public int getUserID(String username) {
		int id = -1;
		try {
			String query = "SELECT ID FROM [User] WHERE UserName = ?";
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				id = rs.getInt("ID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	public byte[] getNewSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	public String getStringFromBytes(byte[] data) {
		return enc.encodeToString(data);
	}

	public String hashPassword(byte[] salt, String password) {

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory f;
		byte[] hash = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		}
		return getStringFromBytes(hash);
	}
}
