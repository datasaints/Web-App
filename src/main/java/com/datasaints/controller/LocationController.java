package com.datasaints.controller;

import java.util.ArrayList;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datasaints.domain.Location;
import com.datasaints.dao.JDBCConnect;

@RestController
@EnableAutoConfiguration
public class LocationController {
	private JDBCConnect conn = new JDBCConnect();
	
	@RequestMapping(value = "/getLocations", method = RequestMethod.GET)
	public ArrayList<Location> getItems() {
		System.out.println("Called get locations");
		conn.populateLocations();
		
		return conn.getLocations();
	}
}
