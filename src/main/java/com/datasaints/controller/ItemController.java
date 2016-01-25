package com.datasaints.controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.datasaints.domain.Item;

@RestController
@EnableAutoConfiguration
public class ItemController {
	
	@RequestMapping("/test")
	public Item item() {
		Item item = new Item();
		item.setItemId(1);
		item.setCheckIn(new Date());
		item.setCheckOut(new Date());
		item.setEmployeeId(2);
		item.setLastCalibrated(new Date());
		
		return item;
	}
	
	@RequestMapping(value = "/getItems", method = RequestMethod.POST)
    public ArrayList<Item> getItems() {
		System.out.println("Got in GetItems");

		ArrayList<Item> itemResponse = new ArrayList<Item>();
		
		Item item = new Item();
		item.setItemId(1);
		item.setCheckIn(new Date());
		item.setCheckOut(new Date());
		item.setEmployeeId(2);
		item.setLastCalibrated(new Date());
		
		Item item2 = new Item();
		item2.setItemId(2);
		item2.setCheckIn(new Date());
		item2.setCheckOut(new Date());
		item2.setEmployeeId(3);
		item2.setLastCalibrated(new Date());
		
		itemResponse.add(item);
		itemResponse.add(item2);

		return itemResponse;
		
		
/*
        GetRecipesResponse recipes = new GetRecipesResponse();
        recipes.setRecipes(recipeService.getRecipes(criteria));
        
        if (recipes.getRecipes() == null || recipes.getRecipes().isEmpty()) {
        	throw new NoRecipesFoundException("No recipes found matching your criteria");
        }

        return recipes;*/
    }
}
