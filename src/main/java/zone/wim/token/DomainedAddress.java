package zone.wim.token;

import java.io.Serializable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zone.wim.exception.AddressException.*;

public class DomainedAddress implements Address {

	public static class Regex {
		public static String NEGATED_SET = "[^" + Address.Regex.RESERVED_CHARS + "\\/@]+";
		public static String USERNAME 	= NEGATED_SET;
		public static String DOMAIN 	= NEGATED_SET;
		public static String SERVER		= "@" + DOMAIN;
		public static String USER		= "(?:" + USERNAME + ")?" + SERVER;
		public static String CAPTURE 	= "(?<user>" + USER + ")" 
										+ "(?:\\/(?<path>" + USERNAME + "))?";
		public static Pattern ADDRESS 	= Pattern.compile(CAPTURE, Pattern.UNICODE_CHARACTER_CLASS);
	}
	
	public static DomainedAddress parse(String address) throws Throwable {
		LOGGER.info("DomainedAddress.parse(" + address + ")");
		return new DomainedAddress(address);
	}
	
	private String address;
	private transient String userPart;
	private transient String pathPart;
	
	public DomainedAddress(String address) throws Invalid {
		LOGGER.info("DomainedAddress(" + address + ")");
		Matcher m = Regex.ADDRESS.matcher(address);
		
		if (!m.matches()) {
			throw new Invalid(address);
		}
		
		pathPart = m.group("path");
		userPart = m.group("user");
		this.address = address;
	}
	
	@Override
	public String get() {
		return address;
	}

	@Override
	public String getUserPart() {
		return userPart;
	}

	@Override
	public String getPathPart() {
		return pathPart;
	}

	@Override
	public boolean validate(String addressToValidate) {
		// TODO Auto-generated method stub
		return false;
	}

}
