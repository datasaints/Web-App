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

	public String addItem(Item item) {
		Connection conn = getConnection();
		PreparedStatement pst;
        ResultSet rst;

        if (item.getItemId() ==  null) {
        	throw new AddItemException("No item id given");
        }

        try {
        	  System.out.println("The item id attempting to be added is " +item.getItemId());

           //TO CHANGE
           String insertStatement = "INSERT INTO DSaints.Equipment (ItemID, EmployeeID, ItemName) VALUES (?, ?, ?);";

           pst = conn.prepareStatement(insertStatement);

           pst.setString(1, item.getItemId());
           pst.setInt(2, item.getEmployeeId());
           pst.setString(3, item.getItemName());

	        pst.executeUpdate();

	        System.out.println("Added item " +item.getItemId() + " to the database");
        }
        catch (SQLException e) {
			System.out.println(e.getMessage());
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
}
