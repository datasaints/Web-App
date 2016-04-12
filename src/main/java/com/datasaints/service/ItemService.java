package com.datasaints.service;

import java.util.ArrayList;

import com.datasaints.domain.Item;

public interface ItemService {
	public Item addItem(Item item);

	void deleteItem(String itemId);

	public String checkAddItemArguments(Item item);
	
	public Item getItemById(String itemId);
	
	public ArrayList<Item> findItem(Item toFind);
	
	public int getItemCount(int whatToGet);
}
