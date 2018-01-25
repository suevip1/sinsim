package com.eservice.api.model.machine;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class MachinePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 对应的需求单号orderNum
     */
    private String orderNum;

    /**
     * 系统内部维护用的机器编号(年、月、类型、头数、针数、不大于台数的数字)
     */
    @Column(name = "machine_id")
    private String machineId;

    /**
     * 技术部填入的机器编号（铭牌）
     */
    private String nameplate;

    /**
     * 机器的位置，一般由生产部管理员上传
     */
    private String location;

    /**
     * 机器状态（“1”==>"初始化"，“2”==>"开始安装"，“3”==>"安装完成"，“4”==>"无效"）
     */
    private Byte status;

    @Column(name = "machine_type")
    private Integer machineType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 安装完成时间
     */
    @Column(name = "installed_time")
    private Date installedTime;

    /**
     * 真实发货时间（如果分批交付，需要用到，否则已订单交付为准）
     */
    @Column(name = "ship_time")
    private Date shipTime;

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
     * 安装完成工序数
     */
    private Integer installedTaskNum;

    /**
     * 已计划工序数
     */
    private Integer planedTaskNum;

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
     * 获取系统内部维护用的机器编号(年、月、类型、头数、针数、不大于台数的数字)
     *
     * @return machine_id - 系统内部维护用的机器编号(年、月、类型、头数、针数、不大于台数的数字)
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * 设置系统内部维护用的机器编号(年、月、类型、头数、针数、不大于台数的数字)
     *
     * @param machineId 系统内部维护用的机器编号(年、月、类型、头数、针数、不大于台数的数字)
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * 获取技术部填入的机器编号（铭牌）
     *
     * @return nameplate - 技术部填入的机器编号（铭牌）
     */
    public String getNameplate() {
        return nameplate;
    }

    /**
     * 设置技术部填入的机器编号（铭牌）
     *
     * @param nameplate 技术部填入的机器编号（铭牌）
     */
    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    /**
     * 获取机器的位置，一般由生产部管理员上传
     *
     * @return location - 机器的位置，一般由生产部管理员上传
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置机器的位置，一般由生产部管理员上传
     *
     * @param location 机器的位置，一般由生产部管理员上传
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取机器状态（“1”==>"初始化"，“2”==>"开始安装"，“3”==>"安装完成"，“4”==>"无效"）
     *
     * @return status - 机器状态（“1”==>"初始化"，“2”==>"开始安装"，“3”==>"安装完成"，“4”==>"无效"）
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置机器状态（“1”==>"初始化"，“2”==>"开始安装"，“3”==>"安装完成"，“4”==>"无效"）
     *
     * @param status 机器状态（“1”==>"初始化"，“2”==>"开始安装"，“3”==>"安装完成"，“4”==>"无效"）
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * @return machine_type
     */
    public Integer getMachineType() {
        return machineType;
    }

    /**
     * @param machineType
     */
    public void setMachineType(Integer machineType) {
        this.machineType = machineType;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取安装完成时间
     *
     * @return installed_time - 安装完成时间
     */
    public Date getInstalledTime() {
        return installedTime;
    }

    /**
     * 设置安装完成时间
     *
     * @param installedTime 安装完成时间
     */
    public void setInstalledTime(Date installedTime) {
        this.installedTime = installedTime;
    }

    /**
     * 获取发货时间（如果分批交付，需要用到，否则已订单交付为准）
     *
     * @return ship_time - 发货时间（如果分批交付，需要用到，否则已订单交付为准）
     */
    public Date getShipTime() {
        return shipTime;
    }

    /**
     * 设置发货时间（如果分批交付，需要用到，否则已订单交付为准）
     *
     * @param shipTime 发货时间（如果分批交付，需要用到，否则已订单交付为准）
     */
    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }
}