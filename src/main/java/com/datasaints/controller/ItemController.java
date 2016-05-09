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
	
	@RequestMapping(value = "/getItems/{location}", method = RequestMethod.GET)
	public ArrayList<Item> getItems(@PathVariable int location) {
		System.out.println("Called get items at location " + location);
		conn.populateItems(location);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getItemsToCalibrate/{location}", method = RequestMethod.GET)
	public ArrayList<Item> getItemsToCalibrate(@PathVariable int location) {
		System.out.println("Called get items to calibrate at location " + location);
		conn.populateItemsToCalibrate(location);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedOut/{location}", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedOutItems(@PathVariable int location) {
		System.out.println("Called get checked out items at location " + location);
		conn.populateCheckedOutItems(location);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedIn/{location}", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedInItems(@PathVariable int location) {
		System.out.println("Called get checked in items at location " + location);
		conn.populateCheckedInItems(location);
		
		return conn.getItems();
	}

	@RequestMapping(value = "/getItemCount/{whatToGet}", method = RequestMethod.GET)
	public int getItemCount(@PathVariable Integer whatToGet) {
		System.out.println("Called get amount of items");    
	    return itemService.getItemCount(whatToGet);
	}

   @RequestMapping(value = "/getItems", method = RequestMethod.GET)
   public ArrayList<Item> getItems() {
	   System.out.println("Called get items");

	   // conn.populateItems();
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
   public ArrayList<Item> findItem(@RequestBody Item item) throws NoItemFoundException {
       System.out.println("Called find item with id: " + item.getItemId());
       
       ArrayList<Item> foundItems = itemService.findItem(item);
       
	   return foundItems;
       
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
