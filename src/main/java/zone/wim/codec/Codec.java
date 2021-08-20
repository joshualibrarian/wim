package zone.wim.codec;

import java.nio.ByteOrder;

public abstract class Codec {

	public abstract String canonicalName();
	public abstract String[] aliases();
	public abstract boolean byteOrderSensitive();

	public abstract Decoder decoder();
	public abstract Encoder encoder();

	protected ByteOrder byteOrder = null;

	public Codec(ByteOrder byteOrder) { this.byteOrder = byteOrder; }

	public ByteOrder byteOrder() {
		return byteOrder;
	}
	public void byteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

	//	public abstract Object decode(DecodeAdapter adapter);


	//TODO: all the caching and instance managing thing here
	//
	// Cache of the most-recently-returned charsets,
	// along with the names that were used to find them
	//
	//private static volatile Object[] cache1; // "Level 1" cache
	//private static volatile Object[] cache2; // "Level 2" cache

}