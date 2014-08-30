package edw.olingo.model;

import javax.persistence.*;

@Entity(name = "MeetingTitle")
@Table(name = "informea_meetings_title")
public class MeetingTitle {

	@Id
    @Column(name = "id")
	private String id;

	private String language;
	@Column(length=400, name="title")
	private String value;

	private Meeting meeting;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

}
