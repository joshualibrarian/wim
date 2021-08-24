package zone.wim.token;

import java.net.InetAddress;

import java.net.UnknownHostException;
import java.util.logging.Logger;
import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.coding.EncodeAdapter;
import zone.wim.item.Signer;
import zone.wim.coding.token.Address;
import zone.wim.coding.token.AddressException.Invalid;

@EmbeddedOnly
public class HostAddress implements Address {
	public static Logger LOGGER = Logger.getLogger(Address.class.getCanonicalName());
	
	public static HostAddress parse(String tokenText) throws Exception {
		// TODO add actual validation to be sure we have an IP
		LOGGER.info("HostAddress(" + tokenText + ")");
		return new HostAddress(InetAddress.getByName(tokenText));
	}

	private String text;
	
	public HostAddress(InetAddress host) {
		text = host.getHostAddress();
	}

	@Override
	public boolean validate(String addressToValidate) {
		// TODO
		return false;
	}

	@Override
	public Address generate(Signer creator, String name, ItemType type) throws Invalid {
		InetAddress a;
		
		try {
			a = InetAddress.getByName(name);
			return new HostAddress(a);
		} catch (UnknownHostException e) {
			throw new Invalid(e);
		}
	}

	@Override
	public String sitePart() {
		return null;
	}

	@Override
	public void encode(EncodeAdapter adapter) {

	}
}
