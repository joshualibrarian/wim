package zone.wim.item;

import zone.wim.exception.AddressException.Invalid;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.token.Address;
import zone.wim.token.ItemType;

public class DomainedUser extends Signer {

	public DomainedUser(Address address, Signer creator) throws SignersOnly {
		super(address);
	}

	@Override
	public Address generateAddress(String name, ItemType type) throws Invalid {
		// TODO Auto-generated method stub
		return null;
	}
	
}
