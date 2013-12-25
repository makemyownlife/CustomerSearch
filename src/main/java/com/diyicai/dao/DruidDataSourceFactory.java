package com.diyicai.dao;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 实现Druid 数据源
 * User: zhangyong
 * Date: 13-9-24
 * Time: 下午1:46
 * To change this template use File | Settings | File Templates.
 */
public class DruidDataSourceFactory implements DataSourceFactory {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setProperties(final Properties props) {
        try {
            this.dataSource = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(props);
            ((DruidDataSource) dataSource).init();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException("init datasource error", e);
        }
    }
}
