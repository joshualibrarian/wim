package zone.wim.item;

import java.util.*;

import java.util.logging.*;

import java.net.InetAddress;

import javax.jdo.annotations.PersistenceCapable;

import javafx.beans.property.ListProperty;
import zone.wim.ui.HostPane;
import zone.wim.ui.ItemUserInterface;
import zone.wim.exception.ItemException.*;
import zone.wim.exception.LibraryException.*;
import zone.wim.library.Library;
import zone.wim.token.*;

@PersistenceCapable
public class Host extends Signer implements Group {
	private static Logger LOGGER = Logger.getLogger(Host.class.getCanonicalName());
	
	public static Host create(InetAddress host) {
		HostAddress address = new HostAddress(host);
		Host item = null;
		try {
			item = new Host(address);
			item.host = host;

//			LOGGER.info("new Host item created! " + e.getMessage());

		} catch (SignersOnly e) {
			LOGGER.info("SignersOnly exception: " + e.getMessage());
		}
		return item;
	}
	
	transient InetAddress host;
	List<Site> hostedSites;
	
	private Host(HostAddress address) throws SignersOnly {
		super(address);
	}

	@Override
	public List<Item> getContents() {
		try {
			return Library.instance().getItemsByClass(Item.class);
		} catch (NotInitialized e) {
			// TODO ?
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ListProperty<Item> contentsProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemUserInterface getUserInterface() {
		super.getUserInterface().setPane(new HostPane(this));
		return userInterface;

	}

	
}
