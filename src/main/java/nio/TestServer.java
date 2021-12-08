package nio;

import utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 单线程 server
 *
 * @创建人 dw
 * @创建时间 2021/12/7
 * @描述
 */
public class TestServer {

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 1、建立连接 使用nio 单线程
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置连接是非阻塞的
        ssc.configureBlocking(false);
        // 2、绑定端口
        ssc.bind(new InetSocketAddress(80));
        // 3、 new一个channelList
        List<SocketChannel> list = new ArrayList<>();
        while (true) {
            //3、获取与客户端的连接
            //        System.out.println("------->sc");
            //accept() 是一个阻塞方法 没有客户端过来 直接停止运行了
            SocketChannel sc = ssc.accept();
            // 设置连接是非阻塞的
            if (sc != null) {
                //       System.out.println("------->,add{}" + sc);
                sc.configureBlocking(false); // 非阻塞模式
                list.add(sc);
            }
            for (SocketChannel channel : list) {
                // 写入buffer
                int read = channel.read(buffer);
                if (read > 0) {
                    // 转换为读
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    System.out.println(StandardCharsets.UTF_8.decode(buffer).toString());
                    buffer.clear();
                }
                //     System.out.println("------->read" + buffer);


            }
        }
    }
}
