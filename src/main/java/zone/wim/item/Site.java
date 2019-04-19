package zone.wim.item;

import java.util.List;

import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.token.Address;

public class Site extends Signer {

	List<User> users;
	List<Host> hosts;
	
	public Site(Address address) throws SignersOnly {
		super(address);
	}

}
