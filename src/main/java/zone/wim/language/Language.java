package zone.wim.language;

import java.util.Locale;
import zone.wim.item.*;
import zone.wim.coding.token.Address;
import zone.wim.ui.ItemUserInterface;
import zone.wim.ui.LanguagePane;

public class Language extends BaseItem {

	Locale locale;	
	
	public Language(Address address, Signer creator, Locale locale) {
		super(address, creator);
		this.locale = locale;
	}
	
	public Locale getLocale() {
		return locale;
	}

	@Override
	public ItemUserInterface getUserInterface() {
		getUserInterface().setPane(new LanguagePane(this));
		return userInterface;
	}
}
