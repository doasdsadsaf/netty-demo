package nio;

import utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @创建人 dw
 * @创建时间 2021/12/7
 * @描述
 */
public class TestClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",80));
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(StandardCharsets.UTF_8.encode("你好"));
       ByteBufferUtil.debugRead(buffer);
        sc.write(buffer);
        System.out.println("---------->");
    }
}
