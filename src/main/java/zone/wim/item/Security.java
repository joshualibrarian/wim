package zone.wim.item;

public enum Security {
	PUBLIC(0xFF), PRIVATE(0);
	
	int value;
	
	Security(int value) {
		this.value = value;
	}
	
	public String toString() {
		String r = Item.SECURITY_CHAR + Integer.toHexString(value);
		return r;
	}
}
