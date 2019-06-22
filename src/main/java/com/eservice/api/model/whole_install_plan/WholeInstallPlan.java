package com.eservice.api.model.whole_install_plan;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whole_install_plan")
public class WholeInstallPlan {
    /**
     * 整装排产
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 安装小组ID
     */
    @Column(name = "install_group_id")
    private Integer installGroupId;

    /**
     * 安排的开始安装日期
     */
    @Column(name = "install_date_plan")
    private Date installDatePlan;

    /**
     * 该整装记录对应的订单号
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 该整装记录对应的机器id
     */
    @Column(name = "machine_id")
    private Integer machineId;

    /**
     * 计划的备注
     */
    @Column(name = "cmt_send")
    private String cmtSend;

    /**
     * 0: 无效 1:有效
     */
    private Byte valid;

    /**
     * 记录的生成时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 记录的更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 获取整装排产
     *
     * @return id - 整装排产
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置整装排产
     *
     * @param id 整装排产
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取安装小组ID
     *
     * @return install_group_id - 安装小组ID
     */
    public Integer getInstallGroupId() {
        return installGroupId;
    }

    /**
     * 设置安装小组ID
     *
     * @param installGroupId 安装小组ID
     */
    public void setInstallGroupId(Integer installGroupId) {
        this.installGroupId = installGroupId;
    }

    /**
     * 获取安排的开始安装日期
     *
     * @return install_date_plan - 安排的开始安装日期
     */
    public Date getInstallDatePlan() {
        return installDatePlan;
    }

    /**
     * 设置安排的开始安装日期
     *
     * @param installDatePlan 安排的开始安装日期
     */
    public void setInstallDatePlan(Date installDatePlan) {
        this.installDatePlan = installDatePlan;
    }

    /**
     * 获取该整装记录对应的订单号
     *
     * @return order_id - 该整装记录对应的订单号
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置该整装记录对应的订单号
     *
     * @param orderId 该整装记录对应的订单号
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取该整装记录对应的机器id
     *
     * @return machine_id - 该整装记录对应的机器id
     */
    public Integer getMachineId() {
        return machineId;
    }

    /**
     * 设置该整装记录对应的机器id
     *
     * @param machineId 该整装记录对应的机器id
     */
    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    /**
     * 获取计划的备注
     *
     * @return cmt_send - 计划的备注
     */
    public String getCmtSend() {
        return cmtSend;
    }

    /**
     * 设置计划的备注
     *
     * @param cmtSend 计划的备注
     */
    public void setCmtSend(String cmtSend) {
        this.cmtSend = cmtSend;
    }

    /**
     * 获取0: 无效 1:有效
     *
     * @return valid - 0: 无效 1:有效
     */
    public Byte getValid() {
        return valid;
    }

    /**
     * 设置0: 无效 1:有效
     *
     * @param valid 0: 无效 1:有效
     */
    public void setValid(Byte valid) {
        this.valid = valid;
    }

    /**
     * 获取记录的生成时间
     *
     * @return create_date - 记录的生成时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置记录的生成时间
     *
     * @param createDate 记录的生成时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取记录的更新时间
     *
     * @return update_date - 记录的更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置记录的更新时间
     *
     * @param updateDate 记录的更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}