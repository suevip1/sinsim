package com.eservice.api.model.install_plan_actual;

import javax.persistence.*;
import java.util.Date;

@Table(name = "install_plan_actual")
public class InstallPlanActualDetails extends InstallPlanActual{
    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 机器编号（铭牌）
     */
    private String nameplate;

    /**
     * 机器的位置
     */
    private String location;

    /**
     * 头数
     */
    private String headNum;

    /**
     * 部装时需要看针数
     */
    private String needleNum;

    public String getNeedleNum() {
        return needleNum;
    }

    public void setNeedleNum(String needleNum) {
        this.needleNum = needleNum;
    }

    /**
     * 计划的备注
     */
    private String cmtSend;

    private String groupName;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getNameplate() {
        return nameplate;
    }

    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeadNum() {
        return headNum;
    }

    public void setHeadNum(String headNum) {
        this.headNum = headNum;
    }

    public String getCmtSend() {
        return cmtSend;
    }

    public void setCmtSend(String cmtSend) {
        this.cmtSend = cmtSend;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 总装；部装；其他. (虽然从安装组里也可以读取该信息)
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "install_date_plan")
    private Date installDatePlan;

    public Date getInstallDatePlan() {
        return installDatePlan;
    }

    public void setInstallDatePlan(Date installDatePlan) {
        this.installDatePlan = installDatePlan;
    }

    //工序名称，比如用于看板查询
    private String taskName;
    private String abnormalName;
    private String taskStatus; //有异常报告未解决则设为 异常，

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAbnormalName() {
        return abnormalName;
    }

    public void setAbnormalName(String abnormalName) {
        this.abnormalName = abnormalName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}