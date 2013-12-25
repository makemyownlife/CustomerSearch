package com.diyicai.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrasDailyInfo implements Searchable {

    private String trasId;

    private String trasTitle;

    private String trasType;

    private String trasStatus;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private String userName;

    private Short status;

    private String trasContent;

    public String getTrasId() {
        return trasId;
    }

    public void setTrasId(String trasId) {
        this.trasId = trasId;
    }

    public String getTrasTitle() {
        return trasTitle;
    }

    public void setTrasTitle(String trasTitle) {
        this.trasTitle = trasTitle;
    }

    public String getTrasType() {
        return trasType;
    }

    public void setTrasType(String trasType) {
        this.trasType = trasType;
    }

    public String getTrasStatus() {
        return trasStatus;
    }

    public void setTrasStatus(String trasStatus) {
        this.trasStatus = trasStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getTrasContent() {
        return trasContent;
    }

    public void setTrasContent(String trasContent) {
        this.trasContent = trasContent;
    }

    //================================================= 相关实现方法 ====================================================================
    public String getId() {
        return this.getTrasId();
    }

    @Override
    public void setId(String id) {
        this.trasId = id;
    }

    public List<String> getStoreFields() {
        return null;
    }

    /**
     * 要进行分词索引的字段
     *
     * @return 返回字段名列表
     */
    public List<String> getIndexFields() {
        List<String> list = new ArrayList<String>();
        list.add("trasTitle");
        list.add("trasContent");
        return list;
    }

    public float boost() {
        return 1.0f;
    }

}