package zone.wim.client;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import zone.wim.language.Language;

public class LanguagePane extends VBox {
	private Language language;
	
	private Label name;
	private Label code;
	
	public LanguagePane(Language language) {
		this.language = language;
		
		name = new Label(this.language.getLocale().getDisplayLanguage());
		code = new Label(this.language.getLocale().getISO3Language());
		getChildren().addAll(name, code);
	}
}
