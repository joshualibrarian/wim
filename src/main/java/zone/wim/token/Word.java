package zone.wim.token;

import zone.wim.exception.TypeException;

public class Word implements Token {
	
	public static Word parse(String className) throws Throwable {
		// TODO stub
		throw new TypeException.Invalid(className);
	}

	@Override
	public String get() {
		// TODO Auto-generated method stub
		return null;
	}
}