package client;

import com.diyicai.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socket 例子
 * User: zhangyong
 * Date: 12-12-29
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
public class SampleClient {

    public static void main(String[] args) throws IOException {
        try {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><content><agent>31017541985</agent><action>215</action><msgID>0e5164263a664525930b</msgID><param><lotID>006</lotID><issue>0103658</issue></param><sign>257a8a78a49b3852bab8d63de0e78510</sign></content>";
            int length = xml.getBytes("UTF-8").length;
            System.out.println(length);
            byte[] sampleByte = ByteUtil.addBytes(ByteUtil.intTo2Bytes(length), xml.getBytes("utf-8"));
            System.out.println(sampleByte.length);

            Socket socket = new Socket("127.0.0.1", 7071);
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            long l1 = System.currentTimeMillis();
            for (int i = 0; i < 2; i++) {

                OutputStream out = socket.getOutputStream();
                out.write(sampleByte);
                out.flush();

                InputStream inputStream = socket.getInputStream();
                byte[] buf = new byte[10024];
                inputStream.read(buf);
                System.out.println("returnData==" + new String(buf, "utf-8"));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("cost time==" + (System.currentTimeMillis() - l1));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
