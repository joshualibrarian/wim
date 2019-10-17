package zone.wim.library;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

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
	    	try {
	    		URL url = TrayMenu.class.getResource("icon.png");
	    		System.out.println(url.toString());
	    		ImageIcon icon = new ImageIcon(url, "WIM");
		    	trayIcon = new TrayIcon(icon.getImage(), "WIM");
//		    	trayIcon.addActionListener(event -> Platform.runLater(this::doSomething));
	            tray.add(trayIcon);

	            MenuItem openItem = new MenuItem("hello, world");
//	            openItem.addActionListener(event -> Platform.runLater(this::doSomething));

	            MenuItem exitItem = new MenuItem("Exit");
	            exitItem.addActionListener(event -> System.exit(0));
	            
	            final PopupMenu popup = new PopupMenu();
	            popup.add(openItem);
	            popup.addSeparator();
	            popup.add(exitItem);
	            trayIcon.setPopupMenu(popup);
	    	} catch (AWTException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public void destroy() {
		
	}
}
