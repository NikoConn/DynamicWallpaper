package com.nikoconn.dynamicwallpaper;
import java.io.IOException;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class WallpaperChanger {  
	
	String os;
	
	public WallpaperChanger() {
		this.os = System.getProperty("os.name").toLowerCase();
		Log.print("SO " + os + " detected");
	}
	
	public static interface User32 extends Library {
		User32 INSTANCE = (User32) Native.loadLibrary("user32",User32.class,W32APIOptions.DEFAULT_OPTIONS);        
		boolean SystemParametersInfo (int one, int two, String s ,int three);         
	}
 
	public void setWallpaper(String path) {
		if(os.contains("windows")) {
			User32.INSTANCE.SystemParametersInfo(0x0014, 0, path , 1);
			Log.print("Wallpaper set to " + path);
		}else if(os.contains("mac")) {
			String as[] = {
		            "osascript", 
		            "-e", "tell application \"Finder\"", 
		            "-e", "set desktop picture to POSIX file \"" + path + "\"",
		            "-e", "end tell"
		    };
		    Runtime runtime = Runtime.getRuntime();
		    try {
				runtime.exec(as);
				Log.print("Wallpaper set to " + path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("Os not compatible");
			Log.print(os + " is not compatible with current version");
			System.exit(0);
		}
	}
}