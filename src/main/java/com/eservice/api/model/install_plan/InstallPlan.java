package com.eservice.api.model.install_plan;

import java.util.Date;
import javax.persistence.*;

@Table(name = "install_plan")
public class InstallPlan {
    /**
     * 排产(总装,部装)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 总装；部装；其他. (虽然从安装组里也可以读取该信息)
     */
    private String type;

    /**
     * 安装小组名称
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
     * 0: 无效 1:有效。 删掉时为0
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
     * 空表示该排产还未发送，发送后填发送时间
     */
    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 获取排产(总装,部装)
     *
     * @return id - 排产(总装,部装)
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置排产(总装,部装)
     *
     * @param id 排产(总装,部装)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取总装；部装；其他. (虽然从安装组里也可以读取该信息)
     *
     * @return type - 总装；部装；其他. (虽然从安装组里也可以读取该信息)
     */
    public String getType() {
        return type;
    }

    /**
     * 设置总装；部装；其他. (虽然从安装组里也可以读取该信息)
     *
     * @param type 总装；部装；其他. (虽然从安装组里也可以读取该信息)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取安装小组名称
     *
     * @return install_group_id - 安装小组名称
     */
    public Integer getInstallGroupId() {
        return installGroupId;
    }

    /**
     * 设置安装小组名称
     *
     * @param installGroupId 安装小组名称
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
     * 获取0: 无效 1:有效。 删掉时为0
     *
     * @return valid - 0: 无效 1:有效。 删掉时为0
     */
    public Byte getValid() {
        return valid;
    }

    /**
     * 设置0: 无效 1:有效。 删掉时为0
     *
     * @param valid 0: 无效 1:有效。 删掉时为0
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

    /**
     * 获取空表示该排产还未发送，发送后填发送时间
     *
     * @return send_time - 空表示该排产还未发送，发送后填发送时间
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 设置空表示该排产还未发送，发送后填发送时间
     *
     * @param sendTime 空表示该排产还未发送，发送后填发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}