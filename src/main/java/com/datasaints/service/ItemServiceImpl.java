package com.datasaints.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.datasaints.dao.ItemDao;
import com.datasaints.dao.ItemDaoImpl;
import com.datasaints.domain.Item;

@Service
public class ItemServiceImpl implements ItemService {
	@Override
	public boolean addItem(Item item) {
		ItemDao itemDao = new ItemDaoImpl();

		return itemDao.addItem(item);
	}
	
	@Override
	public int getItemCount(int whatToGet) {
		ItemDao itemDao = new ItemDaoImpl();

		return itemDao.getItemCount(whatToGet);
	}
	
	@Override
	public int getItemCountByLocation(String location, int whatToGet) {
		ItemDao itemDao = new ItemDaoImpl();
		
		return itemDao.getItemCount(location, whatToGet);
	}

	@Override
	public boolean deleteItem(String id) {
		/* At this point it should already be checked if the item is null,
		 * but let's do it again because we don't know who will call it in
		 * the future.
		 */

		if (id == null) {
	   		throw new IllegalArgumentException("id is required");
		}

		ItemDao itemDao = new ItemDaoImpl();

		return itemDao.deleteItem(id);
	}

	/*
	@Override
	public String checkAddItemArguments(Item item) {
      if (item == null) {
         return "addItem: no item to be added";
      }

		if (item.getItemId() == null || item.getItemId().isEmpty()) {
			return "addItem: item ID missing";
		}
      if (item.getItemName() == null || item.getItemName().isEmpty()) {
         return "addItem: item name missing";
      }
      if (item.getLastCalibrated() == null) {
         return "addItem: last calibration date missing";
      }

		// If all goes well, just return null
		return null;
	}
	*/
	
	@Override
	public boolean updateItemLocation(String id, String newLocation) {
		ItemDao itemDao = new ItemDaoImpl();
		
		return itemDao.updateLocation(id, newLocation);
	}

	@Override
	public Item getItem(String id) {
		ItemDao itemDao = new ItemDaoImpl();

		return itemDao.getItem(id);
	}
	
	@Override
	public boolean updateItem(Item item) {
		ItemDao itemDao = new ItemDaoImpl();
		
		return itemDao.updateItem(item);
	}
	
	@Override
	public ArrayList<Item> findItem(Item toFind) {
		ItemDao itemDao = new ItemDaoImpl();
		ArrayList<Item> foundItems = itemDao.findItem(toFind);
		
		return foundItems;
	}
}
