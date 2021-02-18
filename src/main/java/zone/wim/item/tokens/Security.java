package zone.wim.item.tokens;

import zone.wim.item.Item;

public class Security extends ControlCharToken {	
	int value;
	
	Security(int value) {
		this.value = value;
	}
	
	public String toString() {
		String r = Item.SECURITY_CHAR + Integer.toHexString(value);
		return r;
	}
}
