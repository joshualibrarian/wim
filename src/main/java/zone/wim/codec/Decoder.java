package zone.wim.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CodingErrorAction;

public abstract class Decoder extends Coder {

	protected Decoder(Codec codec) {
		this.codec = codec;
		state = CodingState.RESET;
	}


	protected abstract CoderResult decodeLoop(ByteBuffer in, Object out);
	
}
