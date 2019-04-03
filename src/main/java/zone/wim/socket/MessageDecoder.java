package zone.wim.socket;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class MessageDecoder extends DelimiterBasedFrameDecoder {

	public MessageDecoder(int maxFrameLength, ByteBuf delimiter) {
		super(maxFrameLength, delimiter);
		// TODO Auto-generated constructor stub
	}
	
}
