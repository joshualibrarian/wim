package zone.wim.token;

import java.util.List;
import zone.wim.exception.*;

public interface Type extends Token {
	
	public static Type parse(String type) throws Exception {
		return (Type)Token.parse(type, Type.class);
	}
	
}
