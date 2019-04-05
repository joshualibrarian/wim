package zone.wim.item;

import zone.wim.item.*;

import zone.wim.exception.ItemException.*;
import zone.wim.exception.AddressException.*;
import zone.wim.token.*;

public class DomainedServer extends Signer {

	public DomainedServer(Address address) throws SignersOnly {
		super(address);
	}
	
	public Signer createUser(String username) {
		Signer user = null;
		
		String fullUserAddress = username + this.address.get();
		DomainedAddress userAddress;
		try {
			userAddress = DomainedAddress.parse(username);
			user = new DomainedUser(userAddress, this);

		} catch (SignersOnly e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} 

		return user;
		
	}

	@Override
	public Address generateAddress(String name, ItemType type) throws Invalid {
		return new DomainedAddress(name);
	}

}
