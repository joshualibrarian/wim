package zone.wim.token;

import java.util.regex.*;

import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.item.*;
import zone.wim.token.AddressException.*;

@EmbeddedOnly
public class AtDomainAddress implements Address {

	public static String SITE_CHAR = "@";
	public static String THING_CHAR = "/";
	
	public static class Regex {
		public static String SITE_CHAR  = AtDomainAddress.SITE_CHAR;
		public static String THING_CHAR = "\\" + AtDomainAddress.THING_CHAR;
		public static String NEGATED 	= "[^" + Address.Regex.RESERVED_CHARS 
										+ SITE_CHAR + THING_CHAR + "]+";
		public static String USERNAME 	= NEGATED;
		public static String DOMAIN 	= NEGATED;
		public static String SITE		= "(?<site>" + SITE_CHAR + DOMAIN + ")";
		public static String USER		= "(?<user>" + USERNAME + ")?";
		public static String THING		= "(?:" + THING_CHAR + "(?<thing>" + NEGATED + "))?";
		public static String COMPLETE 	= USER + SITE + THING + "\\b";
		public static Pattern ADDRESS 	= Pattern.compile(COMPLETE, Pattern.UNICODE_CHARACTER_CLASS);
	}
	
	public static AtDomainAddress parse(String address) throws Exception {
		LOGGER.info("AtAddress.parse(" + address + ")");
		return new AtDomainAddress(address);
	}
	
	private String address;
	private String sitePart;
	private String userPart;
	private String thingPart;
	
	public AtDomainAddress(String address) throws Invalid {
		LOGGER.info("DomainedAddress(" + address + ")");
		Matcher m = Regex.ADDRESS.matcher(address);
		
		if (!m.matches()) {
			throw new Invalid(address);
		}
		
		sitePart = m.group("site");
		thingPart = m.group("thing");
		userPart = m.group("user");
		this.address = address;
	}

	@Override
	public AtDomainAddress generate(Signer creator, String name, ItemType type) throws Invalid {
		String a = null;
		if (creator instanceof Host) {
			if (type.getClazz().equals(Site.class)) {
				// TODO: this is where we check with every known peer 
				// TODO: on widest setting to confirm uniqueness
				a = SITE_CHAR + name;
				
			} else {
				throw new Invalid("hosts many only create sites");
			}
		} else if (creator instanceof Site) {
			if (type.getClazz().equals(User.class)) {
				a = name + creator.getAddress().getText();
			} 
		}
		
		if (a == null) {
			a = creator.getAddress().getText() + THING_CHAR + name;
		}
		
		return new AtDomainAddress(a);
	}

	@Override
	public String getText() {
		return address;
	}
	
	@Override
	public void setText(String text) {
		this.address = text;
	}

	@Override
	public String getSitePart() {
		return sitePart;
	}
	
	@Override
	public String getUserPart() {
		return userPart;
	}

	@Override
	public String getThingPart() {
		return thingPart;
	}

	@Override
	public boolean validate(String addressToValidate) {
		return false;
	}

}
