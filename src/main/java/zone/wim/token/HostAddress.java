package zone.wim.token;

import java.net.InetAddress;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.util.logging.Logger;

import javax.jdo.annotations.EmbeddedOnly;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
@EmbeddedOnly
public class HostAddress implements Address {
	public static Logger LOGGER = Logger.getLogger(Address.class.getCanonicalName());
	
	public static HostAddress parse(String tokenText) throws Throwable {
		// TODO add actual validation to be sure we have an IP
		LOGGER.info("HostAddress(" + tokenText + ")");
		return new HostAddress(InetAddress.getByName(tokenText));
	}

	@OneToOne
	private String text;
	
	public HostAddress(InetAddress host) {
		text = host.getHostAddress();
	}
	
	@Override
	public String get() {
		return text;
	}

	@Override
	public String getUserPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(String addressToValidate) {
		return false;
	}

}
