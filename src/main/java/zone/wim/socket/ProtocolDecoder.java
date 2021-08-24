package zone.wim.socket;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import zone.wim.coding.DecodeAdapter;
import zone.wim.socket.protocol.*;

public class ProtocolDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		DecodeAdapter decodeAdapter = new DecodeAdapter(in);
		ProtocolComponent c = ProtocolComponent.decode(decodeAdapter);

		out.add(c);
	}
	
}
