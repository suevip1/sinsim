package com.eservice.api.model.process_record;

import java.util.Date;
import javax.persistence.*;

@Table(name = "process_record")
public class ProcessRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "machine_id")
    private Integer machineId;

    /**
     * 对应的模板（process）的ID
     */
    @Column(name = "process_id")
    private Integer processId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "end_time")
    private Date endTime;

    /**
     * 安装流程的link数据,格式参考linkDataArray
     */
    @Column(name = "link_data")
    private String linkData;

    /**
     * 安装流程的node数据，格式参考nodeDataArray
     */
    @Column(name = "node_data")
    private String nodeData;

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
     * @return machine_id
     */
    public Integer getMachineId() {
        return machineId;
    }

    /**
     * @param machineId
     */
    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    /**
     * 获取对应的模板（process）的ID
     *
     * @return process_id - 对应的模板（process）的ID
     */
    public Integer getProcessId() {
        return processId;
    }

    /**
     * 设置对应的模板（process）的ID
     *
     * @param processId 对应的模板（process）的ID
     */
    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取安装流程的link数据,格式参考linkDataArray
     *
     * @return link_data - 安装流程的link数据,格式参考linkDataArray
     */
    public String getLinkData() {
        return linkData;
    }

    /**
     * 设置安装流程的link数据,格式参考linkDataArray
     *
     * @param linkData 安装流程的link数据,格式参考linkDataArray
     */
    public void setLinkData(String linkData) {
        this.linkData = linkData;
    }

    /**
     * 获取安装流程的node数据，格式参考nodeDataArray
     *
     * @return node_data - 安装流程的node数据，格式参考nodeDataArray
     */
    public String getNodeData() {
        return nodeData;
    }

    /**
     * 设置安装流程的node数据，格式参考nodeDataArray
     *
     * @param nodeData 安装流程的node数据，格式参考nodeDataArray
     */
    public void setNodeData(String nodeData) {
        this.nodeData = nodeData;
    }
}