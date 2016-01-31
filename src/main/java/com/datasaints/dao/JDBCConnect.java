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
                    "jdbc:mysql://datasaintsdbinstance.chsdnjuecf9v.us-west-1.rds.amazonaws.com:3306/DSaints?user=datasaints&password=datasaints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        items = new ArrayList<Item>();

        populateItems();
    }

    private void populateItems() {
        String sql = "SELECT * FROM DSaints.Equipment;";

        ResultSet rst;
        PreparedStatement pst;

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
