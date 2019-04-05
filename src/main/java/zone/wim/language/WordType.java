package zone.wim.language;

import java.util.List;

import zone.wim.exception.TypeException;
import zone.wim.token.*;

public interface WordType extends Type {

	public static Type parse(String type) throws Throwable {
		List<Token> results = Token.parse(type, WordType.class);
		if (results.isEmpty()) {
			throw new TypeException.Unknown(type);
		} else if (results.size() > 1) {
			throw new TypeException.Duplicate(results);
		}
		return (WordType)results.get(0);
	}
	
}
