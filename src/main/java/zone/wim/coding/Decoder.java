package zone.wim.coding;

import java.nio.ByteBuffer;

public abstract class Decoder extends Coder {

	protected Decoder(Codec codec) {
		super(codec);
	}

	protected abstract CoderResult decode(boolean endOfInput);
	protected abstract CoderResult decodeLoop();

}
