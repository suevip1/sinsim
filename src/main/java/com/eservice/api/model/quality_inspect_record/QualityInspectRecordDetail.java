package com.eservice.api.model.quality_inspect_record;

import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.quality_inspect.QualityInspect;
import com.eservice.api.model.task_record.TaskRecord;

import javax.persistence.*;
import java.util.Date;

@Table(name = "quality_inspect_record")
public class QualityInspectRecordDetail extends QualityInspectRecord {

    //下面这些信息 是为了给app端用，本来可以不需要，但是为了减少app端的改动先这样提供。
    private Machine machine;

    private MachineOrder machineOrder;

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public MachineOrder getMachineOrder() {
        return machineOrder;
    }

    public void setMachineOrder(MachineOrder machineOrder) {
        this.machineOrder = machineOrder;
    }

    /**
     * 3期，为了质检和工序安装相关连，下面这些来自 taskRecord内容。
     * 除去
     */
//    @Column(name = "task_name")
//    private String taskName;

    @Column(name = "process_record_id")
    private Integer processRecordId;

    /**
     * 对应流程中node节点的key值
     */
    @Column(name = "node_key")
    private Byte nodeKey;

    /**
     * 扫描组长（名字）
     */
    private String leader;

    /**
     *  3期新质检
     * "10" --> 未开始质检
     * "11" --> 无此检验条目
     * "12" --> 质检不合格
     * "13" --> 质检合格
     * "14" --> 未检
     */
    private Byte status;

    /**
     * task安装开始时间
     */
    @Column(name = "install_begin_time")
    private Date installBeginTime;

    /**
     * task安装结束时间
     */
    @Column(name = "install_end_time")
    private Date installEndTime;

    /**
     * task质检开始时间
     */
    @Column(name = "quality_begin_time")
    private Date qualityBeginTime;

    /**
     * task质检结束时间
     */
    @Column(name = "quality_end_time")
    private Date qualityEndTime;


    /**
     * 组长扫描结束之前，需要填入的工人名字,保存格式为string数组
     */
    @Column(name = "worker_list")
    private String workerList;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 等待安装时长
     */
    @Column(name = "wait_timespan")
    private Integer waitTimespan;

    public String getCmtFeedback() {
        return cmtFeedback;
    }

    public void setCmtFeedback(String cmtFeedback) {
        this.cmtFeedback = cmtFeedback;
    }

    /**
     * 扫码结束时可以添加备注信息, 比如填写为什么花费了这么多时间
     */
    @Column(name = "cmt_feedback")
    private String cmtFeedback;

    /**
     * @return process_record_id
     */
    public Integer getProcessRecordId() {
        return processRecordId;
    }

    /**
     * @param processRecordId
     */
    public void setProcessRecordId(Integer processRecordId) {
        this.processRecordId = processRecordId;
    }

    /**
     * 获取对应流程中node节点的key值
     *
     * @return node_key - 对应流程中node节点的key值
     */
    public Byte getNodeKey() {
        return nodeKey;
    }

    /**
     * 设置对应流程中node节点的key值
     *
     * @param nodeKey 对应流程中node节点的key值
     */
    public void setNodeKey(Byte nodeKey) {
        this.nodeKey = nodeKey;
    }

    /**
     * 获取扫描组长（名字）
     *
     * @return leader - 扫描组长（名字）
     */
    public String getLeader() {
        return leader;
    }

    /**
     * 设置扫描组长（名字）
     *
     * @param leader 扫描组长（名字）
     */
    public void setLeader(String leader) {
        this.leader = leader;
    }

    /** Task(工序)安装状态:
     * "0" --> 初始化状态
     * "1" --> 已计划 （没轮到安装）
     * "2" --> 待安装
     * "3" --> 开始安装
     * "4" --> 安装完成
     * "5" --> 质检中
     * "6" --> 质检完成
     * "7" --> 安装异常
     * "8" --> 质检异常
     * "9" --> 跳过
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置task状态，“1”==>未开始， “2”==>进行中，“3”==>完成， “4”==>异常
     *
     * @param status task状态，“1”==>未开始， “2”==>进行中，“3”==>完成， “4”==>异常
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getInstallBeginTime() {
        return installBeginTime;
    }

    public void setInstallBeginTime(Date installBeginTime) {
        this.installBeginTime = installBeginTime;
    }

    public Date getInstallEndTime() {
        return installEndTime;
    }

    public void setInstallEndTime(Date installEndTime) {
        this.installEndTime = installEndTime;
    }

    public Date getQualityBeginTime() {
        return qualityBeginTime;
    }

    public void setQualityBeginTime(Date qualityBeginTime) {
        this.qualityBeginTime = qualityBeginTime;
    }

    public Date getQualityEndTime() {
        return qualityEndTime;
    }

    public void setQualityEndTime(Date qualityEndTime) {
        this.qualityEndTime = qualityEndTime;
    }

    /**
     * 获取组长扫描结束之前，需要填入的工人名字,保存格式为string数组
     *
     * @return worker_list - 组长扫描结束之前，需要填入的工人名字,保存格式为string数组
     */
    public String getWorkerList() {
        return workerList;
    }

    /**
     * 设置组长扫描结束之前，需要填入的工人名字,保存格式为string数组
     *
     * @param workerList 组长扫描结束之前，需要填入的工人名字,保存格式为string数组
     */
    public void setWorkerList(String workerList) {
        this.workerList = workerList;
    }

    public void setUpdateTime(Date time) {
        this.updateTime = time;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setWaitTimespan(Integer waitTimespan) {
        this.waitTimespan = waitTimespan;
    }

    public Integer getWaitTimespan() {
        return waitTimespan;
    }

    private QualityInspect qualityInspect;

    public QualityInspect getQualityInspect() {
        return qualityInspect;
    }

    public void setQualityInspect(QualityInspect qualityInspect) {
        this.qualityInspect = qualityInspect;
    }
}