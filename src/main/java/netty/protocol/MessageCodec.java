package netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import netty.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @创建人 dw
 * @创建时间 2021/12/13
 * @描述
 */

public class MessageCodec extends ByteToMessageCodec<Message> {

    private static Logger log = LogManager.getLogger(MessageCodec.class);

    // 编码
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        // 1 魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2 版本
        out.writeByte(1);
        // 3 序列化的类型
        out.writeByte(0);
        // 4 字节的指令类型
        out.writeByte(message.getMessageType());
        // 5 指令请求序号
        out.writeInt(message.getSequenceId());
        out.writeByte(0xff);
        // 6 获取内容的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes = bos.toByteArray();
        // 7 长度
        out.writeInt(bytes.length);
        // 8 实际内容
        out.writeBytes(bytes);
        System.out.println("1");
    }

    // 解码
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 先拿魔数
        int i = in.readInt();
        // 再拿版本
        byte edition = in.readByte();
        // 再拿序列化类型
        byte type = in.readByte();
        // 再拿字节的指令类型
        byte instType = in.readByte();
        // 再拿指令请求序号
        int sequenceId = in.readInt();
        // 跳一个字节
        in.readByte();
        // 获取长度
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        if (instType == 0) {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Message msg = (Message) ois.readObject();
            log.info(msg);
            out.add(msg);
        }
        log.info("{},{},{},{},{},{}", i, edition, type, instType, sequenceId, length);
    }
}
