package zone.wim.socket;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.nio.*;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import zone.wim.item.Signer;

public class SocketClient implements Runnable {
	
	Signer user;
	String host;
	int port;
	SslContext sslCtx;
	SSLEngine ssl;
	
	public SocketClient(Signer user, String host, int port) {
		this.user = user;
		this.host = host;
		this.port = port;
		
		// Configure SSL.
        try {
			sslCtx = SslContextBuilder.forClient()
			    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} catch (SSLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		connect();
	}

	public void connect() {
		
		EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ClientInitializer(user, host, port));

            // Start the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                System.out.println("SOCKET CLIENT LINE: " + line);
                if (line == null) {
                    break;
                }

                // Sends the received line to the server.
                lastWriteFuture = ch.writeAndFlush(line + "\r\n");

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
	}

}
