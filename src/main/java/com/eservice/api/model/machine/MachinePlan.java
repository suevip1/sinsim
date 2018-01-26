package com.eservice.api.model.machine;
import java.util.Date;

public class MachinePlan  extends Machine {

    /**
     * 对应的需求单号orderNum
     */
    private String orderNum;

    /**
     * 每一台机器，对应一个Process Record ID
     */
    private Integer processRecordID;

    /**
     * 合同交货时间
     */
    private Date contractShipDate;

    /**
     * 计划交货时间
     */
    private Date planShipDate;

    /**
     * 总工序数
     */
    private Integer totalTaskNum;

    /**
     * 安装中工序数
     */
    private Integer installingTaskNum;

    /**
     * 安装完成工序数
     */
    private Integer installedTaskNum;

    /**
     * 安装异常工序数
     */
    private Integer installAbnormalTaskNum;

    /**
     * 质检中工序数
     */
    private Integer qualityDoingTaskNum;

    /**
     * 质检完成工序数
     */
    private Integer qualityDoneTaskNum;

    /**
     * 质检异常工序数
     */
    private Integer qualityAbnormalTaskNum;

    /**
     * 已计划工序数
     */
    private Integer planedTaskNum;

    public Integer getQualityDoingTaskNum() {
        return qualityDoingTaskNum;
    }

    public void setQualityDoingTaskNum(Integer qualityDoingTaskNum) {
        this.qualityDoingTaskNum = qualityDoingTaskNum;
    }

    public Integer getQualityDoneTaskNum() {
        return qualityDoneTaskNum;
    }

    public void setQualityDoneTaskNum(Integer qualityDoneTaskNum) {
        this.qualityDoneTaskNum = qualityDoneTaskNum;
    }

    public Integer getInstallingTaskNum() {
        return installingTaskNum;
    }

    public void setInstallingTaskNum(Integer installingTaskNum) {
        this.installingTaskNum = installingTaskNum;
    }

    public Integer getInstallAbnormalTaskNum() {
        return installAbnormalTaskNum;
    }

    public void setInstallAbnormalTaskNum(Integer installAbnormalTaskNum) {
        this.installAbnormalTaskNum = installAbnormalTaskNum;
    }

    public Integer getQualityAbnormalTaskNum() {
        return qualityAbnormalTaskNum;
    }

    public void setQualityAbnormalTaskNum(Integer qualityAbnormalTaskNum) {
        this.qualityAbnormalTaskNum = qualityAbnormalTaskNum;
    }

    public Integer getProcessRecordID() {
        return processRecordID;
    }

    public void setProcessRecordID(Integer processRecordID) {
        this.processRecordID = processRecordID;
    }

    public Date getContractShipDate() {
        return contractShipDate;
    }

    public void setContractShipDate(Date contractShipDate) {
        this.contractShipDate = contractShipDate;
    }

    public Date getPlanShipDate() {
        return planShipDate;
    }

    public void setPlanShipDate(Date planShipDate) {
        this.planShipDate = planShipDate;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getTotalTaskNum() {
        return totalTaskNum;
    }

    public void setTotalTaskNum(Integer totalTaskNum) {
        this.totalTaskNum = totalTaskNum;
    }

    public Integer getInstalledTaskNum() {
        return installedTaskNum;
    }

    public void setInstalledTaskNum(Integer installedTaskNum) {
        this.installedTaskNum = installedTaskNum;
    }

    public Integer getPlanedTaskNum() {
        return planedTaskNum;
    }

    public void setPlanedTaskNum(Integer planedTaskNum) {
        this.planedTaskNum = planedTaskNum;
    }
}