package zone.wim.token;

import zone.wim.coding.SelfCoding;
import zone.wim.exception.TypeException;

public class ComponentReference implements SelfCoding {
	
	public static ComponentReference parse(String className) throws Throwable {
		// TODO stub
		throw new TypeException.Invalid(className);
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
}
