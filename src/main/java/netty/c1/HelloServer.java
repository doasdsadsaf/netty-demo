package netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @创建人 dw
 * @创建时间 2021/12/9
 * @描述
 */
public class HelloServer {

    /**
     * 启动器
     * 创建线程池+selector
     * 先选择Server channel
     * 再定义一个childHandler 统一管理socketChannel
     *
     * @param args
     */
    public static void main(String[] args) {
        // 1、启动器 负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2、创建 NioEventLoopGroup，可以简单理解为 `线程池 + Selector` 后面会详细展开
                .group(new NioEventLoopGroup())
                //3、选择服务 Socket 实现类，其中 NioServerSocketChannel 表示基于 NIO 的服务器端实现，他还有BIO。。。实现
                .channel(NioServerSocketChannel.class)
                //4、为啥方法叫 childHandler，是接下来添加的处理器都是给 SocketChannel 用的，而不是给 ServerSocketChannel。
                // ChannelInitializer 处理器（仅执行一次），它的作用是待客户端 SocketChannel 建立连接后，执行 initChannel
                // 以便添加更多的处理器
                // 负责处理连接child，负责处理读写
                .childHandler(
                        // 5 channel 代表和客户端进行数据读写的通道 只是添加了,还没初始化
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            // 12、与client建立连接后，重写初始化方法
                            protected void initChannel(NioSocketChannel ch) {
                                //16、由某个EventLoop处理read时间，接收到byteBuf，解码 ByteBuf => String
                                ch.pipeline().addLast(new StringDecoder());
                                //17、SocketChannel 的业务处理器，使用上一个处理器的处理结果
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    // 18 执行read方法打印hello, 读事件
                                    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                //6、ServerSocketChannel 绑定的监听端口
                .bind(8080);
    }
}
