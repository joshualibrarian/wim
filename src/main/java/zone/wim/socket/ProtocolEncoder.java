package zone.wim.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import zone.wim.coding.EncodeAdapter;
import zone.wim.socket.protocol.ProtocolComponent;

public class ProtocolEncoder extends MessageToByteEncoder<ProtocolComponent> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ProtocolComponent msg, ByteBuf out) throws Exception {
		EncodeAdapter adapter = new EncodeAdapter(msg);
		adapter.encode(out);
	}

}
