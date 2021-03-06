package com.datasaints.service;

import java.util.ArrayList;

import com.datasaints.domain.Item;

public interface ItemService {
	public boolean addItem(Item item);

	public boolean deleteItem(String id);

	// public String checkAddItemArguments(Item item);
	
	public Item getItem(String id);
	
	public boolean updateItemLocation(String id, String newLocation);
	
	public boolean updateItem(Item item);
	
	public ArrayList<Item> findItem(Item toFind);
	
	public int getItemCount(int whatToGet);
	
	public int getItemCountByLocation(String location, int whatToGet);
}
