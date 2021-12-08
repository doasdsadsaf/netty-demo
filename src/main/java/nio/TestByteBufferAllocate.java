package nio;

import java.nio.ByteBuffer;

/**
 * @创建人 dw
 * @创建时间 2021/12/6
 * @描述
 */
public class TestByteBufferAllocate {
    public static void main(String[] args) {
        // 使用的java堆内存 效率低 会受GC影响
        ByteBuffer.allocate(10);
        // 使用直接内存 不受GC影响 可能会造成内存泄漏
        ByteBuffer.allocateDirect(100);
    }

}
