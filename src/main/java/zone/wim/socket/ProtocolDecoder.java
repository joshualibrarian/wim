package zone.wim.socket;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import zone.wim.codec.DecodeAdapter;
import zone.wim.protocol.ProtocolComponent;

public class ProtocolDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		DecodeAdapter decodeAdapter = new DecodeAdapter(in);
		ProtocolComponent c = ProtocolComponent.parse(decodeAdapter);
		out.add(c);
	}
	
}
