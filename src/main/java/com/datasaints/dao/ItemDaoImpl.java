package com.datasaints.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;

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
        
        boolean success = false;
        
        if (item.getOwner() == null) {
        	throw new AddItemException("No owner given");
        }
        
        try {
           //TO CHANGE
           String insertStatement = "INSERT INTO Equipment (id, owner, " +
               "serial, itemName, currentLocation, lastCalibrated) " +
               "VALUES (?, ?, ?, ?, ?, ?);";

           pst = conn.prepareStatement(insertStatement);

           pst.setString(1, item.getId());
           pst.setString(2, item.getOwner());
           pst.setInt(3, item.getSerial());
           pst.setString(4, item.getItemName());
           pst.setString(5, item.getLocation());
           pst.setDate(6, item.getLastCalibrated());

	        success = (pst.executeUpdate() == 1);

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

        Item item = new Item();

        try {
        	getItemsQuery = conn.prepareStatement("SELECT e.id, " + 
        		"l2.name AS 'owner', serial, itemName, l.name AS 'location', " + 
        		"lastCalibrated FROM Equipment e JOIN Location l ON l.id = " +
        		"currentLocation JOIN Location l2 ON l2.id = " + 
        		"owner WHERE e.id = ?");
        	getItemsQuery.setString(1, id);
        		
        	getItemsResult = getItemsQuery.executeQuery();
        		
        	if (getItemsResult.next()) {
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
        	}
        	else {
        		return null;
        	}
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	closeConnection(conn);
        }

        return item;
    }
	
	//TODO: convert datetime
	/*
	public ArrayList<Item> findItem(Item toFind) {
		Connection conn = getConnection();		
		PreparedStatement pst;
        ResultSet rst;
        ArrayList<Item> itemList = new ArrayList<Item>();
		StringBuilder query = new StringBuilder();
		boolean firstCriteria = true;
		int itemIdx = 0;
		
		query.append("SELECT * FROM DSaints.Equipment WHERE ");
		
		if (toFind.getItemId() != null) {
			query.append("ItemID LIKE \"%" + toFind.getItemId() + "%\" ");
			firstCriteria = false;
		}
		
		if (toFind.getItemName() != null) {
			if (!firstCriteria) {
				query.append("AND ");
			} else {
				firstCriteria = false;
			}
			
			query.append("ItemName LIKE \"%" + toFind.getItemName() + "%\" ");
		}
		
		if (toFind.getEmployeeId() != 0) {
			if (!firstCriteria) {
				query.append("AND ");
			} else {
				firstCriteria = false;
			}
			
			query.append("EmployeeID = " + toFind.getEmployeeId() + " ");
		}
		
		if (toFind.getLastCalibrated() != null) {
			if (!firstCriteria) {
				query.append("AND ");
			}
			
			query.append("LastCalibrated = " + toFind.getLastCalibrated());
		}
		
		query.append(";");
		
		System.out.println("Attempting to query " +query.toString());
		
		try {
            pst = conn.prepareStatement(query.toString());
            rst = pst.executeQuery();

            while (rst.next()) {
                itemList.add(itemIdx, new Item());

                itemList.get(itemIdx).setItemId(rst.getString("ItemID"));
                itemList.get(itemIdx).setEmployeeId(rst.getInt("EmployeeID"));
                itemList.get(itemIdx).setItemName(rst.getString("ItemName"));
                itemList.get(itemIdx).setCheckIn(rst.getDate("CheckIn"));
                itemList.get(itemIdx).setCheckOut(rst.getDate("CheckOut"));
                itemList.get(itemIdx).setLastCalibrated(rst.getDate("LastCalibrated"));

                itemIdx++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
        	closeConnection(conn);
        }
		
		return itemList;

	}
	*/
}
