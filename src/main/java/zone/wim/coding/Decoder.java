package zone.wim.coding;

import lombok.Setter;

import java.nio.ByteBuffer;

public abstract class Decoder<ST, DT> extends Coder {

	ST src;
	DT dst;
	protected DecodeAdapter adapter;
	@Setter protected EscapeHandler escapeHandler = null;

	protected Decoder(Codec codec) {
		super(codec);
	}

//	protected abstract CoderResult shouldStop(Object lastDecoded);

	protected abstract CoderResult decode(boolean endOfInput);
	protected abstract CoderResult decodeLoop();

}
