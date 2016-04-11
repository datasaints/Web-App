package com.datasaints.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

import com.datasaints.domain.Item;
import com.datasaints.exception.AddItemException;

public class ItemDaoImpl implements ItemDao {
	 public Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://datasaintsdbinstance.chsdnjuecf9v.us-west-1.rds.amazonaws.com:3306/DSaints?user=datasaints&password=datasaints");
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
			statement = "SELECT COUNT(*) FROM DSaints.Equipment";
		}
		else if (whatToGet == 2) {
			
		}
		else if (whatToGet == 2) {
			
		}
		else if (whatToGet == 2) {
	
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
	
	public String addItem(Item item) {
		Connection conn = getConnection();
		PreparedStatement pst;
        ResultSet rst;
        java.sql.Date sqlCheckIn = null; 
        java.sql.Date sqlCheckOut = null;
        java.sql.Date sqlLastCali = new java.sql.Date(item.getLastCalibrated().getTime());

        if (item.getItemId() ==  null) {
        	throw new AddItemException("No item id given");
        }
        
        if (item.getCheckIn() != null) {
        	sqlCheckIn = new java.sql.Date(item.getCheckIn().getTime());
        }
        
        if (item.getCheckOut() != null) {
        	sqlCheckOut = new java.sql.Date(item.getCheckOut().getTime());
        }
        
        try {
        	  System.out.println("The item id attempting to be added is " +item.getItemId());

           //TO CHANGE
           String insertStatement = "INSERT INTO DSaints.Equipment (ItemID, EmployeeID, ItemName, CheckIn, CheckOut, LastCalibrated) VALUES (?, ?, ?, ?, ?, ?);";

           pst = conn.prepareStatement(insertStatement);

           pst.setString(1, item.getItemId());
           pst.setInt(2, item.getEmployeeId());
           pst.setString(3, item.getItemName());          
           pst.setDate(4, sqlCheckIn);
           pst.setDate(5, sqlCheckOut);
           pst.setDate(6, sqlLastCali);


	        pst.executeUpdate();

	        System.out.println("Added item " +item.getItemId() + " to the database");
        }
        catch (SQLException e) {
			System.out.println(e.getMessage());
			
			//TODO: DUPLICATE ENTRY
        }
        finally {
        	closeConnection(conn);
        }

        return item.getItemId();
	}

	public void deleteItem(String itemId) {
		Connection conn = getConnection();
        PreparedStatement pst;

        String deleteStatement = "DELETE FROM DSaints.Equipment WHERE itemID = " + itemId + ";";

        try {
            pst = conn.prepareStatement(deleteStatement);

            pst.executeUpdate();

            System.out.println("Item Deleted From Database");

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        finally {
        	closeConnection(conn);

        }
	}

	@Override
    public Item getItemById(String itemId) {
        Connection conn = getConnection();

        ResultSet rst;
        PreparedStatement pst;

        /* Check if the string is in the proper format -- need to add "" marks
         * around it.
         */
        if (itemId.charAt(0) != '"') {
        	itemId = '"' + itemId + '"';
        }

        String itemQuery = "SELECT * FROM DSaints.Equipment WHERE ItemID = " + itemId + ";";

        System.out.println("database query executed: " + itemQuery);

        Item item = new Item();

        try {
            pst = conn.prepareStatement(itemQuery);
        	rst = pst.executeQuery();

            if (rst.next()) {
                item = new Item();

                item.setItemId(rst.getString("itemID"));
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
	public Item findItem(Item toFind) {
		Connection conn = getConnection();		
		PreparedStatement pst;
        ResultSet rst;
        Item item = new Item();
		StringBuilder query = new StringBuilder();
		boolean firstCriteria = true;
		
		query.append("SELECT * FROM DSaints.Equipment WHERE ");
		
		if (toFind.getItemId() != null) {
			query.append("ItemID = \"" + toFind.getItemId() + "\" ");
			firstCriteria = false;
		}
		
		if (toFind.getItemName() != null) {
			if (!firstCriteria) {
				query.append("AND ");
			} else {
				firstCriteria = false;
			}
			
			query.append("ItemName = \"" + toFind.getItemName() + "\" ");
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

            if (rst.next()) {
                item = new Item();

                item.setItemId(rst.getString("ItemID"));
                item.setEmployeeId(rst.getInt("EmployeeID"));
                item.setItemName(rst.getString("ItemName"));
                item.setCheckIn(rst.getDate("CheckIn"));
                item.setCheckOut(rst.getDate("CheckOut"));
                item.setLastCalibrated(rst.getDate("LastCalibrated"));


            } else {
            	return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
        	closeConnection(conn);
        }
		
		return item;

	}
}
