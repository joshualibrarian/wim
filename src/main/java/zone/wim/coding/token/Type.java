package zone.wim.coding.token;

import zone.wim.coding.SelfCoding;

public interface Type extends SelfCoding {
	
	public static Type decode(String type) throws Exception {
		return (Type)SelfCoding.decode(type, Type.class);
	}
	
}
