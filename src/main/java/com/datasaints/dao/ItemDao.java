package com.datasaints.dao;

import com.datasaints.domain.Item;

public interface ItemDao {
	public String addItem(Item item);

	public void deleteItem(String itemId);
	
	public Item getItemById(String itemId);
	
	public Item findItem(Item toFind);

}
