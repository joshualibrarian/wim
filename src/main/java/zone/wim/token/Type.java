package zone.wim.token;

import java.util.List;
import zone.wim.exception.*;

public interface Type extends Token {
	
	public static Type parse(String type) throws Throwable {
		List<Token> results = Token.parse(type, Type.class);
		if (results.isEmpty()) {
			throw new TypeException.Unknown(type);
		} else if (results.size() > 1) {
			throw new TypeException.Duplicate(type);
		}
		return (Type)results.get(0);
	}
	
}
