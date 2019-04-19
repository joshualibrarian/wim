package zone.wim.item;

import java.util.*;

import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.token.Address;

public class User extends Signer {

	private Site homeServer;
	private List<Site> friendlyServers;
	private List<Host> friendlyHosts;
	
	public User(Address address) throws SignersOnly {
		super(address);
	}

}
