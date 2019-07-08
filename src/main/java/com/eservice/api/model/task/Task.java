package com.eservice.api.model.task;

import javax.persistence.*;

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 安装作业项的名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 质检用户的ID
     */
    @Column(name = "quality_user_id")
    private Integer qualityUserId;

    /**
     * 安装小组id
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 作业指导，后续可能会需要（一般是html格式）
     */
    private String guidance;

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
     * 获取安装作业项的名称
     *
     * @return task_name - 安装作业项的名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置安装作业项的名称
     *
     * @param taskName 安装作业项的名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 获取质检用户的ID
     *
     * @return quality_user_id - 质检用户的ID
     */
    public Integer getQualityUserId() {
        return qualityUserId;
    }

    /**
     * 设置质检用户的ID
     *
     * @param qualityUserId 质检用户的ID
     */
    public void setQualityUserId(Integer qualityUserId) {
        this.qualityUserId = qualityUserId;
    }

    /**
     * 获取安装小组id
     *
     * @return group_id - 安装小组id
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置安装小组id
     *
     * @param groupId 安装小组id
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取作业指导，后续可能会需要（一般是html格式）
     *
     * @return guidance - 作业指导，后续可能会需要（一般是html格式）
     */
    public String getGuidance() {
        return guidance;
    }

    /**
     * 设置作业指导，后续可能会需要（一般是html格式）
     *
     * @param guidance 作业指导，后续可能会需要（一般是html格式）
     */
    public void setGuidance(String guidance) {
        this.guidance = guidance;
    }

    // 该工序的标准用时,单位分钟
    @Column(name = "standard_minutes")
    private Integer standardMinutes;

    public Integer getStandardMinutes() {
        return standardMinutes;
    }

    public void setStandardMinutes(Integer standardMinutes) {
        this.standardMinutes = standardMinutes;
    }

}