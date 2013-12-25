package com.diyicai.bean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 13-12-21
 * Time: 下午1:05
 * 有问题请联系 zhangyong7120180@163.com
 */
public interface Searchable {

    public String getId();

    public void setId(String id);

    //存储的字段
    public List<String> getStoreFields();

    /**
     * 要进行分词索引的字段
     *
     * @return 返回字段名列表
     */
    public List<String> getIndexFields();

    /**
     * 文档的优先级
     *
     * @return
     */
    public float boost();

}
