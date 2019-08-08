package zone.wim.language;

import java.util.Locale;

import javafx.scene.layout.Pane;
import zone.wim.item.*;
import zone.wim.token.Address;
import zone.wim.client.LanguagePane;

public class Language extends AbstractItem {

	Locale locale;	
	
	public Language(Address address, Signer creator, Locale locale) {
		super(address, creator);
		this.locale = locale;
	}
	
	public Locale getLocale() {
		return locale;
	}

	@Override
	public Pane getPane() {
		return new LanguagePane(this);
	}
	
}
