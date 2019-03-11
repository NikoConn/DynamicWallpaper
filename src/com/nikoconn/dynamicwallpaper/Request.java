package com.nikoconn.dynamicwallpaper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Request {
	private static String api_Key = "";
	private static String urlString = "https://api.opencagedata.com/geocode/v1/json?q=%s&key=" + api_Key;
	
	private String place;
	private Integer placeResult;
	
	private JsonObject root;
	
	private Calendar rise = null;
	private Calendar set = null;

	
	public Request(String place) throws IOException {
		setRoot(place);
		this.place = place;
	}
	
	public Request(String place, Integer index) throws IOException {
		setRoot(place);
		setPlace(index);
		this.place = place;
	}
	
	public void update() throws IOException {
		setRoot(place);
		updateRise();
		updateSet();
	}
	
	public void updateRise() {
		try {
			if(placeResult == null) {throw new Exception("Set place first");}
			JsonObject sun = root.get("results").getAsJsonArray()
					.get(0).getAsJsonObject()
					.get("annotations").getAsJsonObject()
					.get("sun").getAsJsonObject();
		
			long riseLong = sun.get("rise").getAsJsonObject().get("apparent").getAsLong();
		
	    	rise = Calendar.getInstance();
	    	rise.setTimeInMillis(riseLong*1000);
	    
		} catch (Exception e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateSet() {
		try {
			if(placeResult == null) { throw new Exception("Set place first"); }
			
			JsonObject sun = root.get("results").getAsJsonArray()
					.get(0).getAsJsonObject()
					.get("annotations").getAsJsonObject()
					.get("sun").getAsJsonObject();
			
			long setLong = sun.get("set").getAsJsonObject().get("apparent").getAsLong();
			
		    set = Calendar.getInstance();
		    set.setTimeInMillis(setLong*1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	
	public void setPlace(int index) {
		this.placeResult = index;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	private void setRoot(String place) throws IOException {
		URL url = new URL(String.format(urlString, place));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		StringBuilder result = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String line;
	    while ((line = rd.readLine()) != null) {
	       result.append(line);
	    }
	    rd.close();
	    
		JsonParser parser = new JsonParser();
		root = parser.parse(result.toString()).getAsJsonObject();
	}
	
	public String[] getPlaces() {
		JsonArray placesJsonArray = root.get("results").getAsJsonArray();
		String[] places = new String[placesJsonArray.size()];
		
		for(int i = 0; i < places.length; i++) {
			JsonObject place = placesJsonArray.get(i).getAsJsonObject();
			places[i] = place.get("formatted").getAsString();
		}
		
		return places;
	}
	
	
	
	public Calendar getRise() {
		if(rise == null) {updateRise();}
		return this.rise;
	}
	
	public void setRise(Calendar rise) {
		this.rise = rise;
	}
	
	
	public Calendar getSet() {
		if(set == null) {updateSet();}
		return this.set;
	}
	
	public void setSet(Calendar set) {
		this.set = set;
	}
	
}
