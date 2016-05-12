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
           String insertStatement = "INSERT INTO Equipment (owner, " +
               "internalId, serial, itemName, currentLocation, lastCalibrated) " +
               "VALUES (?, ?, ?, ?, ?, ?);";

           pst = conn.prepareStatement(insertStatement);

           pst.setString(1, item.getOwner());
           pst.setInt(2, item.getInternalId());
           pst.setInt(3, item.getSerial());
           pst.setString(4, item.getItemName());
           pst.setString(5, item.getLocation());
           pst.setDate(6, item.getLastCalibrated());

	        success = (pst.executeUpdate() == 1);

	        System.out.println("Added item " + item.getOwner() + "'s " +
	        	item.getInternalId()+ " to the database");
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
	
	public boolean updateLocation(String owner, int internalId, String newLocation) {
		Connection conn = getConnection();
		PreparedStatement pst;
		boolean success = false;
		
		try {
			pst = conn.prepareStatement("UPDATE Equipment SET currentLocation = " + 
				"(SELECT id FROM Location WHERE name = ?) WHERE owner = " + 
				"(SELECT id FROM Location WHERE name = ?) AND internalId = ?");
			pst.setString(1, newLocation);
			pst.setString(2, owner);
			pst.setInt(3, internalId);
			
			success = (pst.executeUpdate() == 1);
			
			System.out.println("Updated location of " + owner + "'s " + 
				internalId + " to be " + newLocation);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			closeConnection(conn);
		}
		
		return success;
	}

	public boolean deleteItem(String owner, int internalId) {
		Connection conn = getConnection();
        PreparedStatement pst;
        boolean success = false;
        
        PreparedStatement deleteStatus = null;
        ResultSet deleteStatusResult = null;

        try {
        	deleteStatus = conn.prepareStatement("SELECT * FROM CheckedOut " +
        		"WHERE id = (SELECT id FROM Equipment WHERE owner = ? " + 
        		"AND internalId = ?)");
        	deleteStatus.setString(1, owner);
        	deleteStatus.setInt(2, internalId);
        	
        	deleteStatusResult = deleteStatus.executeQuery();
        	if (deleteStatusResult.next()) {
        		deleteStatus.close();
        		deleteStatusResult.close();
        		
        		deleteStatus = conn.prepareStatement("DELETE FROM CheckedOut " +
        			"WHERE id = (SELECT id FROM Equipment WHERE owner = ? " +
        			"AND internalID = ?)");
        		deleteStatus.setString(1, owner);
        		deleteStatus.setInt(2, internalId);
        		
        		deleteStatus.executeUpdate();
        	}
        	else {
        		deleteStatus.close();
        		deleteStatusResult.close();
        		
        		deleteStatus = conn.prepareStatement("DELETE FROM CheckedIn " +
        			"WHERE id = (SELECT id FROM Equipment WHERE owner = ? " +
        			"AND internalID = ?)");
        		deleteStatus.setString(1, owner);
        		deleteStatus.setInt(2, internalId);
        		
        		deleteStatus.executeUpdate();
        	}
        	
            pst = conn.prepareStatement("DELETE FROM Equipment WHERE owner = " + 
            	"(SELECT id FROM Location WHERE name = ?) AND internalID = ?");
            pst.setString(1, owner);
            pst.setInt(2, internalId);

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
    public Item getItem(String owner, int internalId) {
        Connection conn = getConnection();
        ResultSet rst;
        PreparedStatement pst;

        Item item = new Item();

        try {
            pst = conn.prepareStatement("SELECT l1.name AS 'owner', " + 
            	"internalId, serial, itemName, l2.name AS 'location', " +
            	"lastCalibrated FROM Equipment e JOIN Location l1 ON " +
            	"l1.name = ? AND l1.id = e.owner JOIN Location l2 ON l2.name = ? WHERE " + 
            	"internalId = ?");
            pst.setString(1, owner);
            pst.setString(2, owner);
            pst.setInt(3, internalId);
        	rst = pst.executeQuery();

            if (rst.next()) {
            	System.out.println("Found item");
                item = new Item();
                item.setOwner(rst.getString("owner"));
                item.setInternalId(rst.getInt("internalId"));
                item.setItemName(rst.getString("itemName"));
                item.setLocation(rst.getString("location"));
                item.setLastCalibrated(rst.getDate("lastCalibrated"));

            } else {
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
