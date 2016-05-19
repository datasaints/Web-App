package com.datasaints.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.ArrayList;

import com.datasaints.domain.Item;
import com.datasaints.domain.Location;

public class JDBCConnect {
    private ArrayList<Item> items;
    private ArrayList<Location> locations;

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
        locations = new ArrayList<Location>();

        populateItems(0, 0, 10);
    }
    
    public void populateItems(int location, int page, int numPerPage) {
    	PreparedStatement getItemsQuery = null;
    	PreparedStatement getStatusQuery = null;
    	ResultSet getItemsResult = null;
    	ResultSet getStatusResult = null;
    	String id;
    	Item.Status status = Item.Status.CHECKED_IN;
    	Timestamp checkTime = null;
    	
    	items.clear();
    	
    	try {
    		getItemsQuery = conn.prepareStatement("SELECT e.id, " + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN Location l2 ON l2.id = " + 
    			"owner WHERE owner = ? LIMIT ?, ?");
    		getItemsQuery.setInt(1, location);
    		getItemsQuery.setInt(2, page * numPerPage);
    		getItemsQuery.setInt(3, numPerPage);
    		
    		getItemsResult = getItemsQuery.executeQuery();
    		
    		while (getItemsResult.next()) {    			
    			// For now, assume that if an item isn't checked in, it's checked out
    			id = getItemsResult.getString("id");
    			getStatusQuery = conn.prepareStatement("SELECT * FROM CheckedOut WHERE id = ?");
    			getStatusQuery.setString(1, id);
    			
    			getStatusResult = getStatusQuery.executeQuery();
    			
    			if (getStatusResult.next()) {
    				status = Item.Status.CHECKED_OUT;
    				checkTime = getStatusResult.getTimestamp("checkTime");
    			}
    			else {
    				status = Item.Status.CHECKED_IN;
    				getStatusQuery.close();
    				getStatusResult.close();
    				
    				getStatusQuery = conn.prepareStatement("SELECT * FROM CheckedIn WHERE id = ?");
    				getStatusQuery.setString(1, id);
    				getStatusResult = getStatusQuery.executeQuery();
    				
    				getStatusResult.next();
    				checkTime = getStatusResult.getTimestamp("checkTime");
    			}
    			
    			items.add(new Item(
    					getItemsResult.getString("id"),
    					getItemsResult.getString("owner"),
    					getItemsResult.getInt("serial"),
    					getItemsResult.getString("itemName"),
    					getItemsResult.getString("location"),
    					status,
    					getItemsResult.getDate("lastCalibrated"),
    					checkTime));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(getItemsQuery, getItemsResult, getStatusQuery, getStatusResult);
    	}
    }
    
    public void populateItems(int page, int numPerPage) {
    	PreparedStatement getItemsQuery = null;
    	PreparedStatement getStatusQuery = null;
    	ResultSet getItemsResult = null;
    	ResultSet getStatusResult = null;
    	String id;
    	Item.Status status = Item.Status.CHECKED_IN;
    	Timestamp checkTime = null;
    	
    	items.clear();
    	
    	try {
    		getItemsQuery = conn.prepareStatement("SELECT e.id, " + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN Location l2 ON l2.id = owner " + 
    			"LIMIT ?, ?");
    		getItemsQuery.setInt(1, page * numPerPage);
    		getItemsQuery.setInt(2, numPerPage);
    		
    		getItemsResult = getItemsQuery.executeQuery();
    		
    		while (getItemsResult.next()) {    			
    			// For now, assume that if an item isn't checked in, it's checked out
    			id = getItemsResult.getString("id");
    			getStatusQuery = conn.prepareStatement("SELECT * FROM CheckedOut WHERE id = ?");
    			getStatusQuery.setString(1, id);
    			
    			getStatusResult = getStatusQuery.executeQuery();
    			
    			if (getStatusResult.next()) {
    				status = Item.Status.CHECKED_OUT;
    				checkTime = getStatusResult.getTimestamp("checkTime");
    			}
    			else {
    				status = Item.Status.CHECKED_IN;
    				getStatusQuery.close();
    				getStatusResult.close();
    				
    				getStatusQuery = conn.prepareStatement("SELECT * FROM CheckedIn WHERE id = ?");
    				getStatusQuery.setString(1, id);
    				getStatusResult = getStatusQuery.executeQuery();
    				
    				getStatusResult.next();
    				checkTime = getStatusResult.getTimestamp("checkTime");
    			}
    			
    			items.add(new Item(
    					getItemsResult.getString("id"),
    					getItemsResult.getString("owner"),
    					getItemsResult.getInt("serial"),
    					getItemsResult.getString("itemName"),
    					getItemsResult.getString("location"),
    					status,
    					getItemsResult.getDate("lastCalibrated"),
    					checkTime));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(getItemsQuery, getItemsResult, getStatusQuery, getStatusResult);
    	}
    }
    
    public void populateItemsToCalibrate(int location, int period, int page, int numPerPage) {
    	PreparedStatement getItemsQuery = null;
    	PreparedStatement getStatusQuery = null;
    	ResultSet getItemsResult = null;
    	ResultSet getStatusResult = null;
    	String id;
    	Item.Status status = Item.Status.CHECKED_IN;
    	Timestamp checkTime = null;
    	
    	items.clear();
    	
    	try {
    		getItemsQuery = conn.prepareStatement("SELECT e.id, " + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN Location l2 ON l2.id = owner " +
    			"WHERE owner = ? AND DATEDIFF(NOW(), " + 
    			"lastCalibrated) >= ? LIMIT ?, ?");
    		getItemsQuery.setInt(1, location);
    		getItemsQuery.setInt(2, period);
    		getItemsQuery.setInt(3, page * numPerPage);
    		getItemsQuery.setInt(4, numPerPage);
    		
    		getItemsResult = getItemsQuery.executeQuery();
    		
    		while (getItemsResult.next()) {    			
    			// For now, assume that if an item isn't checked in, it's checked out
    			id = getItemsResult.getString("id");
    			getStatusQuery = conn.prepareStatement("SELECT * FROM CheckedOut WHERE id = ?");
    			getStatusQuery.setString(1, id);
    			
    			getStatusResult = getStatusQuery.executeQuery();
    			
    			if (getStatusResult.next()) {
    				status = Item.Status.CHECKED_OUT;
    				checkTime = getStatusResult.getTimestamp("checkTime");
    			}
    			else {
    				status = Item.Status.CHECKED_IN;
    				getStatusQuery.close();
    				getStatusResult.close();
    				
    				getStatusQuery = conn.prepareStatement("SELECT * FROM CheckedIn WHERE id = ?");
    				getStatusQuery.setString(1, id);
    				getStatusResult = getStatusQuery.executeQuery();
    				
    				getStatusResult.next();
    				checkTime = getStatusResult.getTimestamp("checkTime");
    			}
    			
    			items.add(new Item(
    					getItemsResult.getString("id"),
    					getItemsResult.getString("owner"),
    					getItemsResult.getInt("serial"),
    					getItemsResult.getString("itemName"),
    					getItemsResult.getString("location"),
    					status,
    					getItemsResult.getDate("lastCalibrated"),
    					checkTime));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(getItemsQuery, getItemsResult, getStatusQuery, getStatusResult);
    	}
    }
    
    public void populateItemsToCalibrate(int period, int page, int numPerPage) {
    	PreparedStatement getItemsQuery = null;
    	PreparedStatement getStatusQuery = null;
    	ResultSet getItemsResult = null;
    	ResultSet getStatusResult = null;
    	String id;
    	Item.Status status = Item.Status.CHECKED_IN;
    	Timestamp checkTime = null;
    	
    	items.clear();
    	
    	try {
    		getItemsQuery = conn.prepareStatement("SELECT e.id, " + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN Location l2 ON l2.id = owner WHERE DATEDIFF(NOW(), " + 
    			"lastCalibrated) >= ? LIMIT ?, ?");
    		getItemsQuery.setInt(1, period);
    		getItemsQuery.setInt(2, page * numPerPage);
    		getItemsQuery.setInt(3, numPerPage);
    		
    		getItemsResult = getItemsQuery.executeQuery();
    		
    		while (getItemsResult.next()) {    			
    			// For now, assume that if an item isn't checked in, it's checked out
    			id = getItemsResult.getString("id");
    			getStatusQuery = conn.prepareStatement("SELECT * FROM CheckedOut WHERE id = ?");
    			getStatusQuery.setString(1, id);
    			
    			getStatusResult = getStatusQuery.executeQuery();
    			
    			if (getStatusResult.next()) {
    				status = Item.Status.CHECKED_OUT;
    				checkTime = getStatusResult.getTimestamp("checkTime");
    			}
    			else {
    				status = Item.Status.CHECKED_IN;
    				getStatusQuery.close();
    				getStatusResult.close();
    				
    				getStatusQuery = conn.prepareStatement("SELECT * FROM CheckedIn WHERE id = ?");
    				getStatusQuery.setString(1, id);
    				getStatusResult = getStatusQuery.executeQuery();
    				
    				getStatusResult.next();
    				checkTime = getStatusResult.getTimestamp("checkTime");
    			}
    			
    			items.add(new Item(
    					getItemsResult.getString("id"),
    					getItemsResult.getString("owner"),
    					getItemsResult.getInt("serial"),
    					getItemsResult.getString("itemName"),
    					getItemsResult.getString("location"),
    					status,
    					getItemsResult.getDate("lastCalibrated"),
    					checkTime));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(getItemsQuery, getItemsResult, getStatusQuery, getStatusResult);
    	}
    }
    
    public void populateCheckedOutItems(int location, int page, int numPerPage) {
    	PreparedStatement getItemsQuery = null;
    	ResultSet getItemsResult = null;
    	Item.Status status = Item.Status.CHECKED_OUT;
    	
    	items.clear();
    	
    	try {
    		getItemsQuery = conn.prepareStatement("SELECT e.id," + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated, checkTime FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN CheckedOut c ON c.id = " + 
    			"e.id JOIN Location l2 ON l2.id = owner WHERE owner = ? " + 
    			"LIMIT ?, ?");
    		getItemsQuery.setInt(1, location);
    		getItemsQuery.setInt(2, page * numPerPage);
    		getItemsQuery.setInt(3, numPerPage);
    		
    		getItemsResult = getItemsQuery.executeQuery();
    		
    		while (getItemsResult.next()) {
    			items.add(new Item(
    					getItemsResult.getString("id"),
    					getItemsResult.getString("owner"),
    					getItemsResult.getInt("serial"),
    					getItemsResult.getString("itemName"),
    					getItemsResult.getString("location"),
    					status,
    					getItemsResult.getDate("lastCalibrated"),
    					getItemsResult.getTimestamp("checkTime")));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(getItemsQuery, getItemsResult);
    	}
    }
    
    public void populateCheckedOutItems(int page, int numPerPage) {
    	PreparedStatement getItemsQuery = null;
    	ResultSet getItemsResult = null;
    	Item.Status status = Item.Status.CHECKED_OUT;
    	
    	items.clear();
    	
    	try {
    		getItemsQuery = conn.prepareStatement("SELECT e.id," + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated, checkTime FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN CheckedOut c ON c.id = " + 
    			"e.id JOIN Location l2 ON l2.id = owner LIMIT ?, ?");
    		getItemsQuery.setInt(1, page * numPerPage);
    		getItemsQuery.setInt(2, numPerPage);
    		
    		getItemsResult = getItemsQuery.executeQuery();
    		
    		while (getItemsResult.next()) {
    			items.add(new Item(
    					getItemsResult.getString("id"),
    					getItemsResult.getString("owner"),
    					getItemsResult.getInt("serial"),
    					getItemsResult.getString("itemName"),
    					getItemsResult.getString("location"),
    					status,
    					getItemsResult.getDate("lastCalibrated"),
    					getItemsResult.getTimestamp("checkTime")));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(getItemsQuery, getItemsResult);
    	}
    }
    
    public void populateCheckedInItems(int location, int page, int numPerPage) {
    	PreparedStatement getItemsQuery = null;
    	ResultSet getItemsResult = null;
    	Item.Status status = Item.Status.CHECKED_IN;
    	
    	items.clear();
    	
    	try {
    		getItemsQuery = conn.prepareStatement("SELECT e.id," + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated, checkTime FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN CheckedIn c ON c.id = " + 
    			"e.id JOIN Location l2 ON l2.id = owner WHERE owner = ? " + 
    			"LIMIT ?, ?");
    		getItemsQuery.setInt(1, location);
    		getItemsQuery.setInt(2, page * numPerPage);
    		getItemsQuery.setInt(3, numPerPage);
    		
    		getItemsResult = getItemsQuery.executeQuery();
    		
    		while (getItemsResult.next()) {
    			items.add(new Item(
    					getItemsResult.getString("id"),
    					getItemsResult.getString("owner"),
    					getItemsResult.getInt("serial"),
    					getItemsResult.getString("itemName"),
    					getItemsResult.getString("location"),
    					status,
    					getItemsResult.getDate("lastCalibrated"),
    					getItemsResult.getTimestamp("checkTime")));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(getItemsQuery, getItemsResult);
    	}
    }
    
    public void populateCheckedInItems(int page, int numPerPage) {
    	PreparedStatement getItemsQuery = null;
    	ResultSet getItemsResult = null;
    	Item.Status status = Item.Status.CHECKED_IN;
    	
    	items.clear();
    	
    	try {
    		getItemsQuery = conn.prepareStatement("SELECT e.id," + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated, checkTime FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN CheckedIn c ON c.id = " + 
    			"e.id JOIN Location l2 ON l2.id = owner LIMIT ?, ?");
    		getItemsQuery.setInt(1, page * numPerPage);
    		getItemsQuery.setInt(2, numPerPage);
    		
    		getItemsResult = getItemsQuery.executeQuery();
    		
    		while (getItemsResult.next()) {
    			items.add(new Item(
    					getItemsResult.getString("id"),
    					getItemsResult.getString("owner"),
    					getItemsResult.getInt("serial"),
    					getItemsResult.getString("itemName"),
    					getItemsResult.getString("location"),
    					status,
    					getItemsResult.getDate("lastCalibrated"),
    					getItemsResult.getTimestamp("checkTime")));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(getItemsQuery, getItemsResult);
    	}
    }
    
    public void populateLocations() {
    	PreparedStatement pst = null;
    	ResultSet rst = null;
    	
    	locations.clear();
    	
    	try {
    		pst = conn.prepareStatement("SELECT id, name FROM Location");
    		
    		rst = pst.executeQuery();
    		
    		while (rst.next()) {
    			locations.add(new Location(
    				rst.getInt("id"),
    				rst.getString("name")));
    		}
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		close(pst, rst);
    	}
    }
	
    public ArrayList<Item> getItems() {
        return this.items;
    }
    
    public ArrayList<Location> getLocations() {
    	return this.locations;
    }
    
    private void close(Object... toClose) {
    	for (Object obj : toClose) {
    		if (obj != null) {
    			try {
    				obj.getClass().getMethod("close").invoke(obj);
    			}
    			catch (Throwable t) {
    				System.out.println("Failed to close");
    			}
    		}
    	}
    }
}
