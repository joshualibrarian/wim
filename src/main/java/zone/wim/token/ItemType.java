package zone.wim.token;

import java.util.List;
import zone.wim.item.*;

import zone.wim.exception.TypeException;

public interface ItemType extends Type {
	public static Type parse(String type) throws Throwable {
		List<Token> results = Token.parse(type, ItemType.class);
		if (results.isEmpty()) {
			throw new TypeException.Unknown(type);
		} else if (results.size() > 1) {
			throw new TypeException.Duplicate(type);
		}
		return (ItemType)results.get(0);
	}
	
	public default Class<? extends Item> getClazz() {
		return Item.class;
	}
}
