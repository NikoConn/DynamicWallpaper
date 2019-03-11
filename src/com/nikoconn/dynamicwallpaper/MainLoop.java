package com.nikoconn.dynamicwallpaper;

import java.io.File;
import java.util.Calendar;


public class MainLoop {
	private File[] day;
	private File[] night;
	private File[] set;
	private File[] rise;
	
	private WallpaperChanger wallpaperChanger;
	
	public Request request;
	
	
	public MainLoop(File[] day, File[] night, File[] set, File[] rise, Request request) {
		this.day = day; this.night = night; this.set = set; this.rise = rise;
		this.request = request;
		this.wallpaperChanger = new WallpaperChanger();
	}
	
	private static int getIndex(int nFiles, long timeNow, long timeFirst, long timeLast) {
		long timeTotalSteps = timeLast-timeFirst;
		long step = timeTotalSteps/nFiles;
		long stepNow = timeNow - timeFirst;
				
		long res = stepNow/step;
		
		return (int) res;
	}
	
	private static long getSleep(int nFiles, long timeFirst, long timeLast, long timeNow) {
		long aux = timeLast-timeFirst;
		aux = aux/nFiles;
		long aux2 = (timeNow-timeFirst)%aux;
		
		return aux-aux2;
	}
	
	public void runMainLoop() throws InterruptedException {
			Calendar now = Calendar.getInstance();
			
			Calendar Rise = request.getRise();
			Calendar Set = request.getSet();
			
			long hourNow = now.getTimeInMillis();
			long hourRise = Rise.getTimeInMillis();
			long hourSet = Set.getTimeInMillis();
						
			long halfhour = 1800000;
			
			File[] files;
			long timeFirst, timeLast;
			long timeNow = now.getTimeInMillis();
			
			if(hourNow + halfhour >= hourRise && hourNow < hourRise) { //Rise
				Log.print("Rise");
				files = rise;
				timeFirst = hourRise-halfhour;
				timeLast = hourRise;
			}else if(hourNow + halfhour >= hourSet && hourNow < hourSet){ //Set
				Log.print("Set");
				files = set;
				timeFirst = hourSet-halfhour;
				timeLast = hourSet;
			}else if(hourNow >= hourRise && hourNow < hourSet) { //Day
				Log.print("Day");
				files = day;
				timeFirst = hourRise;
				timeLast = hourSet-halfhour;
			}else { //Night
				Log.print("Night");
				if(Rise.compareTo(Set) < 0) { 
					Rise.add(Calendar.DAY_OF_MONTH, 1);
					request.setRise(Rise);
					hourRise = Rise.getTimeInMillis();
				}
				files = night;
				timeFirst = hourSet;
				timeLast = hourRise;
			}
			
			
			int nFiles = files.length;
			int index = getIndex(nFiles, timeNow, timeFirst, timeLast);
			Log.print("File index: " + index + ", timeNow: " + timeNow + ", timeFirst: " + timeFirst + ", timeLast: " + timeLast);
			
			
			wallpaperChanger.setWallpaper(files[index].getAbsolutePath());
			
			long sleep = getSleep(nFiles, timeFirst, timeLast, timeNow);
			Log.print("Sleeping for " + sleep + " ms");
			Thread.sleep(sleep);
			
			runMainLoop();
		
	}
	
}
