package zone.wim.library;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.application.Platform;

public class TrayMenu {
	Library library;
	TrayIcon trayIcon = null;
	SystemTray tray;

	public static TrayMenu init(Library library) {
		
		TrayMenu menu = new TrayMenu(library);
		return menu;
	}
	
	private TrayMenu(Library library) {
		this.library = library;
		initSystemTray();
	}
	
	private void initSystemTray() {
	     if (SystemTray.isSupported()) {
	    	// ensure awt toolkit is initialized.
	    	Toolkit.getDefaultToolkit();

	    	 // set up a system tray icon.
	    	tray = SystemTray.getSystemTray();
	    	URL imageLoc;
	    	Image image;
	    	try {
	    		imageLoc = new URL("icon.png");
	    		image = ImageIO.read(imageLoc);
	    		
		    	trayIcon = new TrayIcon(image);
//		    	trayIcon.addActionListener(event -> Platform.runLater(this::doSomething));
	            tray.add(trayIcon);

	            MenuItem openItem = new MenuItem("hello, world");
//	            openItem.addActionListener(event -> Platform.runLater(this::doSomething));

	            MenuItem exitItem = new MenuItem("Exit");
	            exitItem.addActionListener(event -> {
//	                notificationTimer.cancel();
	                try {
						library.shutdown();
					} catch (Exception e) {
						e.printStackTrace();
					}
	                tray.remove(trayIcon);
	            });
	            
	            final PopupMenu popup = new PopupMenu();
	            popup.add(openItem);
	            popup.addSeparator();
	            popup.add(exitItem);
	            trayIcon.setPopupMenu(popup);
	    	} catch (MalformedURLException e) {
	    		e.printStackTrace();
	    	} catch (IOException e) {
	    		e.printStackTrace();
			} catch (AWTException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public void destroy() {
		
	}
}
