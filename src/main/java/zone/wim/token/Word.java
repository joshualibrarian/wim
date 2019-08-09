package zone.wim.token;

import zone.wim.exception.TypeException;

public abstract class Word implements Token {
	
	public static Word parse(String className) throws Throwable {
		// TODO stub
		throw new TypeException.Invalid(className);
	}

	@Override
	public String text() {
		// TODO Auto-generated method stub
		return null;
	}
}