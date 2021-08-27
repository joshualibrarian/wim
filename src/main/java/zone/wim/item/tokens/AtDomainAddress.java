package zone.wim.item.tokens;

import java.util.regex.*;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.EncodeAdapter;
import zone.wim.coding.token.Address;
import zone.wim.item.*;
import zone.wim.coding.token.AddressException.*;
import zone.wim.token.ItemType;

@Log @Getter @Setter
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
		public static Pattern PATTERN = Pattern.compile(COMPLETE, Pattern.UNICODE_CHARACTER_CLASS);
	}

	public static AtDomainAddress decode(String text) throws Invalid {
		return new AtDomainAddress(text);
	}

	public static AtDomainAddress decode(DecodeAdapter adapter) throws Invalid {
		String text = adapter.expectString();
		return new AtDomainAddress(text);
	}

	@Override
	public void encode(EncodeAdapter adapter) {

	}

	public static AtDomainAddress parse(String address) throws Exception {
		return new AtDomainAddress(address);
	}
	
	private String address;
	private String sitePart;
	private String userPart;
	private String thingPart;

	public AtDomainAddress(String address) throws Invalid {
		log.entering(this.getClass().getCanonicalName(), "AtDomainAddress(String)");
		Matcher m = Regex.PATTERN.matcher(address);
		
		if (!m.matches()) {
			throw new Invalid(address);
		}
		
		sitePart = m.group("site");
		userPart = m.group("user");
		thingPart = m.group("thing");
		this.address = address;
	}

	public AtDomainAddress(String site, String user, String thing) {
		this.sitePart = site;
		this.userPart = user;
		this.thingPart = thing;
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
	public boolean validate(String addressToValidate) {
		return false;
	}

}
