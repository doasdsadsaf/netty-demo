package netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import sun.awt.EmbeddedFrame;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @创建人 dw
 * @创建时间 2021/12/13
 * @描述
 */
public class EncoderStudy {
    public static void main(String[] args) {
        // 模拟服务端
        // 用EmbeddedChannel 测试handler
        EmbeddedChannel channel = new EmbeddedChannel(
                // 整个数据长度 1024 偏移量为1 标识数据长度的字节是4,长度和有用数据的偏移量为1
                new LengthFieldBasedFrameDecoder(1024,1,
                        4,1,0),
                new LoggingHandler(LogLevel.DEBUG)
        );
        // 模拟客户端
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer,"hello");
        channel.writeInbound(buffer);
        send(buffer,"word");
        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buffer, String msg) {
        // 得到数据长度
        int length = msg.length();
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        // 写入长度标识符前面的数据
        buffer.writeByte(1);
        // 写入长度标识符
        buffer.writeInt(length);
        // 写入长度标识符后面的数据
        buffer.writeByte(2);
        // 写入具体的数据
        buffer.writeBytes(bytes);
    }

}
