package zone.wim.token;

import zone.wim.exception.TypeException;

public class ComponentReference implements Token {
	
	public static ComponentReference parse(String className) throws Throwable {
		// TODO stub
		throw new TypeException.Invalid(className);
	}
}
