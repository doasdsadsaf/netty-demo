package netty.c1;

import com.sun.org.apache.bcel.internal.generic.NEW;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @创建人 dw
 * @创建时间 2021/12/14
 * @描述
 */
public class HelloServerTow {
    public static void main(String[] args) {
        new ServerBootstrap().group(new NioEventLoopGroup()).channel(ServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {

                    }
                }).bind(11);
    }


}
