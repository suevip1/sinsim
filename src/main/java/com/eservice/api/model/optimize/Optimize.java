package com.eservice.api.model.optimize;

import java.util.Date;
import javax.persistence.*;

public class Optimize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 优化类部件
     */
    @Column(name = "optimize_part")
    private String optimizePart;

    /**
     * 订单号
     */
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 机型
     */
    @Column(name = "machine_type")
    private String machineType;

    /**
     * 目的
     */
    private String purpose;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 工时
     */
    @Column(name = "working_hours")
    private String workingHours;

    /**
     * 效果
     */
    private String results;

    /**
     * 附件
     */
    private String files;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

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
     * 获取项目名称
     *
     * @return project_name - 项目名称
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * 设置项目名称
     *
     * @param projectName 项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 获取优化类部件
     *
     * @return optimize_part - 优化类部件
     */
    public String getOptimizePart() {
        return optimizePart;
    }

    /**
     * 设置优化类部件
     *
     * @param optimizePart 优化类部件
     */
    public void setOptimizePart(String optimizePart) {
        this.optimizePart = optimizePart;
    }

    /**
     * 获取订单号
     *
     * @return order_num - 订单号
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * 设置订单号
     *
     * @param orderNum 订单号
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取机型
     *
     * @return machine_type - 机型
     */
    public String getMachineType() {
        return machineType;
    }

    /**
     * 设置机型
     *
     * @param machineType 机型
     */
    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    /**
     * 获取目的
     *
     * @return purpose - 目的
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * 设置目的
     *
     * @param purpose 目的
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * 获取负责人
     *
     * @return owner - 负责人
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置负责人
     *
     * @param owner 负责人
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取工时
     *
     * @return working_hours - 工时
     */
    public String getWorkingHours() {
        return workingHours;
    }

    /**
     * 设置工时
     *
     * @param workingHours 工时
     */
    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    /**
     * 获取效果
     *
     * @return results - 效果
     */
    public String getResults() {
        return results;
    }

    /**
     * 设置效果
     *
     * @param results 效果
     */
    public void setResults(String results) {
        this.results = results;
    }

    /**
     * 获取附件
     *
     * @return files - 附件
     */
    public String getFiles() {
        return files;
    }

    /**
     * 设置附件
     *
     * @param files 附件
     */
    public void setFiles(String files) {
        this.files = files;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return update_date
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}