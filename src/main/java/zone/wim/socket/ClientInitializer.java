package zone.wim.socket;

import javax.net.ssl.SSLException;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import zone.wim.item.Signer;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

	SslContext sslCtx;
	String host;
	int port;

	public ClientInitializer(Signer user, String host, int port) {
		// Configure SSL.
        try {
			sslCtx = SslContextBuilder.forClient()
			    .trustManager(InsecureTrustManagerFactory.INSTANCE)
			    .build();
		} catch (SSLException e) {
			e.printStackTrace();
		}
	}
	
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslCtx.newHandler(ch.alloc(), host, port));

        // On top of the SSL handler, add the text line codec.
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // and then business logic.
        pipeline.addLast(new ClientHandshakeHandler());
    }
    
    
}
