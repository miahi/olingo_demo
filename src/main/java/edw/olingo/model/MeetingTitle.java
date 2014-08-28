package edw.olingo.model;

import javax.persistence.*;

@Entity(name = "MeetingTitle")
@Table(name = "informea_meetings_title")
public class MeetingTitle extends LocalizableString {

	@Id
    @Column(name = "id")
	private String id;

	private String language;
	@Column(length=400)
	private String title;

//	@ManyToOne
//	@JoinColumn(name = "meeting_id", referencedColumnName = "id")
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

}
