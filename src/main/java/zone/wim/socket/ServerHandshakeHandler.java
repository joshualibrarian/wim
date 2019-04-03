package zone.wim.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ServerHandshakeHandler extends ChannelInboundHandlerAdapter {
	private static Logger LOGGER = Logger.getLogger(ChannelInboundHandlerAdapter.class.getCanonicalName());

//    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
    public void channelActive(ChannelHandlerContext ctx) {
    	LOGGER.info("channelActive()");
        // Send greeting for a new connection.
        try {
        	String welcome = "200 " 
					+ InetAddress.getLocalHost().getHostName() 
					+ " WIM v0.1! " + new Date() + "\r\n";
			final ChannelFuture f = ctx.writeAndFlush(
					Unpooled.copiedBuffer(welcome, CharsetUtil.UTF_8));
			ctx.fireChannelActive();
			f.addListener(new ChannelFutureListener() {
	            @Override
	            public void operationComplete(ChannelFuture future) {
	                LOGGER.info("OPERATION COMPLETE!");
	            }
	        });
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        SslHandler sslHandler = ctx.pipeline().get(SslHandler.class);
        
//        channels.add(ctx.channel());
    }
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		LOGGER.info("channelRead()");
		LOGGER.info(msg.toString());
	}

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}