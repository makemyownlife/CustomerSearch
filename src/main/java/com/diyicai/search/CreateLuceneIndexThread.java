package com.diyicai.search;

import com.diyicai.bean.Searchable;
import com.diyicai.bean.TrasDailyInfo;
import com.diyicai.bean.TrasLuceneTask;
import com.diyicai.dao.SearchDao;
import com.diyicai.util.LuneneUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 创建lucene索引的线程
 * User: zhangyong
 * Date: 13-12-18
 * Time: 下午8:01
 * To change this template use File | Settings | File Templates.
 */
public class CreateLuceneIndexThread implements Runnable {

    private final Logger logger = Logger.getLogger(CreateLuceneIndexThread.class);

    public void run() {
        long startTime = System.currentTimeMillis();
        logger.info("开始创建lucene索引");
        List<TrasLuceneTask> trasLuceneTaskList = SearchDao.getTrasLuceneTasklist();
        logger.info("待处理任务数目:" + trasLuceneTaskList.size());
        for (TrasLuceneTask task : trasLuceneTaskList) {
            try {
                String trasId = task.getTrasId();                                      //事务日志公告
                Integer opt = task.getOpt();                                           //索引类型
                TrasDailyInfo trasDailyInfo = SearchDao.getTrasInfo(trasId);
                if (trasDailyInfo == null) {
                    logger.warn("事务编号:" + trasId + "没有查询到信息");
                    continue;
                }
                switch (opt.intValue()) {
                    case LuneneUtils.OPT_ADD:
                        LuneneUtils.add((Searchable) trasDailyInfo);
                        break;
                    case LuneneUtils.OPT_UPDATE:
                        break;
                    case LuneneUtils.OPT_DELETE:
                        LuneneUtils.delete((Searchable) trasDailyInfo);
                        break;
                }
                //修改task的状态
                SearchDao.updateTrasLuceneTaskStatus(task.getId(), 1);
            } catch (Exception e) {
                logger.error("建立索引异常 error ", e);
            }
        }
        logger.info("结束创建lucene索引 耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
    }

}
