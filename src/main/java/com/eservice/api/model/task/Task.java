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
}