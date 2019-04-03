package zone.wim.socket;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import zone.wim.library.Library;

public class SocketServer implements Runnable {	
	private static Logger LOGGER = Logger.getLogger(SocketServer.class.getCanonicalName());
//	Server serverItem;
	
	EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;
	
	@Override
	public void run() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			
            ServerBootstrap b = new ServerBootstrap()
             .group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ServerInitializer(sslCtx))
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
    
            Collection<Channel> channels = new ArrayList<>(Library.PORTS.size());
            for (int port : Library.PORTS) {
            	try {
            		Channel serverChannel = b.bind(port).sync().channel();
            		LOGGER.info("bound to port " + port);
            		channels.add(serverChannel);
            	} catch (Exception se) {
            		LOGGER.info("failed to bind port " + port);
            	}
            }
            for (Channel ch : channels) {
                ch.closeFuture().sync();
            }
            
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SSLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LOGGER.info("shutting down SocketServer()");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}
}
