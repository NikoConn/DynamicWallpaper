package com.nikoconn.dynamicwallpaper;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TrayHelper {
	
	public static void setTray(MainLoop ml) {
		if (SystemTray.isSupported()) {
			
		    SystemTray tray = SystemTray.getSystemTray();
		    Image image = Toolkit.getDefaultToolkit().getImage("data/ico.gif");
		    PopupMenu popup = new PopupMenu();

		    MenuItem updateItem = new MenuItem("Update Rise and Set Hours");
		    updateItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						ml.request.update();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		    popup.add(updateItem); 
		    
		    MenuItem exitItem = new MenuItem("Exit");
		    exitItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});
		    popup.add(exitItem);
		    
		   TrayIcon trayIcon = new TrayIcon(image, "Dynamic Wallpaper", popup);
		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		        Log.print(e.toString());
		    }
	}
	}
}
