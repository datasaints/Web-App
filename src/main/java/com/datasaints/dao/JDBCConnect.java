package com.datasaints.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

import com.datasaints.domain.Item;

public class JDBCConnect {
    private ArrayList<Item> items;

    private Connection conn;

    public JDBCConnect() {
        conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://aa1id9u2m7qsv38.chsdnjuecf9v.us-west-1.rds.amazonaws.com/ebdb?user=datasaints&password=datasaints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        items = new ArrayList<Item>();

        populateItems(0);
    }
    
    public void populateItems(int location) {
    	// Might have to hard code this
    	String query;
    	
    	switch (location) {
    		case 0:
    			query = "SELECT * FROM ebdb.Metrology;";
    			break;
    		case 1:
    			query = "SELECT * FROM ebdb.Production;";
    			break;
    		case 2:
    			query = "SELECT * FROM  ebdb.SoftwareEngineering;";
    			break;
    		default:
    			// Default to metrology, shouldn't get here anyway
    			query = "SELECT * FROM ebdb.Metrology;";
    			break;
    	}
    	
    	ResultSet rst;
    	PreparedStatement pst;
    	
    	items.clear();
    	
    	try {
    		pst = conn.prepareStatement(query);
    		rst = pst.executeQuery();
    		
    		while (rst.next()) {
    			// Get columns needed
    			items.add(new Item(
    					rst.getString("serial"),
    					0,
    					null,
    					null,
    					null,
    					rst.getDate("lastCalibrated")));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    public void populateItemsToCalibrate(int location) {
    	// Might have to hard code this
    	String query;
    	String locationName;
    	
    	switch (location) {
    		case 0:
    			locationName = "Metrology";
    			break;
    		case 1:
    			locationName = "Production";
    			break;
    		case 2:
    			locationName = "SoftwareEngineering";
    			break;
    		default:
    			// Default to metrology, shouldn't get here anyway
    			locationName = "Metrology";
    			break;
    	}
    	
    	query = "SELECT * FROM ebdb." + locationName + " WHERE DATEDIFF(NOW(), lastCalibrated) >= 14;";
    	
    	ResultSet rst;
    	PreparedStatement pst;
    	
    	items.clear();
    	
    	try {
    		pst = conn.prepareStatement(query);
    		rst = pst.executeQuery();
    		
    		while (rst.next()) {
    			items.add(new Item(
    					rst.getString("serial"),
    					0,
    					null,
    					null,
    					null,
    					rst.getDate("lastCalibrated")));    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    
    
    public void populateCheckedOutItems(int location) {
    	// Might have to hard code this
    	String query;
    	String locationName;
    	
    	switch (location) {
    		case 0:
    			locationName = "Metrology";
    			break;
    		case 1:
    			locationName = "Production";
    			break;
    		case 2:
    			locationName = "SoftwareEngineering";
    			break;
    		default:
    			// Default to metrology, shouldn't get here anyway
    			locationName = "Metrology";
    			break;
    	}
    	
    	query = "SELECT * FROM ebdb." + locationName + " JOIN ebdb.CheckedOut ON ownerId = id;";
    	
    	ResultSet rst;
    	PreparedStatement pst;
    	
    	items.clear();
    	
    	try {
    		pst = conn.prepareStatement(query);
    		rst = pst.executeQuery();
    		
    		while (rst.next()) {
    			items.add(new Item(
    					rst.getString("serial"),
    					0,
    					null,
    					null,
    					null,
    					rst.getDate("lastCalibrated")));    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    public void populateCheckedInItems(int location) {
    	// Might have to hard code this
    	String query;
    	String locationName;
    	
    	switch (location) {
    		case 0:
    			locationName = "Metrology";
    			break;
    		case 1:
    			locationName = "Production";
    			break;
    		case 2:
    			locationName = "SoftwareEngineering";
    			break;
    		default:
    			// Default to metrology, shouldn't get here anyway
    			locationName = "Metrology";
    			break;
    	}
    	
    	query = "SELECT * FROM ebdb." + locationName + " JOIN ebdb.CheckedIn ON ownerId = id;";
    	
    	ResultSet rst;
    	PreparedStatement pst;
    	
    	items.clear();
    	
    	try {
    		pst = conn.prepareStatement(query);
    		rst = pst.executeQuery();
    		
    		while (rst.next()) {
    			items.add(new Item(
    					rst.getString("serial"),
    					0,
    					null,
    					null,
    					null,
    					rst.getDate("lastCalibrated")));    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }

    public void populateItems() {
        String sql = "SELECT * FROM DSaints.Equipment;";

        ResultSet rst;
        PreparedStatement pst;
        
        // Clear item list in case it is full
        items.clear();

        try {
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();

            while (rst.next()) {
            	items.add(new Item(rst.getString("ItemID"), rst.getInt("EmployeeID"),
            			rst.getString("ItemName"), rst.getDate("CheckIn"),
            			rst.getDate("CheckOut"), rst.getDate("LastCalibrated")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
    public ArrayList<Item> getItems() {
        return this.items;
    }
}
