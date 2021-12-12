package netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @创建人 dw
 * @创建时间 2021/12/10
 * @描述
 */
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup(1))
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("init...");
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    }
                })
                .channel(NioSocketChannel.class).connect("localhost", 8888)
                .sync()
                .channel();

        channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("未发生非".getBytes()));
        Thread.sleep(2000);
        channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("wangwu".getBytes()));
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.sync();
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        group.shutdownGracefully();
    }
    }

