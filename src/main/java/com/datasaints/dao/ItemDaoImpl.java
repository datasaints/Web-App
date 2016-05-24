package com.datasaints.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.SQLException;

import com.datasaints.domain.Item;
import com.datasaints.exception.AddItemException;

public class ItemDaoImpl implements ItemDao {
	 public Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://aa1id9u2m7qsv38.chsdnjuecf9v.us-west-1.rds.amazonaws.com/ebdb?user=datasaints&password=datasaints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conn;
    }

	 public void closeConnection(Connection conn) {
		/* TODO: clean up before close:
		 * It is strongly recommended that an application explicitly commits or
		 * rolls back an active transaction prior to calling the close method. If
		 * the close method is called and there is an active transaction, the
		 * results are implementation-defined.
		 */
        try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	public int getItemCount(int whatToGet) {
		Connection conn = getConnection();
		int count = 0;
		PreparedStatement pst;
		String statement = "";
        ResultSet rst;

		if (whatToGet == 1) {
			statement = "SELECT COUNT(*) FROM Equipment";
		}
		else if (whatToGet == 2) { //CheckIn count
			statement = "SELECT COUNT(*) FROM CheckedIn";
		}
		else if (whatToGet == 3) { //CheckOut Count
			statement = "SELECT COUNT(*) FROM CheckedOut";
		}
		else if (whatToGet == 4) { //lastCalibratedCount
			//TODO: logic for last calibrated count
			return 1;
		}
		else {
			System.out.println("shouldn't have gotten in here..... GET ITEM COUNT");
		}
		
        try {
            pst = conn.prepareStatement(statement);

            rst = pst.executeQuery();
            
            if (rst.next())
            	count = rst.getInt(1);
            
            System.out.println("Atempted to count: " +count);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        finally {
        	closeConnection(conn);

        }
		
		return count;
	}
	
	public int getItemCount(String location, int whatToGet) {
		Connection conn = getConnection();
		int count = 0;
		PreparedStatement pst;
		String statement = "";
        ResultSet rst;

		if (whatToGet == 1) {
			statement = "SELECT COUNT(*) FROM Equipment e JOIN " +
				"Location l ON e.owner = l.id WHERE l.name = ?";
		}
		else if (whatToGet == 2) { //CheckIn count
			statement = "SELECT COUNT(*) FROM Equipment e JOIN " +
				"Location l ON e.owner = l.id JOIN CheckedIn c " +
				"ON c.id = e.id WHERE l.name = ?";
		}
		else if (whatToGet == 3) { //CheckOut Count
			statement = "SELECT COUNT(*) FROM Equipment e JOIN " +
				"Location l ON e.owner = l.id JOIN CheckedOut c " +
				"ON c.id = e.id WHERE l.name = ?";
		}
		else if (whatToGet == 4) { //lastCalibratedCount
			//TODO: logic for last calibrated count
			return 1;
		}
		else {
			System.out.println("shouldn't have gotten in here..... GET ITEM COUNT");
		}
		
        try {
            pst = conn.prepareStatement(statement);
            pst.setString(1, location);

            rst = pst.executeQuery();
            
            if (rst.next())
            	count = rst.getInt(1);
            
            System.out.println("Atempted to count: " +count);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        finally {
        	closeConnection(conn);

        }
		
		return count;
	}
	
	public boolean addItem(Item item) {
		Connection conn = getConnection();
		PreparedStatement pst;
		String checkStatus;
        
        boolean success = false;
        
        if (item.getOwner() == null) {
        	throw new AddItemException("No owner given");
        }
        
        try {
           pst = conn.prepareStatement("INSERT INTO Equipment (id, owner, " +
        	   "serial, itemName, currentLocation, lastCalibrated) " + 
        	   "VALUES (?, (SELECT id FROM Location WHERE name = ?)" + 
        	   ", ?, ?, (SELECT id FROM Location WHERE " + 
        	   "name = ?), ?)");

           pst.setString(1, item.getId());
           pst.setString(2, item.getOwner());
           pst.setInt(3, item.getSerial());
           pst.setString(4, item.getItemName());
           pst.setString(5, item.getLocation());
           pst.setDate(6, item.getLastCalibrated());

	        success = (pst.executeUpdate() == 1);
	        
	        pst.close();
	        
	        checkStatus = item.getStatus() == Item.Status.CHECKED_OUT ? "CheckedOut" : "CheckedIn";
	        
	        pst = conn.prepareStatement("INSERT INTO " + checkStatus + 
	        	" VALUES (?, ?)");
	        pst.setString(1, item.getId());
	        pst.setTimestamp(2, item.getCheckTime());
	        
	        success = (pst.executeUpdate() == 1);
	        pst.close();

	        System.out.println("Added item " + item.getId() + " to the database");
        }
        catch (SQLException e) {
			System.out.println(e.getMessage());
			
			//TODO: DUPLICATE ENTRY
        }
        finally {
        	closeConnection(conn);
        }
        
        return success;
	}
	
	@Override
	public boolean updateLocation(String id, String newLocation) {
		Connection conn = getConnection();
		PreparedStatement pst;
		boolean success = false;
		
		try {
			pst = conn.prepareStatement("UPDATE Equipment SET currentLocation = " + 
				"(SELECT id FROM Location WHERE name = ?) WHERE id = ?");
			pst.setString(1, newLocation);
			pst.setString(2, id);
			
			success = (pst.executeUpdate() == 1);
			
			System.out.println("Updated location of " + id + " to be " + newLocation);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			closeConnection(conn);
		}
		
		return success;
	}
	
	@Override
	public boolean updateItem(Item item) {
		boolean success = false;
		PreparedStatement pst = null;
		PreparedStatement updateStatement = null;
		ResultSet rst = null;
		Connection conn = getConnection();
		
		try {
			pst = conn.prepareStatement("UPDATE Equipment SET owner = " + 
				"(SELECT id FROM Location WHERE name = ?), " +
				"serial = ?, itemName = ?, currentLocation = " + 
				"(SELECT id FROM Location WHERE name = ?), lastCalibrated " + 
				"= ? WHERE id = ?");
			
			pst.setString(1, item.getOwner());
			pst.setInt(2, item.getSerial());
			pst.setString(3, item.getItemName());
			pst.setString(4, item.getLocation());
			pst.setDate(5, item.getLastCalibrated());
			pst.setString(6, item.getId());
			
			success = (pst.executeUpdate() == 1);
					
			pst.close();
			
			// Check if item status changed
			pst = conn.prepareStatement("SELECT * FROM CheckedOut WHERE id = ?");
			pst.setString(1, item.getId());
			
			rst = pst.executeQuery();
						
			if (rst.next()) {
				
				pst.close();
				
				// Item is currently checked out
				if (item.getStatus() == Item.Status.CHECKED_IN) {
					// Want to check item in
					pst = conn.prepareStatement("DELETE FROM CheckedOut WHERE id = ?");
					pst.setString(1, item.getId());
					pst.executeUpdate();
					
					pst.close();
					
					pst = conn.prepareStatement("INSERT INTO CheckedIn (id, checkTime) VALUES " + 
						"(?, ?)");
					pst.setString(1, item.getId());
					pst.setTimestamp(2, item.getCheckTime());
					pst.executeUpdate();
					
				}
			}
			else {
				// Item is currently checked in
				if (item.getStatus() == Item.Status.CHECKED_OUT) {
					// Want to check item out
					pst = conn.prepareStatement("DELETE FROM CheckedIn WHERE id = ?");
					pst.setString(1, item.getId());
					pst.executeUpdate();
					
					pst.close();
					
					pst = conn.prepareStatement("INSERT INTO CheckedOut (id, checkTime) VALUES " + 
						"(?, ?)");
					pst.setString(1, item.getId());
					pst.setTimestamp(2, item.getCheckTime());
					pst.executeUpdate();
				}
			}
			
			if (success) {
				System.out.println("Successfully updated item " + item.getId());
			}
			else {
				System.out.println("Unable to update item " + item.getId());
			}
			
			pst.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			closeConnection(conn);
		}
		
		return success;
	}

	@Override
	public boolean deleteItem(String id) {
		Connection conn = getConnection();
        PreparedStatement pst;
        boolean success = false;
        
        PreparedStatement deleteStatus = null;
        ResultSet deleteStatusResult = null;

        try {
        	deleteStatus = conn.prepareStatement("SELECT * FROM CheckedOut " +
        		"WHERE id = ?");
        	deleteStatus.setString(1, id);
        	
        	deleteStatusResult = deleteStatus.executeQuery();
        	if (deleteStatusResult.next()) {
        		deleteStatus.close();
        		deleteStatusResult.close();
        		
        		deleteStatus = conn.prepareStatement("DELETE FROM CheckedOut " +
        			"WHERE id = ?");
        		deleteStatus.setString(1, id);
        		
        		deleteStatus.executeUpdate();
        	}
        	else {
        		deleteStatus.close();
        		deleteStatusResult.close();
        		
        		deleteStatus = conn.prepareStatement("DELETE FROM CheckedIn " +
        			"WHERE id = ?");
        		deleteStatus.setString(1, id);
        		
        		deleteStatus.executeUpdate();
        	}
        	
            pst = conn.prepareStatement("DELETE FROM Equipment WHERE id = ?");
            pst.setString(1, id);

            success = (pst.executeUpdate() == 1);

            System.out.println("Item Deleted From Database");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
        	closeConnection(conn);
        }
        
        return success;
	}

	@Override
    public Item getItem(String id) {
        Connection conn = getConnection();

        PreparedStatement getItemsQuery;
        ResultSet getItemsResult;
        PreparedStatement getStatusQuery;
        ResultSet getStatusResult;
        Item.Status status;
        Timestamp checkTime;

        ArrayList<Item> items = new ArrayList<Item>();
        Item item = null;
        
        try {
        	getItemsQuery = conn.prepareStatement("SELECT e.id, " + 
        		"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
        		"lastCalibrated FROM Equipment e JOIN Location l ON l.id = " +
        		"currentLocation JOIN Location l2 ON l2.id = " + 
        		"owner WHERE e.id = ?");
        	getItemsQuery.setString(1, id);
        		
        	getItemsResult = getItemsQuery.executeQuery();
        		
        	if (getItemsResult == null) {
        		return null;
        	}
        	
        	while (getItemsResult.next()) {
        		System.out.println("Found item with id " + id);
        			
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
        			
        		item = new Item(
        			getItemsResult.getString("id"),
        			getItemsResult.getString("owner"),
        			getItemsResult.getInt("serial"),
        			getItemsResult.getString("itemName"),
        			getItemsResult.getString("location"),
        			status,
        			getItemsResult.getDate("lastCalibrated"),
        			checkTime);
        		
        		items.add(item);
        	}
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	closeConnection(conn);
        }

        return item;
    }
	
	public ArrayList<Item> findItem(Item toFind) {
		Connection conn = getConnection();		
		PreparedStatement pst;
        ResultSet rst;
        ArrayList<Item> itemList = new ArrayList<Item>();
		StringBuilder query = new StringBuilder();
		boolean firstCriteria = true;
		
		PreparedStatement statusQuery = null;
		ResultSet statusResult = null;
		Item.Status status = Item.Status.NONE;
		Timestamp checkTime = null;
		
		/*
		getItemsQuery = conn.prepareStatement("SELECT e.id, " + 
    			"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
    			"lastCalibrated FROM Equipment e JOIN Location l ON l.id = " +
    			"currentLocation JOIN Location l2 ON l2.id = " + 
    			"owner WHERE owner = ? LIMIT ?, ?");
    	*/
		
		query.append("SELECT e.id, l2.name AS 'owner', serial, itemName, " + 
			"l.name AS 'location', lastCalibrated FROM Equipment e JOIN " + 
			"Location l ON l.id = currentLocation JOIN Location l2 ON l2.id = " + 
			"owner WHERE ");
				
		if (toFind.getId() != null) {
			query.append("e.id LIKE '%" + toFind.getId() + "%' ");
			firstCriteria = false;
		}
		
		if (toFind.getOwner() != null) {
			if (!firstCriteria) {
				query.append("AND ");
			}
			else {
				firstCriteria = false;
			}
			
			query.append("l2.name LIKE '%" + toFind.getLocation() + "%' ");
		}
		
		if (toFind.getSerial() != -1) {
			if (!firstCriteria) {
				query.append("AND ");
			}
			else {
				firstCriteria = false;
			}
			
			query.append("CAST(serial AS CHAR) LIKE '%" + toFind.getSerial() + "%' ");
		}
		
		if (toFind.getItemName() != null) {
			if (!firstCriteria) {
				query.append("AND ");
			}
			else {
				firstCriteria = false;
			}
			
			query.append("itemName LIKE '%" + toFind.getItemName() + "%' ");
		}
		
		if (toFind.getLocation() != null) {
			if (!firstCriteria) {
				query.append("AND ");
			}
			else {
				firstCriteria = false;
			}
			
			query.append("l.name LIKE '%" + toFind.getLocation() + "%' ");
		}
		
		
		if (toFind.getLastCalibrated() != null) {
			if (!firstCriteria) {
				query.append("AND ");
			}
			else {
				firstCriteria = false;
			}
			
			query.append("lastCalibrated = " + toFind.getLastCalibrated());
		}
		
		if (toFind.getStatus() == Item.Status.CHECKED_IN) {
			if (!firstCriteria) {
				query.append("AND ");
			}
			else {
				firstCriteria = false;
			}
			
			query.append("e.id IN(SELECT id FROM CheckedIn) ");
		}
		else if (toFind.getStatus() == Item.Status.CHECKED_OUT) {
			if (!firstCriteria) {
				query.append("AND ");
			}
			else {
				firstCriteria = false;
			}
			
			query.append("e.id IN(SELECT id FROM CheckedOut) ");
		}
				
		System.out.println("QUERY: " + query.toString());
		
		try {
            pst = conn.prepareStatement(query.toString());
            rst = pst.executeQuery();

            while (rst.next()) {
            	statusQuery = conn.prepareStatement("SELECT * FROM CheckedOut " +
            		"WHERE id = ?");
            	statusQuery.setString(1, rst.getString("id"));
            	statusResult = statusQuery.executeQuery();
            	
            	if (statusResult.next()) {
            		status = Item.Status.CHECKED_OUT;
            		checkTime = statusResult.getTimestamp("checkTime");
            	}
            	else {
            		status = Item.Status.CHECKED_IN;
            		
            		statusQuery.close();
            		statusResult.close();
            		
            		statusQuery = conn.prepareStatement("SELECT * FROM CheckedIn " +
            			"WHERE id = ?");
            		statusQuery.setString(1, rst.getString("id"));
            		statusResult = statusQuery.executeQuery();
            		
            		statusResult.next();
            		checkTime = statusResult.getTimestamp("checkTime");
            	}
            	
            	statusQuery.close();
            	statusResult.close();
            	
            	itemList.add(new Item(
            		rst.getString("id"),
            		rst.getString("owner"),
            		rst.getInt("serial"),
            		rst.getString("itemName"),
            		rst.getString("location"),
            		status,
            		rst.getDate("lastCalibrated"),
            		checkTime));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
        	closeConnection(conn);
        }
		
		return itemList;

	}
}
