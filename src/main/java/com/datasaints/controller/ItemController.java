package com.datasaints.controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datasaints.domain.Item;
import com.datasaints.dao.JDBCConnect;

@RestController
@EnableAutoConfiguration
public class ItemController {

   private JDBCConnect conn = new JDBCConnect();

	@RequestMapping("/test")
	public Item item() {
		Item item = new Item();
		item.setItemId("1");
		item.setCheckIn(new Date());
		item.setCheckOut(new Date());
		item.setEmployeeId(2);
		item.setLastCalibrated(new Date());

		return item;
	}

   @RequestMapping(value = "/getItems", method = RequestMethod.POST)
   public ArrayList<Item> getItems() {
	  conn.populateItems();
      return conn.getItems();
   }
}
