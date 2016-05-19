package com.datasaints.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datasaints.domain.Item;
import com.datasaints.exception.NoItemFoundException;
import com.datasaints.service.ItemService;
import com.datasaints.dao.JDBCConnect;

@RestController
@EnableAutoConfiguration
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	private JDBCConnect conn = new JDBCConnect();
	
   @RequestMapping(value = "/getItems/{page}/{numPerPage}", method = RequestMethod.GET)
   public ArrayList<Item> getItems(@PathVariable Integer page, @PathVariable Integer numPerPage) {
	   System.out.println("Called get items");

	   conn.populateItems(page, numPerPage);
      return conn.getItems();
   }
	
	@RequestMapping(value = "/getItems/{location}/{page}/{numPerPage}", method = RequestMethod.GET)
	public ArrayList<Item> getItems(@PathVariable Integer location, @PathVariable Integer page, @PathVariable Integer numPerPage) {
		System.out.println("Called get items at location " + location);
		conn.populateItems(location, page, numPerPage);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getItemsToCalibrate/{location}/{period}/{page}/{numPerPage}", method = RequestMethod.GET)
	public ArrayList<Item> getItemsToCalibrate(@PathVariable Integer location, @PathVariable Integer period, @PathVariable Integer page, @PathVariable Integer numPerPage) {
		System.out.println("Called get items to calibrate at location " + location);
		conn.populateItemsToCalibrate(location, period, page, numPerPage);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getItemsToCalibrate/{period}/{page}/{numPerPage}", method = RequestMethod.GET)
	public ArrayList<Item> getItemsToCalibrate(@PathVariable Integer period, @PathVariable Integer page, @PathVariable Integer numPerPage) {
		System.out.println("Called get items to calibrate");
		conn.populateItemsToCalibrate(period, page, numPerPage);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedOut/{location}/{page}/{numPerPage}", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedOutItems(@PathVariable Integer location, @PathVariable Integer page, @PathVariable Integer numPerPage) {
		System.out.println("Called get checked out items at location " + location);
		conn.populateCheckedOutItems(location, page, numPerPage);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedOut/{page}/{numPerPage}", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedOutItems(@PathVariable Integer page, @PathVariable Integer numPerPage) {
		System.out.println("Called get checked out items");
		conn.populateCheckedOutItems(page, numPerPage);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedIn/{location}/{page}/{numPerPage}", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedInItems(@PathVariable Integer location, @PathVariable Integer page, @PathVariable Integer numPerPage) {
		System.out.println("Called get checked in items at location " + location);
		conn.populateCheckedInItems(location, page, numPerPage);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getCheckedIn/{page}/{numPerPage}", method = RequestMethod.GET)
	public ArrayList<Item> getCheckedInItems(@PathVariable Integer page, @PathVariable Integer numPerPage) {
		System.out.println("Called get checked in items");
		conn.populateCheckedInItems(page, numPerPage);
		
		return conn.getItems();
	}
	
	@RequestMapping(value = "/getItemCountByLocation/{location}/{whatToGet}", method = RequestMethod.GET)
	public int getItemCountByLocation(@PathVariable String location, @PathVariable Integer whatToGet) {
		System.out.println("Called get amount of items at " + location + "what to get = " +whatToGet);
		return itemService.getItemCountByLocation(location, whatToGet);
	}

	@RequestMapping(value = "/getItemCount/{whatToGet}", method = RequestMethod.GET)
	public int getItemCount(@PathVariable Integer whatToGet) {
		System.out.println("Called get amount of items");    
	    return itemService.getItemCount(whatToGet);
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
   
   @RequestMapping(value = "/updateLocation/{id}/{newLocation}", method = RequestMethod.POST)
   public String updateItemLocation(@PathVariable String id, @PathVariable String newLocation) {
	   if (id == null) {
		   throw new IllegalArgumentException("id is required");
	   }
	   
	   System.out.println("Updating location of " + id + " to " + newLocation);
	   
	   if (itemService.updateItemLocation(id, newLocation)) {
		   return "Successfully updated location of " + id + " to " + newLocation;
	   }
	   else {
		   return "Failed to update location of " + id + " to " + newLocation;
	   }
   }
   
   @RequestMapping(value = "/updateItem", method = RequestMethod.POST)
   public boolean updateItem(@RequestBody Item item) {
	   System.out.println("Called update item with id: " + item.getId());
	   
	   return itemService.updateItem(item);
   }
   
   /*
   @RequestMapping(value = "/findItem", method = RequestMethod.POST)
   public ArrayList<Item> findItem(@RequestBody Item item) throws NoItemFoundException {
       System.out.println("Called find item with id: " + item.getItemId());
       
       ArrayList<Item> foundItems = itemService.findItem(item);
       
	   return foundItems;
       
   }
   */
   
   @RequestMapping(value = "/findItem/{id}", method = RequestMethod.POST)
   public Item findItem(@PathVariable String id) {
	   if (id == null) {
		   throw new IllegalArgumentException("id is required");
	   }
	   
	   System.out.println("Finding item with id = " + id);
	   
	   return itemService.getItem(id);
   }
   
   @RequestMapping(value = "/deleteItem/{id}", method = RequestMethod.DELETE)
   public String deleteItem(@PathVariable String id) {

   	if (id == null) {
   		throw new IllegalArgumentException("id is required");
   	}
   	
   	System.out.println("Called delete with id = " + id);
   	
   	if (itemService.getItem(id) == null) {
       	throw new NoItemFoundException("Item with id " + id + " does not exist");
       }
   	
       if (itemService.deleteItem(id)) {
    	   return "Deleted item with id" + id;
       }
       else {
    	   return "Failed to delete item with id " + id;
       }
   }
}
