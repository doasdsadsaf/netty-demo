package netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * @创建人 dw
 * @创建时间 2021/12/10
 * @描述
 */
public class EventLoopServer {
    public static void main(String[] args) throws InterruptedException {
        DefaultEventLoopGroup group = new DefaultEventLoopGroup();
        new ServerBootstrap().
                // 第一个EventLoop负责accept 后面的负责channel的read和write
                        group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 使用自定义的eventLoopGroup
                        ch.pipeline().addLast(group, "group1", new ChannelInboundHandlerAdapter() {
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                ctx.fireChannelRead(byteBuf);
                            }
                        });
                        // 定义第一个执行任务
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ByteBuf byteBuf = msg instanceof ByteBuf ? ((ByteBuf) msg) : null;
                                if (byteBuf != null) {
                                    System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
                                    /*byte[] buf = new byte[16];
                                    ByteBuf len = byteBuf.readBytes(buf, 0, byteBuf.readableBytes());
                                    System.out.println(buf.toString(CharsetUtil.UTF_8));*/
                                }
                            }

                        });

                    }


                }).bind(8888).sync();
    }
}
