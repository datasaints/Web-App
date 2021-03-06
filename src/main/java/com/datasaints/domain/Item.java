package com.datasaints.domain;

//import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;

public class Item {
	public enum Status {
		CHECKED_IN, CHECKED_OUT, NONE
	}
	
	private String id;
	private String owner;
	private int serial;
	private String itemName;
	private String location;
	private Status status;
	private Date lastCalibrated;
	private Timestamp checkTime;	// Date and time checked in or checked out 

   public Item() {
	   this.id = "";
	   this.owner = "";
	   this.serial = 0;
	   this.itemName = "";
	   this.location = "";
	   this.status = Status.CHECKED_IN;
	   this.lastCalibrated = null;
	   this.checkTime = null;
   }
   
   public Item(String id, String owner, int serial, String itemName,
		   String location, Status status, Date lastCalibrated, Timestamp checkTime) {
	   this.id = id;
	   this.owner = owner;
	   this.serial = serial;
	   this.itemName = itemName;
	   this.location = location;
	   this.status = status;
	   this.lastCalibrated = lastCalibrated;
	   this.checkTime = checkTime;
   }
   
   // Getters
   
   public String getId() {
	   return this.id;
   }
   
   public String getOwner() {
	   return this.owner;
   }
   
   public int getSerial() {
	   return this.serial;
   }
   
   public String getItemName() {
	   return this.itemName;
   }
   
   public String getLocation() {
	   return this.location;
   }
   
   public Status getStatus() {
	   return this.status;
   }
   
   public Date getLastCalibrated() {
	   return this.lastCalibrated;
   }
   
   public Timestamp getCheckTime() {
	   return this.checkTime;
   }
   
   // Setters
   
   public void setId(String id) {
	   this.id = id;
   }
   
   public void setOwner(String owner) {
	   this.owner = owner;
   }
   
   public void setSerial(int serial) {
	   this.serial = serial;
   }
   
   public void setItemName(String itemName) {
	   this.itemName = itemName;
   }
   
   public void setLocation(String location) {
	   this.location = location;
   }
   
   public void setStatus(Status status) {
	   this.status = status;
   }
   
   public void setLastCalibrated(Date lastCalibrated) {
	   this.lastCalibrated = lastCalibrated;
   }
   
   public void setCheckTime(Timestamp checkTime) {
	   this.checkTime = checkTime;
   }
}
