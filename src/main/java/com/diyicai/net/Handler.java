package com.diyicai.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diyicai.bean.TrasDailyInfo;
import com.diyicai.util.ByteUtil;
import com.diyicai.util.LuneneUtils;
import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 13-12-22
 * Time: 下午12:43
 * any questions ,please contact zhangyong7120180@163.com
 */
public class Handler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    private static final String ENCODING = "UTF-8";

    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            logger.info("socket==" + socket);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                byte first = in.readByte();
                byte second = in.readByte();
                //读取前两个字节,然后读取字符串的大小
                int dataSize = ((first & 0xff) << 8) + (second & 0xff);
                byte[] data = new byte[dataSize];
                in.readFully(data);
                String json = new String(data, ENCODING);
                logger.info("客户端请求json串：" + json);
                //解析json串
                JSONObject jsonObject = JSON.parseObject(json);
                String queryText = jsonObject.getString("query");
                String fieldName = jsonObject.getString("fieldName");
                String pageSize = jsonObject.getString("pageSize");
                String curPage = jsonObject.getString("curPage");
                //调用lucene接口相关
                Query query = LuneneUtils.createQuery(fieldName, queryText, 1.5f);
                List<String> idList = LuneneUtils.find(TrasDailyInfo.class, query, null, null, Integer.valueOf(curPage), Integer.valueOf(pageSize));
                int count = LuneneUtils.count(TrasDailyInfo.class, query, null);
                //返回结果
                Map<String, Object> returnMap = new HashMap<String, Object>();
                returnMap.put("count", count);
                returnMap.put("idList", idList);
                String returnJson = JSON.toJSONString(returnMap);
                byte[] returnJsonBytes = returnJson.getBytes(ENCODING);
                int returnLength = returnJsonBytes.length;
                logger.info("returnLength==" + returnLength);
                logger.info("returnJson==" + returnJson);
                byte[] sampleByte = ByteUtil.addBytes(ByteUtil.intTo2Bytes(returnLength), returnJsonBytes);
                outputStream.write(sampleByte);
                outputStream.flush();
            }
        } catch (Exception e) {
            logger.error("exception ", e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                logger.error("Handler run error ", e);
            }
        }
    }

    public static void main(String[] args) {
        // 238 - >  1110 1110 - > 反码： 1110 1101 -> 源码： 1001 0010  所以 是 -18
        //-18   源码 1001 0010 反码: 1110 1101 补码： 1110 1110
        byte[] b = ByteUtil.intTo2Bytes(238);
        System.out.println(b[0]);    // 0
        System.out.println(b[1]);    // -18
        System.out.println(Integer.toBinaryString(238));
        System.out.println(Integer.toBinaryString(-18));
    }

}
