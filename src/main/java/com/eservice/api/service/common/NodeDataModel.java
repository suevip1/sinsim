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
    private String task_status;

    public String getTaskStatus() {
        return task_status;
    }

    public void setTaskStatus(String status) {
        this.task_status = status;
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
    private String begin_time;

    public String getBeginTime() {
        return begin_time;
    }

    public void setBeginTime(String beginTime) {
        this.begin_time = beginTime;
    }
    //</editor-fold>

    //<editor-fold desc="获取或设置字段end_time,工序的结束时间">
    private String end_time;

    public String getEndTime() {
        return end_time;
    }

    public void setEndTime(String endTime) {
        this.end_time = endTime;
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
    private String work_list;

    public String getWorkList() {
        return work_list;
    }

    public void setWorkList(String workList) {
        this.work_list = workList;
    }
    //</editor-fold>

}
