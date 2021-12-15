package netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                LoginRequestMessage loginRequestMessage) throws Exception {
        String username = loginRequestMessage.getUsername();
        String password = loginRequestMessage.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message = new LoginResponseMessage(login, login ? "登录成功" : "登录失败");
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
        }
        ctx.writeAndFlush(message);
    }
}
