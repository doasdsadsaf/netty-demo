package netty.c3;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import nio.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @创建人 dw
 * @创建时间 2021/12/10
 * @描述
 */
public class TestEventLoop {
    private static Logger log = LoggerFactory.getLogger(TestEventLoop.class);
    public static void main(String[] args) {
        // 1. 创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup(2); // io 事件，普通任务，定时任务
//        EventLoopGroup group = new DefaultEventLoopGroup(); // 普通任务，定时任务
        // 2. 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        // 3. 执行普通任务
        /*group.next().execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();ddd
            }
            log.debug("ok");
        });*/
        // 4. 执行定时任务
        group.next().scheduleAtFixedRate(() -> {
            log.info("ok");
            System.out.println("ok");
        }, 0, 1, TimeUnit.SECONDS);

        log.info("main");
    }
}
