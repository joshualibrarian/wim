package zone.wim.socket;

import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelInitializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext ssl;

    public ServerInitializer(SslContext sslCtx) {
        this.ssl = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(ssl.newHandler());
        pipeline.addLast(new ServerHandshakeHandler());

        // On top of the SSL handler, add the text line codec.
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // and then business logic.
        pipeline.addLast(new ServerHandshakeHandler());
    }
}