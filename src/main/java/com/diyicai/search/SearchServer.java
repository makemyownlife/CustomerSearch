package com.diyicai.search;

import com.diyicai.dao.DBTool;
import com.diyicai.net.StartApi;
import org.apache.log4j.Logger;

/**
 * 搜索服务器
 * User: zhangyong
 * Date: 13-12-18
 * Time: 下午7:16
 * To change this template use File | Settings | File Templates.
 */
public class SearchServer {

    private final static Logger logger = Logger.getLogger(SearchServer.class);

    private static final SearchServer INSTANCE = new SearchServer();

    public static SearchServer getInstance() {
        return INSTANCE;
    }

    public void startup() throws Exception {
        logger.info("==========================服务端开始启动==================================");

        logger.info("开始初始化数据库");
        DBTool.initDb();
        logger.info("结束初始化数据库");

        logger.info("开始初始化定时任务");
        LuceneTimerTask task = new LuceneTimerTask();
        task.init();
        logger.info("结束初始化定时任务");

        logger.info("开始创建socket服务");
        StartApi.getInstance().start();
        logger.info("结束创建socket服务");

        logger.info("==========================服务端结束启动 ^_^=================================");
    }


}
