package nio;

import lombok.extern.slf4j.Slf4j;
import utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 单线程 server
 *
 * @创建人 dw
 * @创建时间 2021/12/7
 * @描述
 */

public class TestServer {


    public static void main(String[] args) throws IOException {
        // 创建selector 管理多个 channel
        Selector selector = Selector.open();

        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 1、建立连接 使用nio 单线程
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置连接是非阻塞的
        ssc.configureBlocking(false);

        // 向selector 注册channel 通过它可以知道是哪个channel发生的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // 只关注ACCEPT 事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        System.out.println("---->key:" + sscKey);
        // 2、绑定端口
        ssc.bind(new InetSocketAddress(80));
        while (true) {
            // 3、select 方法 阻塞直到绑定事件发生 select事件要么处理channel.accept() 要么取消 key.cancel()
            selector.select();
            // 4、处理事件 包含了所有发生的事件 包括ssc 和sc
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                System.out.println("---->forKey:" + key);
                // 5 区分事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, SelectionKey.OP_READ);
                    scKey.interestOps(SelectionKey.OP_READ);
                    System.out.println("-------->accept:" + sc);
                }else if(key.isReadable()) {
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                        int read = sc.read(byteBuffer);
                        if (read == -1) {
                            key.cancel();
                            sc.close();
                        }
                        buffer.flip();
               //         ByteBufferUtil.debugRead(byteBuffer);
                        System.out.println("---->code:"+Charset.defaultCharset().decode(buffer));
                        //   buffer.clear();
                    }catch (Exception e){
                        e.printStackTrace();
                        key.cancel();
                    }
                }
                // 处理之后移除
                iterator.remove();
            }
        }


    }
}
