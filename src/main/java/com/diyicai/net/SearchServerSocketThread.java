package com.diyicai.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 13-12-22
 * Time: 下午12:09
 * any questions ,please contact zhangyong7120180@163.com
 */
public class SearchServerSocketThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SearchServerSocketThread.class);

    private static final int PORT = 7071;

    private static final int POOL_SIZE = 3;

    private ServerSocket serverSocket = null;

    private ExecutorService executorService = null;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            //创建线程池
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
            service();
        } catch (Exception e) {
            logger.error("SearchServerSocketThread error ", e);
        }
    }

    private void service() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executorService.execute(new Handler(socket));
            } catch (Exception e) {
                logger.error("service error ", e);
            }
        }
    }

}
