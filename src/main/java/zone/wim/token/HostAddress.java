package zone.wim.token;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.validator.routines.InetAddressValidator;

import zone.wim.exception.AddressException.Invalid;
import zone.wim.item.Signer;

import java.util.logging.Logger;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.Persistent;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@EmbeddedOnly
public class HostAddress implements Address {
	public static Logger LOGGER = Logger.getLogger(Address.class.getCanonicalName());
	
	public static HostAddress parse(String tokenText) throws Throwable {
		// TODO add actual validation to be sure we have an IP
		LOGGER.info("HostAddress(" + tokenText + ")");
		return new HostAddress(InetAddress.getByName(tokenText));
	}

	@Persistent
	private String text;
	
	public HostAddress(InetAddress host) {
		text = host.getHostAddress();
	}
	
	@Override
	public String text() {
		return text;
	}
	@Override public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getSitePart() {
		return text;
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

}
