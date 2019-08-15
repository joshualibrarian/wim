package zone.wim.language;

import java.util.Locale;

import javafx.scene.layout.Pane;
import zone.wim.item.*;
import zone.wim.token.Address;
import zone.wim.client.ItemUserInterface;
import zone.wim.client.LanguagePane;

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
		super.getUserInterface().setPane(new LanguagePane(this));
		return userInterface;
	}
}
