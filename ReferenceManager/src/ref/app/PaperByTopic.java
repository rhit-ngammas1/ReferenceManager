package ref.app;

public class PaperByTopic {
	private String papername;
	private String topic;
	private String yearPublished;
	private String ID;

	public PaperByTopic(String name, String topic, String yearpub, String ids) {
		this.papername = name;
		this.topic = topic;
		this.yearPublished = yearpub;
		this.ID = ids;
	}

	public String getValue(String propertyName) {
		String str;
		switch ((str = propertyName).hashCode()) {
		case -172532207:
			if (!str.equals("YearPublished"))
				break;
			return this.yearPublished;
		case 80993551:
			if (!str.equals("Topic"))
				break;
			return this.topic;
		case -1993115113:
			if (!str.equals("PaperName"))
				break;
			return this.papername;

		case 2331:
			if (!str.equals("ID"))
				break;
			return this.ID;
		}
		return null;
	}

	public String getPapername() {
		return this.papername;
	}

	public String getTopic() {
		return this.topic;
	}

	public String getYearPublished() {
		return this.yearPublished;
	}
}
