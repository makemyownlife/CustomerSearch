package com.diyicai.search;

import org.apache.log4j.Logger;

/**
 * 客服后台查询搜索的主入口
 * User: zhangyong
 * Date: 13-12-18
 * Time: 下午5:37
 * To change this template use File | Settings | File Templates.
 */
public class SearchMain {

    private final static Logger logger = Logger.getLogger(SearchMain.class);

    public static void main(String[] args) {
        try {
            SearchServer.getInstance().startup();
        } catch (Throwable e) {
            logger.error("搜索服务器启动异常，信息:", e);
            System.exit(-1);
        }
    }

}
