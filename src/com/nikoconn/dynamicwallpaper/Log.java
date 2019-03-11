package com.nikoconn.dynamicwallpaper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Log {
	
	public static void initialise() throws IOException {
	}
	
	public static void print(String s) {
		try {
			File logFile = new File("data/log.txt");
			PrintWriter pw = new PrintWriter(new FileWriter(logFile, true));
			
			Calendar now = Calendar.getInstance();
			
			pw.println(now.getTime() + " - " + s);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
