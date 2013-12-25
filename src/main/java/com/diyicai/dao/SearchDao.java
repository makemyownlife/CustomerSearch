package com.diyicai.dao;

import com.diyicai.bean.TrasDailyInfo;
import com.diyicai.bean.TrasLuceneTask;
import org.apache.ibatis.session.ExecutorType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索查库
 * User: zhangyong
 * Date: 13-12-19
 * Time: 上午11:20
 * To change this template use File | Settings | File Templates.
 */
public class SearchDao {

    public static List<TrasLuceneTask> getTrasLuceneTasklist() {
        return DBTool.queryForList("Search.getTrasLuceneTasklist", null);
    }

    public static TrasDailyInfo getTrasInfo(String trasId) {
        return (TrasDailyInfo) DBTool.queryForObject("Search.getTrasInfo", trasId);
    }

    public static void updateTrasLuceneTaskStatus(Integer id, Integer status) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("status", status);
        DBTool.update("Search.updateTrasLuceneTaskStatus", map, ExecutorType.SIMPLE);
    }

}
