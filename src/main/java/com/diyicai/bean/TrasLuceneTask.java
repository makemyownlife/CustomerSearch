package com.diyicai.bean;

import javax.xml.crypto.Data;
import java.io.Serializable;

/**
 * 事务的索引任务实体
 * User: zhangyong
 * Date: 13-12-19
 * Time: 下午8:19
 * 有问题请联系 zhangyong7120180@163.com
 */
public class TrasLuceneTask implements Serializable {

    private Integer id;

    private String trasId;

    private Integer type;

    private Integer opt;

    private Data createTime;

    private Integer status;

    private Data handleTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrasId() {
        return trasId;
    }

    public void setTrasId(String trasId) {
        this.trasId = trasId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOpt() {
        return opt;
    }

    public void setOpt(Integer opt) {
        this.opt = opt;
    }

    public Data getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Data createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Data handleTime) {
        this.handleTime = handleTime;
    }


}
