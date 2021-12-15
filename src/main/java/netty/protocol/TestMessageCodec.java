package netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import netty.message.LoginRequestMessage;

/**
 * @创建人 dw
 * @创建时间 2021/12/13
 * @描述
 */
public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        /*EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(), new MessageCodec());
        LoginRequestMessage message = new LoginRequestMessage("zz", "123", "张");
        channel.writeOutbound(message);
        MessageCodec code = new MessageCodec();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        code.encode(null,message,buffer);*/



    }

}
