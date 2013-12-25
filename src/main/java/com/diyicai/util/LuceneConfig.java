package com.diyicai.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 13-12-21
 * Time: 下午12:58
 * 有问题请联系 zhangyong7120180@163.com
 */
public class LuceneConfig {

    private static final Logger log = Logger.getLogger(LuceneConfig.class);

    private final static Properties props = new Properties();

    public final static String STORE_PATH = "STORE_PATH";

    static {
        InputStream in = null;
        try {
            in = ResourceUtils.getResourceAsStream("lucene.properties");
            props.load(in);
        } catch (IOException e) {
            log.error("加载lucene.properties出错", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("关闭lucene.properties出错", e);
                }
            }
        }
    }

    public static String getPropertyValue(String key) {
        return props.getProperty(key);
    }

}
