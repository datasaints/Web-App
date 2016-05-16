package com.datasaints.domain;

public class Location {
	int id;
	String name;

   public Location() {
	   this.id = 0;
	   this.name = "";
   }
   
   public Location(int id, String name) {
	   this.id = id;
	   this.name = name;
   }
   
   // Getters
   
   public int getId() {
	   return this.id;
   }
   
   public String getName() {
	   return this.name;
   }
   
   // Setters
   
   public void setId(int id) {
	   this.id = id;
   }
   
   public void setName(String name) {
	   this.name = name;
   }
}
