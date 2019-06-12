package com.eservice.api.model.task_record;

import java.util.Date;
import javax.persistence.*;

@Table(name = "task_record")
public class TaskRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_name")
    private String taskName;

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
     * task状态，“1”==>未开始， “2”==>进行中，“3”==>完成， “4”==>异常
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

    @Column(name = "plan_timespan")
    private Integer planTimespan;

    public Integer getPlanTimespan() {
        return planTimespan;
    }

    public void setPlanTimespan(Integer planTimespan) {
        this.planTimespan = planTimespan;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return task_name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

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
}