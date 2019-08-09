package zone.wim.item;

import zone.wim.exception.TypeException;
import zone.wim.token.Token;

public class Signature implements Token {
	
	private String text;
	private byte[] bytes;
	private Signature signature;
	
	public static Signature parse(String className) throws Throwable {
		// TODO stub
		throw new TypeException.Invalid(className);
	}

	public Signature(String text) {
		this.text = text;
	}
	
	@Override
	public String text() {
		return text;
	}

}
