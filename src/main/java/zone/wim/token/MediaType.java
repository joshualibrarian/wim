package zone.wim.token;

import org.apache.tika.mime.MimeType;
import zone.wim.exception.*;

public class MediaType implements Type {
	String text;
	
	public static MediaType parse(String typeText) throws Throwable {
		if(MimeType.isValid(typeText)) {
			return new MediaType(typeText);
		} else throw new TypeException.Invalid(typeText);
	}
	
	public MediaType(String typeText) {
		this.text = typeText;
	}
}
