package com.diyicai.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 启动对外的socket接口 暂时采用BIO的处理方式
 * User: zhangyong
 * Date: 13-12-21
 * Time: 下午9:03
 * any questions ,please contact zhangyong7120180@163.com
 */
public class StartApi {

    private static final Logger logger = LoggerFactory.getLogger(StartApi.class);

    private static StartApi INSTANCE = new StartApi();

    public static StartApi getInstance() {
        return INSTANCE;
    }

    public void start() throws IOException {
        new Thread(new SearchServerSocketThread()).start();
    }

}
