package com.eservice.api.model.quality_inspect_record;

import java.util.Date;
import javax.persistence.*;

@Table(name = "quality_inspect_record")
public class QualityInspectRecord {
    /**
     * 某机器的总体质检情况
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 质检的是哪台机器
     */
    @Column(name = "machine_nameplate")
    private String machineNameplate;

    /**
     * 质检的名称
     */
    @Column(name = "inspect_name")
    private String inspectName;

    @Column(name = "inspect_person")
    private String inspectPerson;

    @Column(name = "create_time")
    private Date createTime; //这个是在上传机器位置，生成了质检记录 时的时间，不是真正做质检的时间

    /**
     * 该条质检未开始，无此检验条目，质检合格，质检不合格。（没有质检中,因为是对一条质检内容而言,对机器有多条质检，才有质检中这样的状态）,“未检”按钮（因为无法检查，比如安装好了盖住了无法打开检验）,
     *
     * 为了和安装工序taskRecord交互，这个字段最终用的是数字。
     */
    @Column(name = "record_status")
    private String recordStatus;

    /**
     * 允许输入对该条质检的备注，也可为空
     */
    @Column(name = "record_remark")
    private String recordRemark;

    /**
     *  复检结果。 即最多复检一次。有弹框提示是否确认。不需要编辑。
     */
    @Column(name = "re_inspect")
    private String reInspect;

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
     * 获取质检的名称
     *
     * @return inspect_name - 质检的名称
     */
    public String getInspectName() {
        return inspectName;
    }

    /**
     * 设置质检的名称
     * QualityInspectRecord和QualityInspect如何联系： 共同的 inspect_name ，inspect_name是唯一的。
     * @param inspectName 质检的名称
     */
    public void setInspectName(String inspectName) {
        this.inspectName = inspectName;
    }

    /**
     * @return inspect_person
     */
    public String getInspectPerson() {
        return inspectPerson;
    }

    /**
     * @param inspectPerson
     */
    public void setInspectPerson(String inspectPerson) {
        this.inspectPerson = inspectPerson;
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
     * 获取该条质检未开始，无此检验条目，质检合格，质检不合格。（没有质检中,因为是对一条质检内容而言,对机器有多条质检，才有质检中这样的状态）,“未检”按钮（因为无法检查，比如安装好了盖住了无法打开检验）,
     *
     * @return record_status - 该条质检未开始，无此检验条目，质检合格，质检不合格。（没有质检中,因为是对一条质检内容而言,对机器有多条质检，才有质检中这样的状态）,“未检”按钮（因为无法检查，比如安装好了盖住了无法打开检验）,
     */
    public String getRecordStatus() {
        return recordStatus;
    }

    /**
     * 设置该条质检未开始，无此检验条目，质检合格，质检不合格。（没有质检中,因为是对一条质检内容而言,对机器有多条质检，才有质检中这样的状态）,“未检”按钮（因为无法检查，比如安装好了盖住了无法打开检验）,
     *
     * @param recordStatus 该条质检未开始，无此检验条目，质检合格，质检不合格。（没有质检中,因为是对一条质检内容而言,对机器有多条质检，才有质检中这样的状态）,“未检”按钮（因为无法检查，比如安装好了盖住了无法打开检验）,
     */
    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * 获取允许输入对该条质检的备注，也可为空
     *
     * @return record_remark - 允许输入对该条质检的备注，也可为空
     */
    public String getRecordRemark() {
        return recordRemark;
    }

    /**
     * 设置允许输入对该条质检的备注，也可为空
     *
     * @param recordRemark 允许输入对该条质检的备注，也可为空
     */
    public void setRecordRemark(String recordRemark) {
        this.recordRemark = recordRemark;
    }

    /**
     * 获取 复检结果。 即最多复检一次。有弹框提示是否确认。不需要编辑。
     *
     * @return re_inspect -  复检结果。 即最多复检一次。有弹框提示是否确认。不需要编辑。
     */
    public String getReInspect() {
        return reInspect;
    }

    /**
     * 设置 复检结果。 即最多复检一次。有弹框提示是否确认。不需要编辑。
     *
     * @param reInspect  复检结果。 即最多复检一次。有弹框提示是否确认。不需要编辑。
     */
    public void setReInspect(String reInspect) {
        this.reInspect = reInspect;
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

    /**
     * 质检对应的工序, 一个工序可以有多个检验条目， “无此检验条目” 可以一个个点，也可以按照工序一次点一堆条目。
     * 因为“质检内容：要允许编辑”，所以这个表要和quality_inspect脱离关系，所有质检信息都要保存在此表，不依赖质检内容的表。所以增加了taskName。
     * QualityInspectRecord和QualityInspect如何联系： 共同的 inspect_name ，inspect_name是唯一的。
     */
    @Column(name = "task_name")
    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Column(name = "order_number")
    private String orderNumber;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}