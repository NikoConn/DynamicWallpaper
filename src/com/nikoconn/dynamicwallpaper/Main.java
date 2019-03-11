package com.nikoconn.dynamicwallpaper;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		try {
		File dataFolder = new File("data");
		dataFolder.mkdir();
		
		Log.print("New session");
		File f = new File("data/data.dat");
		
		//Set rise and set by request. Ask for place if not saved in data.dat
		Request request;
		if(!f.exists()) {
			Log.print("Data file not found, asking for place and creating file");
			Scanner sc = new Scanner(System.in);
			System.out.println("City/Postal Code:");
			String place = sc.nextLine();
			
			request = new Request(place);
			
			String[] places = request.getPlaces();
			System.out.println("Select one:");
			for(int i = 0; i < places.length; i++) {
				System.out.println(i + ") " + places[i]);
			}
			Integer placeIndex = sc.nextInt();
			request.setPlace(placeIndex);
			
			PrintWriter pw = new PrintWriter(f);
			pw.println("Place:" + place);
			pw.println("PlaceIndex:" + placeIndex);
			pw.close();
			Log.print("Info saved succesfully, notifying the user");
			System.out.println("Data saved sucessfully, you can henceforth run the file \"DynamicWallpaper.jar\" if don't want command prompt to be opened. Please, restart the program");
			System.out.println("Press enter to exit"); 
			new Scanner(System.in).nextLine();
			System.exit(0);
		} else {
			Log.print("Data file found, getting place and index");
			Scanner sc = new Scanner(f);
			String place = null;
			Integer index = null;
			
			while(sc.hasNext()) {
				String line = sc.nextLine();
				String[] data = line.split(":");
				if(data[0].equals("Place")) { place = data[1]; }
				else if(data[0].equals("PlaceIndex")) {index = Integer.valueOf(data[1]);}
			}
			
			sc.close();
			
			request = new Request(place, index);
			Log.print("Info readed succesfully");
		}
		
		
		//Check folders
		Log.print("Checking folders");
		File imagesFolder = new File("Images");

		File setFolder = new File(imagesFolder, "Set");
		File riseFolder = new File(imagesFolder, "Rise");
		File dayFolder = new File(imagesFolder, "Day");
		File nightFolder = new File(imagesFolder, "Night");
		
		boolean allFoldersAreCreated = true;
		File[] foldersRequired = {imagesFolder, setFolder, riseFolder, dayFolder, nightFolder};
		for(int i = 0; i < foldersRequired.length; i++) {
			File folder = foldersRequired[i];
			if(!isDirectoryAndExists(folder)) { 
				if(folder.mkdir()) {
					Log.print("Folder \"" + folder.getAbsolutePath() + "\" not found, created");
					allFoldersAreCreated = false;
				}else {
					throw new Exception("Error creating folder " + folder.getAbsolutePath());
				}
			}
		}
		
		//set array of files of each folder
		File[] day = dayFolder.listFiles();
		File[] night = nightFolder.listFiles();
		File[] rise = riseFolder.listFiles();
		File[] set = setFolder.listFiles();
		
		//Check all folders are created and have files
		if(!allFoldersAreCreated || day.length == 0 || night.length == 0 || rise.length == 0 || set.length == 0) { 
			Log.print("There's one or more empty folder. Exit");
			System.out.println("Place images in folders and restart the program\nPress enter to exit"); 
			new Scanner(System.in).nextLine(); 
			System.exit(0);
		}
		
		Log.print("Folders ok");
		MainLoop mainLoop = new MainLoop(day, night, set, rise, request);
		TrayHelper.setTray(mainLoop);
		Log.print("Starting main loop");
		mainLoop.runMainLoop();
		}catch(Exception e) {Log.print(e.toString());}
	}
	
	
	private static boolean isDirectoryAndExists(File f) {
		return f.isDirectory() && f.exists();
	}
	
}
