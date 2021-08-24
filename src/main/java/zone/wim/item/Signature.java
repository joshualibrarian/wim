package zone.wim.item;

import zone.wim.coding.SelfCoding;
import zone.wim.exception.TypeException;

public class Signature implements SelfCoding {
	
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


}
