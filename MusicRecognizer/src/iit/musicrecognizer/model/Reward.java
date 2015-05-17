package iit.musicrecognizer.model;

import java.util.Date;

public class Reward {
	
	private String rewardID;
	private String name;
	private String desc;
	private String image;
	private int value;
	private int quantity;
	private Date date_added;
	
	
	public String getRewardID() {
		return rewardID;
	}
	public void setRewardID(String rewardID) {
		this.rewardID = rewardID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Date getDate_added() {
		return date_added;
	}
	public void setDate_added(Date date_added) {
		this.date_added = date_added;
	}
	
	

}
