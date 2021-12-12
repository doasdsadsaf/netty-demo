package netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class NettyFuTrue {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 获得EventLoop
        EventLoop eventLoop = group.next();
        Future<Integer> future = eventLoop.submit(() -> 1);
        System.out.println("+++"+future.get());
        future.addListener(future1 -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(future1.get());

        });
    }

}
