package ref.services;

public class Note {
	public int noteID;
	public int userID;
	public int paperID;
	public String data;
	public Note(int noteID, int userID, int paperID, String data) {
		this.noteID = noteID;
		this.userID = userID;
		this.paperID = paperID;
		this.data = data;
	}
	
}
