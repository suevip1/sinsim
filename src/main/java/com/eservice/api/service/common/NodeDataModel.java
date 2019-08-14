package com.eservice.api.service.common;

/*
此类用于ProcessRecord的
NodeData JSON数据的解析
*/
public class NodeDataModel {

    //<editor-fold desc="获取或设置字段category,流程图的类型">
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段loc,流程图的坐标值">
    private String loc;

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段task_status,工序的完成状态">
    private String taskStatus;

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String status) {
        this.taskStatus = status;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段key,工序的Key">
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段text,工序的名称">
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段begin_time,工序的开始时间">
    private String beginTime;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段end_time,工序的结束时间">
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段leader,工序的组长">
    private String leader;

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段work_list,工序的实施人员">
    private String workList;

    public String getWorkList() {
        return workList;
    }

    public void setWorkList(String workList) {
        this.workList = workList;
    }
    //</editor-fold>

    private Integer waitTimespan;
    public void setWaitTimespan(Integer waitTimespan) {
        this.waitTimespan = waitTimespan;
    }

    public Integer getWaitTimespan() {
        return waitTimespan;
    }

}
