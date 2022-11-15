package ref.services;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;
public class InstitutionService {
	private DatabaseConnectionService dbService = null;
	
	public InstitutionService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	public boolean addInstitution(String name, String location) { 
		try {
			CallableStatement stmt = dbService.getConnection().prepareCall("{? = call AddInstitution(?, ?)}");
			int i = 0;
			stmt.registerOutParameter(i++, Types.INTEGER);
			stmt.setString(i++, name);
			stmt.setString(i++, location);
			stmt.execute();
			int returnCode = stmt.getInt(1);
			if (returnCode == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: Name cannot be null");
			} else if (returnCode == 2){
				JOptionPane.showMessageDialog(null, "ERROR: Location cannot be null");
			} else {
				JOptionPane.showMessageDialog(null, "Successfully added note!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
