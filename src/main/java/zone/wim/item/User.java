package zone.wim.item;

import java.util.*;

import javafx.beans.property.ListProperty;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.coding.token.Address;

public class User extends Signer {

	Site homeSite;
	List<Site> knownSites;
	List<Host> knownHosts;
	
	public User(Address address) throws SignersOnly {
		super(address);
	}

	@Override
	public List<Item> getContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListProperty<Item> contentsProperty() {
		// TODO Auto-generated method stub
		return null;
	}

}
