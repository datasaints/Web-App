package com.datasaints.controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datasaints.domain.Item;
import com.datasaints.exception.AddItemException;
import com.datasaints.exception.NoItemFoundException;
import com.datasaints.service.ItemService;

import com.datasaints.dao.JDBCConnect;

@RestController
@EnableAutoConfiguration
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	private JDBCConnect conn = new JDBCConnect();

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Item item() {
		System.out.println("called test");
		
		Item item = new Item();
		item.setItemId("1");
		item.setCheckIn(new Date());
		item.setCheckOut(new Date());
		item.setEmployeeId(2);
		item.setLastCalibrated(new Date());

		return item;
	}
	
	

   @RequestMapping(value = "/getItems", method = RequestMethod.GET)
   public ArrayList<Item> getItems() {
	   System.out.println("Called get items");

	   conn.populateItems();
      return conn.getItems();
   }
   
   @RequestMapping(value = "/addItem", method = RequestMethod.POST)
   public Item addItem(@RequestBody Item item) {
	   System.out.println("Called add item");
	  //TODO: ADD MORE ERROR CHECKING
	   String checkArgumentResponse = itemService.checkAddItemArguments(item);
	   
	   if (checkArgumentResponse != null) {
	       	throw new IllegalArgumentException(checkArgumentResponse);
	   }
	   
	   Item newItem = itemService.addItem(item);
	   
	   if (newItem == null) {
	       	throw new AddItemException("There was an error attempting to add the item to the database");
	   }
	   
	   return newItem;
   }
   
   @RequestMapping(value = "/findItem", method = RequestMethod.POST)
   public Item findItem(@RequestBody Item item) throws NoItemFoundException {
       System.out.println("Called find item with id: " + item.getItemId());
       
       Item foundItem = itemService.findItem(item);

       if (foundItem == null) {
    	   throw new NoItemFoundException("The requested item does not exist");
       }
       
	   return foundItem;
       
   }
   
   @RequestMapping(value = "/deleteItem", method = RequestMethod.DELETE)
   public String deleteItem(@RequestBody String itemId) {

   	if (itemId == null) {
   		throw new IllegalArgumentException("itemId is required");
   	}
   	
   	System.out.println("Called delete with itemId: " + itemId);
   	
   	if (itemService.getItemById(itemId) == null) {
       	throw new NoItemFoundException("Item with id '" + itemId + "' does not exist");
       }
   	
       itemService.deleteItem(itemId);

       return "Deleted item with ID " + itemId;
   }
}
