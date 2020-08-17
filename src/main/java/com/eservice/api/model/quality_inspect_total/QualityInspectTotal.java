package com.eservice.api.model.quality_inspect_total;

import java.util.Date;
import javax.persistence.*;

@Table(name = "quality_inspect_total")
public class QualityInspectTotal {
    /**
     * 某机器的总体质检情况
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_number")
    private String orderNumber;

    /**
     * 机器的位置
     */
    private String location;

    /**
     * 质检的是哪台机器
     */
    @Column(name = "machine_nameplate")
    private String machineNameplate;

    /**
     * 质检结果整体做一个备注信息。
     */
    private String remark;

    /**
     * 该机器质检未开始，质检进行中，质检通过，质检未通过
     */
    @Column(name = "machine_inspect_status")
    private String machineInspectStatus;

    @Column(name = "remark_person")
    private String remarkPerson;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取某机器的总体质检情况
     *
     * @return id - 某机器的总体质检情况
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置某机器的总体质检情况
     *
     * @param id 某机器的总体质检情况
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return order_number
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * 获取机器的位置
     *
     * @return location - 机器的位置
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置机器的位置
     *
     * @param location 机器的位置
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取质检的是哪台机器
     *
     * @return machine_nameplate - 质检的是哪台机器
     */
    public String getMachineNameplate() {
        return machineNameplate;
    }

    /**
     * 设置质检的是哪台机器
     *
     * @param machineNameplate 质检的是哪台机器
     */
    public void setMachineNameplate(String machineNameplate) {
        this.machineNameplate = machineNameplate;
    }

    /**
     * 获取质检结果整体做一个备注信息。
     *
     * @return remark - 质检结果整体做一个备注信息。
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置质检结果整体做一个备注信息。
     *
     * @param remark 质检结果整体做一个备注信息。
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取该机器质检未开始，质检进行中，质检通过，质检未通过
     *
     * @return machine_inspect_status - 该机器质检未开始，质检进行中，质检通过，质检未通过
     */
    public String getMachineInspectStatus() {
        return machineInspectStatus;
    }

    /**
     * 设置该机器质检未开始，质检进行中，质检通过，质检未通过
     *
     * @param machineInspectStatus 该机器质检未开始，质检进行中，质检通过，质检未通过
     */
    public void setMachineInspectStatus(String machineInspectStatus) {
        this.machineInspectStatus = machineInspectStatus;
    }

    /**
     * @return remark_person
     */
    public String getRemarkPerson() {
        return remarkPerson;
    }

    /**
     * @param remarkPerson
     */
    public void setRemarkPerson(String remarkPerson) {
        this.remarkPerson = remarkPerson;
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
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}