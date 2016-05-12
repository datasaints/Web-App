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
	public int getItemCount(String location, int whatToGet) {
		ItemDao itemDao = new ItemDaoImpl();
		
		return itemDao.getItemCount(location, whatToGet);
	}

	@Override
	public boolean deleteItem(String owner, int internalId) {
		/* At this point it should already be checked if the item is null,
		 * but let's do it again because we don't know who will call it in
		 * the future.
		 */

		if (owner == null) {
	   		throw new IllegalArgumentException("owner is required");
		}

		ItemDao itemDao = new ItemDaoImpl();

		return itemDao.deleteItem(owner, internalId);
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
	public boolean updateItemLocation(String owner, int internalId, String newLocation) {
		ItemDao itemDao = new ItemDaoImpl();
		
		return itemDao.updateLocation(owner, internalId, newLocation);
	}

	@Override
	public Item getItem(String owner, int internalId) {
		ItemDao itemDao = new ItemDaoImpl();

		return itemDao.getItem(owner, internalId);
	}
	
	/*
	@Override
	public ArrayList<Item> findItem(Item toFind) {
		ItemDao itemDao = new ItemDaoImpl();
		ArrayList<Item> foundItems = itemDao.findItem(toFind);
		
		return foundItems;
	}
	*/
}
