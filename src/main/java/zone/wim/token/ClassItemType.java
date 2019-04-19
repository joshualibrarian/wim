package zone.wim.token;

import zone.wim.exception.TypeException.*;
import zone.wim.item.*;

public class ClassItemType implements ItemType {
	
	public static ClassItemType parse(String className) throws Throwable {
		return new ClassItemType(className);
	}

	private Class<? extends Item> clazz;

	public ClassItemType(Class<? extends Item> clazz) {
		this.clazz = clazz;
	}
	
	public ClassItemType(String className) throws Invalid {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Item> clazz = 
					(Class<? extends Item>) Class.forName(className);
			this.clazz = clazz;

		} catch (ClassNotFoundException e) {
			throw new Invalid(e);
		}
	}
	
	@Override
	public Class<? extends Item> getClazz() {
		return clazz;
	}

	@Override
	public String get() {
		return clazz.getCanonicalName();
	} 
}
