package zone.wim.item.tokens;

import zone.wim.item.Item;

public class Security extends ControlCharToken {
	public static Security PRIVATE = new Security(0);
	int value;

	public Security() {
		this(0);
	}
	public Security(int value) {
		this.value = value;
	}
	
	public String toString() {
		String r = Item.SECURITY_CHAR + Integer.toHexString(value);
		return r;
	}
}
