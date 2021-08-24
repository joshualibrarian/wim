package zone.wim.item;

import java.util.List;

import javafx.beans.property.ListProperty;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.coding.token.Address;

public class Site extends Signer implements Group {

	List<User> users;
	List<Host> hosts;
	
	public Site(Address address) throws SignersOnly {
		super(address);
	}

//	public User createUser(String name) {
		
//	}
	
	@Override
	public ListProperty<Item> contentsProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> getContents() {
		// TODO Auto-generated method stub
		return null;
	}

}
