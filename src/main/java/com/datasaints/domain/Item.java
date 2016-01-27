package com.datasaints.domain;

//import java.sql.Time;
import java.util.Date;

public class Item {
	private String itemId;
	private int employeeId;
	private String itemName;
	private Date checkIn;
	private Date checkOut;
	private Date lastCalibrated;
	
	public Item(String itemID, int employeeID, String name, Date checkIn,
			Date checkOut, Date lastCalibrated) {
		this.itemId = itemID;
		this.employeeId = employeeID;
		this.itemName = name;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.lastCalibrated = lastCalibrated;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Date getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}
	public Date getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}
	public Date getLastCalibrated() {
		return lastCalibrated;
	}
	public void setLastCalibrated(Date lastCalibrated) {
		this.lastCalibrated = lastCalibrated;
	}

}