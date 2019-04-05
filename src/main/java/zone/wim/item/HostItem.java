package zone.wim.item;

import java.net.InetAddress;

import javax.persistence.Entity;
import zone.wim.exception.AddressException.*;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.token.Address;
import zone.wim.token.HostAddress;
import zone.wim.token.ItemType;
import zone.wim.token.Type;

@Entity
public class HostItem extends Signer {
	
	transient InetAddress host;
	
	public HostItem(InetAddress host) throws SignersOnly {
		super(new HostAddress(host));
		this.host = host;
	}

	@Override
	public Address generateAddress(String name, ItemType type) throws Invalid {
		try {
			return HostAddress.parse(name);
		} catch (Throwable e) {
			throw new Invalid(e);
		}
	}
}
