package com.eservice.api.model.task_record;

import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.process_record.ProcessRecord;

import javax.persistence.*;
import java.util.Date;

@Table(name = "task_record")
public class TaskRecordDetail {
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
     * task开始时间
     */
    @Column(name = "begin_time")
    private Date beginTime;

    /**
     * task结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 组长扫描结束之前，需要填入的工人名字,保存格式为string数组
     */
    @Column(name = "worker_list")
    private String workerList;

    /**
    task_record相关的：
     process_Record 信息，
    machine信息,等
     */
    private ProcessRecord processRecord;
    private Machine machine;
    private MachineOrder machineOrder;

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

    /**
     * 获取task状态，“1”==>未开始， “2”==>进行中，“3”==>完成， “4”==>异常
     *
     * @return status - task状态，“1”==>未开始， “2”==>进行中，“3”==>完成， “4”==>异常
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

    /**
     * 获取task开始时间
     *
     * @return begin_time - task开始时间
     */
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * 设置task开始时间
     *
     * @param beginTime task开始时间
     */
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * 获取task结束时间
     *
     * @return end_time - task结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置task结束时间
     *
     * @param endTime task结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public ProcessRecord getProcessRecord() {return  this.processRecord; }
    public void setProcessRecord(ProcessRecord processRecord) { this.processRecord = processRecord; }

    /**
    获得Task_record的机器信息
     */
    public Machine getMachine(){ return  this.machine;}

    /**
     * 设置Task_record的机器信息
     */
    public void setMachine(Machine machine) { this.machine = machine; }
    public MachineOrder getMachineOrder() { return machineOrder; }
    public void setMachineOrder(MachineOrder machineOrder) { this.machineOrder = machineOrder; }
}