package zone.wim.token;

import java.util.List;
import zone.wim.item.*;

import zone.wim.exception.TypeException;

public interface ItemType extends Type {
	public static Type parse(String type) throws Exception {
		return (ItemType)Token.parse(type, ItemType.class);
	}
	
	public default Class<? extends Item> getClazz() {
		return Item.class;
	}
	
}
