package netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ByteBufferUtil;

import java.nio.ByteBuffer;


/**
 * @创建人 dw
 * @创建时间 2021/12/13
 * @描述
 */
public class TestByteBuf {
    private static Logger log = LogManager.getLogger(TestByteBuf.class);

    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(16);
        log.info(buf.getClass().toString());

        // 写入数据
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ;i< 10;i++ ){
            sb.append(i);
        }
        buf.writeBytes(sb.toString().getBytes());
    }
}
