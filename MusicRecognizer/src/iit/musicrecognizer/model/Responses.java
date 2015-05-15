package iit.musicrecognizer.model;

public class Responses {
	
	private int responseID;
	private String username;
	private Tune t;
	private User user;
	private String artist;
	private String title;
	private String status;
	private String Date;
	
	public Responses() {
		super();
	}
	
	public int getResponseID() {
		return responseID;
	}
	public void setResponseID(int responseID) {
		this.responseID = responseID;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Tune getT() {
		return t;
	}
	public void setT(Tune t) {
		this.t = new Tune();
		this.t = t;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = new User();
		this.user = user;
	}

	@Override
	public String toString() {
		return "Responses [responseID=" + responseID + ", username=" + username
				+ ", t=" + t + ", user=" + user + ", artist=" + artist
				+ ", title=" + title + ", status=" + status + ", Date=" + Date
				+ "]";
	}
	
	
}
