package dw.netty.c1;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class TestByteBuffer {

    @Test
    public void demo1() {

            // 创建一个缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 看一下初始时4个核心变量的值
            System.out.println("初始时-->limit--->" + byteBuffer.limit());
            System.out.println("初始时-->position--->" + byteBuffer.position());
            System.out.println("初始时-->capacity--->" + byteBuffer.capacity());
            System.out.println("初始时-->mark--->" + byteBuffer.mark());

            System.out.println("--------------------------------------");

            // 添加一些数据到缓冲区中
            String s = "根深蒂固多少个";
            byteBuffer.put(s.getBytes());

            // 看一下初始时4个核心变量的值
            System.out.println("put完之后-->limit--->" + byteBuffer.limit());
            System.out.println("put完之后-->position--->" + byteBuffer.position());
            System.out.println("put完之后-->capacity--->" + byteBuffer.capacity());
            System.out.println("put完之后-->mark--->" + byteBuffer.mark());
            // 切换模式 由读变写 或由写变读
            byteBuffer.flip();

            // 创建一个limit()大小的字节数组(因为就只有limit这么多个数据可读)
            byte[] bytes = new byte[byteBuffer.limit()];
            // 将读取的数据装进我们的字节数组中
            byteBuffer.get(bytes);
            // 输出数据
            System.out.println(new String(bytes, 0, bytes.length));
            System.out.println("flip完之后-->limit--->" + byteBuffer.limit());
            System.out.println("flip完之后-->position--->" + byteBuffer.position());
            System.out.println("flip完之后-->capacity--->" + byteBuffer.capacity());
            System.out.println("flip完之后-->mark--->" + byteBuffer.mark());
        }

        @Test
        public void fileDemo() throws FileNotFoundException {
        try{
            RandomAccessFile randomAccessFile = new RandomAccessFile("src\\fileDemo", "rw");
            FileChannel channel = randomAccessFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1);
            do{
                int read = channel.read(buffer);
                if(-1 == read){
                    return;
                }
                buffer.flip();
              //  while(buffer.hasRemaining()) {
                    System.out.println((char) buffer.get());
             //   }
                buffer.clear();
            }while (true);

        }catch (Exception e){

        }


        }



}
