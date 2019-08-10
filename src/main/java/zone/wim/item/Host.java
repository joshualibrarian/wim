package zone.wim.item;

import java.util.*;
import java.util.logging.*;

import java.net.InetAddress;

import javax.jdo.annotations.PersistenceCapable;

import javafx.beans.property.ListProperty;
import javafx.scene.layout.Pane;
import zone.wim.client.HostPane;
import zone.wim.exception.AddressException.*;
import zone.wim.exception.ItemException.*;
import zone.wim.exception.LibraryException;
import zone.wim.exception.LibraryException.*;
import zone.wim.library.Library;
import zone.wim.token.Address;
import zone.wim.token.HostAddress;
import zone.wim.token.ItemType;
import zone.wim.token.Type;

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
	public Pane getPane() {
		return new HostPane(this);
	}

}
