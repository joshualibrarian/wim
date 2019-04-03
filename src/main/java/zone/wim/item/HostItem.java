package zone.wim.item;

import java.net.InetAddress;
import javax.persistence.Entity;

import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.token.HostAddress;

@Entity
public class HostItem extends Signer {
	
	transient InetAddress host;
	
	public HostItem(InetAddress host) throws SignersOnly {
		super(new HostAddress(host));
		this.host = host;
	}
}
