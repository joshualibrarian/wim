package zone.wim.item;

import java.util.*;
import java.net.InetAddress;

import javax.persistence.Entity;
import zone.wim.exception.AddressException.*;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.token.Address;
import zone.wim.token.HostAddress;
import zone.wim.token.ItemType;
import zone.wim.token.Type;

@Entity
public class Host extends Signer {
	
	public static Host create(InetAddress host) {
		HostAddress address = new HostAddress(host);
		Host item = null;
		try {
			item = new Host(address);
			item.host = host;

		} catch (SignersOnly e) {
//			e.printStackTrace();
		}
		return item;
	}
	
	transient InetAddress host;
	List<Site> hostedServers;
	
	private Host(HostAddress address) throws SignersOnly {
		super(address);
	}

}
