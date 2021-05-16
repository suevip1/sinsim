package com.eservice.api.model.task_plan;

import java.util.Date;
import javax.persistence.*;

@Table(name = "task_plan")
public class TaskPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 对应taskj记录的id
     */
    @Column(name = "task_record_id")
    private Integer taskRecordId;

    /**
     * 计划的类型  （日计划、弹性计划）-->目前没有在用，全是1。
     */
    @Column(name = "plan_type")
    private Byte planType;

    /**
     * task的计划完成时间
     */
    @Column(name = "plan_time")
    private Date planTime;

    /**
     * 弹性计划task的计划完成截止时间
     */
    @Column(name = "deadline")
    private Date deadline;


    /**
     * 添加计划的人
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上一次更改计划的时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    public Byte getPlanType() {
        return planType;
    }

    public void setPlanType(Byte planType) {
        this.planType = planType;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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
     * 获取对应taskj记录的id
     *
     * @return task_record_id - 对应taskj记录的id
     */
    public Integer getTaskRecordId() {
        return taskRecordId;
    }

    /**
     * 设置对应taskj记录的id
     *
     * @param taskRecordId 对应taskj记录的id
     */
    public void setTaskRecordId(Integer taskRecordId) {
        this.taskRecordId = taskRecordId;
    }

    /**
     * 获取task的计划完成时间
     *
     * @return plan_time - task的计划完成时间
     */
    public Date getPlanTime() {
        return planTime;
    }

    /**
     * 设置task的计划完成时间
     *
     * @param planTime task的计划完成时间
     */
    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    /**
     * 获取添加计划的人
     *
     * @return user_id - 添加计划的人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置添加计划的人
     *
     * @param userId 添加计划的人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
     * 获取上一次更改计划的时间
     *
     * @return update_time - 上一次更改计划的时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置上一次更改计划的时间
     *
     * @param updateTime 上一次更改计划的时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}