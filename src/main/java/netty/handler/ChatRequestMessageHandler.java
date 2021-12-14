package netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.message.ChatRequestMessage;
import netty.message.ChatResponseMessage;
import netty.message.LoginRequestMessage;
import netty.message.LoginResponseMessage;
import service.UserServiceFactory;
import session.SessionFactory;

/**
 * @创建人 dw
 * @创建时间 2021/12/14
 * @描述
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null){
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(),msg.getContent()));
        }else{
            ctx.writeAndFlush(new ChatResponseMessage(false,"用户不存在或不在线"));
        }
    }
}
