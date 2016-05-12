package com.datasaints.controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping(value = "/getItemsToCalibrate", method = RequestMethod.GET)
	public ArrayList<Item> getItemsToCalibrate() {
		System.out.println("Called get items to calibrate");
		conn.populateItemsToCalibrate();
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedOut/{location}", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedOutItems(@PathVariable int location) {
		System.out.println("Called get checked out items at location " + location);
		conn.populateCheckedOutItems(location);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedOut/", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedOutItems() {
		System.out.println("Called get checked out items");
		conn.populateCheckedOutItems();
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedIn/{location}", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedInItems(@PathVariable int location) {
		System.out.println("Called get checked in items at location " + location);
		conn.populateCheckedInItems(location);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedIn", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedInItems() {
		System.out.println("Called get checked in items");
		conn.populateCheckedInItems();
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getItemCount/{location}/{whatToGet}", method = RequestMethod.GET)
	public int getItemCount(@PathVariable String location, int whatToGet) {
		System.out.println("Called get amount of items at " + location);
		return itemService.getItemCount(location, whatToGet);
	}

	@RequestMapping(value = "/getItemCount/{whatToGet}", method = RequestMethod.GET)
	public int getItemCount(@PathVariable Integer whatToGet) {
		System.out.println("Called get amount of items");    
	    return itemService.getItemCount(whatToGet);
	}

   @RequestMapping(value = "/getItems", method = RequestMethod.GET)
   public ArrayList<Item> getItems() {
	   System.out.println("Called get items");

	   conn.populateItems();
      return conn.getItems();
   }
   
   @RequestMapping(value = "/addItem", method = RequestMethod.POST)
   public boolean addItem(@RequestBody Item item) {
	   System.out.println("Called add item");
	  //TODO: Reimplement error checking
	   /*
	   String checkArgumentResponse = itemService.checkAddItemArguments(item);
	   
	   if (checkArgumentResponse != null) {
	       	throw new IllegalArgumentException(checkArgumentResponse);
	   }
	   */
	   
	   return itemService.addItem(item);
   }
   
   @RequestMapping(value = "/updateLocation/{owner}/{internalId}/{newLocation}", method = RequestMethod.POST)
   public String updateItemLocation(@PathVariable String owner,
     @PathVariable int internalId, @PathVariable String newLocation) {
	   if (owner == null) {
		   throw new IllegalArgumentException("owner is required");
	   }
	   
	   System.out.println("Updating location of " + owner + "'s " + internalId + 
	       " to " + newLocation);
	   
	   if (itemService.updateItemLocation(owner, internalId, newLocation)) {
		   return "Successfully updated location of " + owner + "'s " + internalId +
		       " to " + newLocation;
	   }
	   else {
		   return "Failed to update location of " + owner + "'s " + internalId +
		       " to " + newLocation;
	   }
   }
   
   /*
   @RequestMapping(value = "/findItem", method = RequestMethod.POST)
   public ArrayList<Item> findItem(@RequestBody Item item) throws NoItemFoundException {
       System.out.println("Called find item with id: " + item.getItemId());
       
       ArrayList<Item> foundItems = itemService.findItem(item);
       
	   return foundItems;
       
   }
   */
   
   @RequestMapping(value = "/findItem/{owner}/{internalId}", method = RequestMethod.POST)
   public Item findItem(@PathVariable String owner, @PathVariable int internalId) {
	   if (owner == null) {
		   throw new IllegalArgumentException("owner is required");
	   }
	   
	   System.out.println("Finding item with owner = " + owner + ", internalId = " + internalId);
	   
	   return itemService.getItem(owner, internalId);
   }
   
   @RequestMapping(value = "/deleteItem/{owner}/{internalId}", method = RequestMethod.DELETE)
   public String deleteItem(@PathVariable String owner, @PathVariable int internalId) {

   	if (owner == null) {
   		throw new IllegalArgumentException("owner is required");
   	}
   	
   	System.out.println("Called delete with owner: " + owner + ", internalId = " + internalId);
   	
   	if (itemService.getItem(owner, internalId) == null) {
       	throw new NoItemFoundException(owner + " does not have item with internalId " + internalId);
       }
   	
       if (itemService.deleteItem(owner, internalId)) {
    	   return "Deleted " + owner + "'s item with internalId " + internalId;
       }
       else {
    	   return "Failed to delete " + owner + "'s item with internalId " + internalId;
       }
   }
}
