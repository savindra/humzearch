package iit.musicrecognizer.model;

import java.util.Date;

public class Tune {
	private String tuneID;
	private String artist = "";
	private String language = "";
	private int year;
	private String country = "";
	private String date_added;
	private String author;
	private String authorImgUrl;
	
	public Tune(String tuneID, String artist, String language, int year,
			String country, String date_added, String author, String authorImgUrl) {
		super();
		this.tuneID = tuneID;
		this.artist = artist;
		this.language = language;
		this.year = year;
		this.country = country;
		this.date_added = date_added;
		this.author = author;
		this.authorImgUrl = authorImgUrl;
	}

	public Tune() {
	}

	public String getTuneID() {
		return tuneID;
	}

	public void setTuneID(String tuneID) {
		this.tuneID = "Tune ID: " + tuneID;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDate_added() {
		return date_added;
	}

	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorImgUrl() {
		return authorImgUrl;
	}

	public void setAuthorImgUrl(String authorImgUrl) {
		this.authorImgUrl = authorImgUrl;
	}
	
	
	

}
