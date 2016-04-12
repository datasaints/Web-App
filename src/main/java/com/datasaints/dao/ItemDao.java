package com.datasaints.dao;

import java.util.ArrayList;

import com.datasaints.domain.Item;

public interface ItemDao {
	public String addItem(Item item);

	public void deleteItem(String itemId);
	
	public Item getItemById(String itemId);
	
	public ArrayList<Item> findItem(Item toFind);
	
	public int getItemCount(int whatToGet);

}
