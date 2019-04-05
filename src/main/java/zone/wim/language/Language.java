package zone.wim.language;

import java.util.Locale;
import zone.wim.item.*;
import zone.wim.token.Address;

public class Language extends Item {

	Locale locale;
	
	public Language(Address address, Signer creator, Locale locale) {
		super(address, creator);
		this.locale = locale;
	}
	
	public Locale getLocale() {
		return locale;
	}
}
