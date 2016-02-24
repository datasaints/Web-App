package com.datasaints.service;

import org.springframework.stereotype.Service;

import com.datasaints.dao.ItemDao;
import com.datasaints.dao.ItemDaoImpl;
import com.datasaints.domain.Item;

@Service
public class ItemServiceImpl implements ItemService {
	@Override
	public Item addItem(Item item) {
		ItemDao itemDao = new ItemDaoImpl();

		return itemDao.getItemById(itemDao.addItem(item));
	}

	@Override
	public void deleteItem(String itemId) {
		/* At this point it should already be checked if the item is null,
		 * but let's do it again because we don't know who will call it in
		 * the future.
		 */

		if (itemId == null) {
	   		throw new IllegalArgumentException("itemId is required");
		}

		ItemDao itemDao = new ItemDaoImpl();

		itemDao.deleteItem(itemId);
	}

	@Override
	public String checkAddItemArguments(Item item) {
      if (item == null) {
         return "addItem: no item to be added";
      }

		if (item.getItemId() == null || item.getItemId().isEmpty()) {
			return "addItem: item ID missing";
		}
      /*
      if (item.getEmployeeId() == null) {
         return "addItem: employee ID missing";
      }
      */
      if (item.getItemName() == null || item.getItemName().isEmpty()) {
         return "addItem: item name missing";
      }
      if (item.getCheckIn() == null) {
         return "addItem: check in time missing";
      }
      if (item.getCheckOut() == null) {
         return "addItem: check out time missing";
      }
      if (item.getLastCalibrated() == null) {
         return "addItem: last calibration date missing";
      }

		/* If all goes well, just return null */
		return null;
	}

	@Override
	public Item getItemById(String itemId) {
		ItemDao recipeDao = new ItemDaoImpl();

		return recipeDao.getItemById(itemId);
	}
}
