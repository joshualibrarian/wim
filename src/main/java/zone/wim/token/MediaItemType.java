package zone.wim.token;

import org.apache.tika.mime.MimeType;
import zone.wim.exception.*;

public class MediaItemType implements ItemType {
	String text;
	
	public static MediaItemType parse(String typeText) throws Throwable {
		if(MimeType.isValid(typeText)) {
			return new MediaItemType(typeText);
		} else throw new TypeException.Invalid(typeText);
	}
	
	public MediaItemType(String typeText) {
		this.text = typeText;
	}
	
	public String text() {
		return text;
	}
}
