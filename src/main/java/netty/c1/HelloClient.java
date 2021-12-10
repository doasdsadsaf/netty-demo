package netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

/**
 * @创建人 dw
 * @创建时间 2021/12/9
 * @描述
 */
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建普通启动类
         * 创建线程池+selector
         * 选择socketChannel 统一进行管理
         * 连接服务器
         */
        // 7 client启动器
        new Bootstrap()
                // 8 添加EventLoop
                .group(new NioEventLoopGroup())
                // 9 选择客户端channel实现
                .channel(NioSocketChannel.class)
                // 10 添加处理器
                .handler(new ChannelInitializer<Channel>() { // 3
                    // 12 连接到服务器之后调用处理器
                    @Override
                    protected void initChannel(Channel ch) {
                        // 15 把数据转换成byteBuf发送出去
                        ch.pipeline().addLast(new StringEncoder()); // 8
                    }
                })
                // 11 连接服务器
                .connect("127.0.0.1", 8080) // 4
                // 13 阻塞方法 直到连接建立
                .sync()
                // 连接建立好了 代表连接对象
                .channel()
                // 14 发送数据
                .writeAndFlush(new Date() + ": hello world!"); // 7

    }
}
