package zone.wim.item;

import java.util.*;

import javafx.beans.property.ListProperty;
import javafx.scene.layout.Pane;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.exception.ItemException.SitesOnly;
import zone.wim.token.Address;

public class User extends Signer {

	Site homeSite;
	List<Site> friendlySites;
	List<Host> friendlyHosts;
	
	public User(Address address) throws SignersOnly {
		super(address);
		
	}

	@Override
	public Pane getPane() {
		// TODO Auto-generated method stub
		return null;
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
