package client;

import com.alibaba.fastjson.JSON;
import com.diyicai.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Socket 例子
 * User: zhangyong
 * Date: 12-12-29
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
public class JsonClient {

    public static void main(String[] args) throws IOException {
        try {
            Map map = new HashMap();
            map.put("query", "红楼");
            map.put("curPage", "1");
            map.put("fieldName", "trasContent");
            map.put("pageSize", "10");
            String json = JSON.toJSONString(map);
            System.out.println("json:" + json);
            int length = json.getBytes("UTF-8").length;
            System.out.println(length);
            byte[] sampleByte = ByteUtil.addBytes(ByteUtil.intTo2Bytes(length), json.getBytes("utf-8"));
            System.out.println(sampleByte.length);

            Socket socket = new Socket("192.168.5.23", 7071);
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            long l1 = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {

                OutputStream out = socket.getOutputStream();
                out.write(sampleByte);
                out.flush();

                InputStream inputStream = socket.getInputStream();
                byte[] buf = new byte[10024];
                inputStream.read(buf);


                System.out.println("returnData==" + new String(buf, "utf-8"));
            }
            System.out.println("cost time==" + (System.currentTimeMillis() - l1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
