package com.diyicai.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;

import java.io.Reader;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 初始化
 * User: zhangyong
 * Date: 13-9-24
 * Time: 上午10:53
 * To change this template use File | Settings | File Templates.
 */
public class DBTool {

    private static volatile SqlSessionFactory factory = null;

    private static AtomicBoolean isInit = new AtomicBoolean(false);

    static {
        try {
            initDb();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize DaoConfig.  Cause: " + e);
        }
    }

    public static synchronized void initDb() throws Exception {
        if (isInit.get()) {
            return;
        }
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis/configuration.xml");
            factory = builder.build(reader);
            isInit.getAndSet(true);
        } catch (Exception e) {
            throw new RuntimeException("initDb error ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //=====================================================常用方法如下所示================================================================
    public static List queryForList(String statement, Object obj, ExecutorType executorType) {
        SqlSession sqlSession = null;
        try {
            sqlSession = factory.openSession(executorType, TransactionIsolationLevel.READ_COMMITTED);
            return sqlSession.selectList(statement, obj);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public static List queryForList(String statement, Object obj) {
        return queryForList(statement, obj, ExecutorType.SIMPLE);
    }

    public static Object queryForObject(String statement, Object obj, ExecutorType executorType) {
        SqlSession sqlSession = null;
        try {
            sqlSession = factory.openSession(executorType, TransactionIsolationLevel.READ_COMMITTED);
            return sqlSession.selectOne(statement, obj);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public static Object queryForObject(String statement, Object obj) {
        return queryForObject(statement, obj, ExecutorType.SIMPLE);
    }

    public static int update(String statement, Object obj, ExecutorType executorType) {
        SqlSession sqlSession = null;
        try {
            sqlSession = factory.openSession(executorType, TransactionIsolationLevel.READ_COMMITTED);
            int count = sqlSession.update(statement, obj);
            sqlSession.commit();
            return count;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
            List list = DBTool.queryForList("TSporrttery.getTSportteryMatchComfrim", null);
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
