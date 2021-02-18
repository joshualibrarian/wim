package zone.wim.codec;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public abstract class Decoder {

	protected abstract CoderResult decodeLoop(ByteBuffer in, Buffer out);
	
}
