package zone.wim.codec;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public abstract class Encoder {
	
	protected abstract CoderResult encodeLoop(Object in, ByteBuffer out);
	
}
