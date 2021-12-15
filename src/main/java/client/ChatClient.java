package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.message.*;
import netty.protocol.MessageCodecSharable;
import netty.protocol.ProtocolFrameDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 客户端代码
 *
 * @创建人 dw
 * @创建时间 2021/12/14
 * @描述
 */
public class ChatClient {

    private static Logger log = LogManager.getLogger(ChatClient.class);

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 添加handel打印日志
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    // 处理数据方式
                    channel.pipeline().addLast(new ProtocolFrameDecoder());
                    // 日志
                    channel.pipeline().addLast(loggingHandler);
                    // 编解码
                    channel.pipeline().addLast(messageCodecSharable);
                    // 入站处理器写入会发出站操作,往上找
                    channel.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter() {
                        // 接收响应消息
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("收到消息:{}", msg);
                            if (msg instanceof LoginResponseMessage) {
                                // 如果是登录响应信息
                                LoginResponseMessage message = (LoginResponseMessage) msg;
                                boolean isSuccess = message.isSuccess();
                                // 登录成功，设置登陆标记
                               if (isSuccess) {
                                   atomicBoolean.set(true);
                                }
                                // 登陆后，唤醒登陆线程
                                WAIT_FOR_LOGIN.countDown();
                            }
                        }

                        public void channelActive(ChannelHandlerContext ctx) {
                            // 开辟额外线程，用于用户登陆及后续操作
                            new Thread(()->{
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("请输入用户名");
                                String username = scanner.next();
                                System.out.println("请输入密码");
                                String password = scanner.next();
                                // 创建包含登录信息的请求体
                                LoginRequestMessage message = new LoginRequestMessage(username, password,null);
                                // 发送消息 调用 writeAndFlush pipeline会从当前向前执行 依次执行messageCodecSharable,loggingHandler,Decoder
                                ctx.writeAndFlush(message);
                                System.out.println("等待后续操作...");
                                // 阻塞，直到登陆成功后CountDownLatch被设置为0
                                try {
                                    WAIT_FOR_LOGIN.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // 执行后续操作
                                if (!atomicBoolean.get()) {
                                    // 登陆失败，关闭channel并返回
                                    ctx.channel().close();
                                    return;
                                }
                                // 登录成功后，执行其他操作
                                while (true) {
                                    System.out.println("==================================");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gsend [group name] [content]");
                                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                                    System.out.println("gmembers [group name]");
                                    System.out.println("gjoin [group name]");
                                    System.out.println("gquit [group name]");
                                    System.out.println("quit");
                                    System.out.println("==================================");
                                    String command = scanner.nextLine();
                                    // 获得指令及其参数，并发送对应类型消息
                                    String[] commands = command.split(" ");
                                    switch (commands[0]){
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(username, commands[1], commands[2]));
                                            break;
                                        case "gsend":
                                            ctx.writeAndFlush(new GroupChatRequestMessage(username,commands[1], commands[2]));
                                            break;
                                        case "gcreate":
                                            // 分割，获得群员名
                                            String[] members = commands[2].split(",");
                                            Set<String> set = new HashSet<>(Arrays.asList(members));
                                            // 把自己加入到群聊中
                                            set.add(username);
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(commands[1],set));
                                            break;
                                        case "gmembers":
                                            ctx.writeAndFlush(new GroupMembersRequestMessage(commands[1]));
                                            break;
                                        case "gjoin":
                                            ctx.writeAndFlush(new GroupJoinRequestMessage(username, commands[1]));
                                            break;
                                        case "gquit":
                                            ctx.writeAndFlush(new GroupQuitRequestMessage(username, commands[1]));
                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                        default:
                                            System.out.println("指令有误，请重新输入");
                                            continue;
                                    }
                                }
                            }, "login channel").start();
                        }


                    });
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // group 优雅关闭
            group.shutdownGracefully();
        }
    }

}
