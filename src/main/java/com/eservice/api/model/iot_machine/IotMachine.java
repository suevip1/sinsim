package com.eservice.api.model.iot_machine;

import javax.persistence.*;
import java.util.Date;

@Table(name = "iot_machine")
public class IotMachine {
    /**
     * iot开头的都是绣花机物联网项目
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameplate;

    /**
     * 机型信息
     */
    @Column(name = "machine_model_info")
    private String machineModelInfo;

    /**
     * 已开机的时间
     */
    private String uptime;

    /**
     * 刺绣（工作）时间
     */
    @Column(name = "working_time")
    private String workingTime;

    /**
     * 停机时间
     */
    @Column(name = "nonworking_time")
    private String nonworkingTime;

    /**
     * 断线次数
     */
    @Column(name = "line_broken_number")
    private String lineBrokenNumber;

    @Column(name = "line_broken_average_time")
    private String lineBrokenAverageTime;

    /**
     * 工件总数
     */
    @Column(name = "product_total_number")
    private String productTotalNumber;

    /**
     * 开机次数
     */
    @Column(name = "power_on_times")
    private String powerOnTimes;

    /**
     * 累计针数
     */
    @Column(name = "needle_total_number")
    private String needleTotalNumber;

    /**
     * 获取iot开头的都是绣花机物联网项目
     *
     * @return id - iot开头的都是绣花机物联网项目
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置iot开头的都是绣花机物联网项目
     *
     * @param id iot开头的都是绣花机物联网项目
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return nameplate
     */
    public String getNameplate() {
        return nameplate;
    }

    /**
     * @param nameplate
     */
    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    /**
     * 获取机型信息
     *
     * @return machine_model_info - 机型信息
     */
    public String getMachineModelInfo() {
        return machineModelInfo;
    }

    /**
     * 设置机型信息
     *
     * @param machineModelInfo 机型信息
     */
    public void setMachineModelInfo(String machineModelInfo) {
        this.machineModelInfo = machineModelInfo;
    }

    /**
     * 获取已开机的时间
     *
     * @return uptime - 已开机的时间
     */
    public String getUptime() {
        return uptime;
    }

    /**
     * 设置已开机的时间
     *
     * @param uptime 已开机的时间
     */
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    /**
     * 获取刺绣（工作）时间
     *
     * @return working_time - 刺绣（工作）时间
     */
    public String getWorkingTime() {
        return workingTime;
    }

    /**
     * 设置刺绣（工作）时间
     *
     * @param workingTime 刺绣（工作）时间
     */
    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    /**
     * 获取停机时间
     *
     * @return nonworking_time - 停机时间
     */
    public String getNonworkingTime() {
        return nonworkingTime;
    }

    /**
     * 设置停机时间
     *
     * @param nonworkingTime 停机时间
     */
    public void setNonworkingTime(String nonworkingTime) {
        this.nonworkingTime = nonworkingTime;
    }

    /**
     * 获取断线次数
     *
     * @return line_broken_number - 断线次数
     */
    public String getLineBrokenNumber() {
        return lineBrokenNumber;
    }

    /**
     * 设置断线次数
     *
     * @param lineBrokenNumber 断线次数
     */
    public void setLineBrokenNumber(String lineBrokenNumber) {
        this.lineBrokenNumber = lineBrokenNumber;
    }

    /**
     * @return line_broken_average_time
     */
    public String getLineBrokenAverageTime() {
        return lineBrokenAverageTime;
    }

    /**
     * @param lineBrokenAverageTime
     */
    public void setLineBrokenAverageTime(String lineBrokenAverageTime) {
        this.lineBrokenAverageTime = lineBrokenAverageTime;
    }

    /**
     * 获取工件总数
     *
     * @return product_total_number - 工件总数
     */
    public String getProductTotalNumber() {
        return productTotalNumber;
    }

    /**
     * 设置工件总数
     *
     * @param productTotalNumber 工件总数
     */
    public void setProductTotalNumber(String productTotalNumber) {
        this.productTotalNumber = productTotalNumber;
    }

    /**
     * 获取开机次数
     *
     * @return power_on_times - 开机次数
     */
    public String getPowerOnTimes() {
        return powerOnTimes;
    }

    /**
     * 设置开机次数
     *
     * @param powerOnTimes 开机次数
     */
    public void setPowerOnTimes(String powerOnTimes) {
        this.powerOnTimes = powerOnTimes;
    }

    /**
     * 获取累计针数
     *
     * @return needle_total_number - 累计针数
     */
    public String getNeedleTotalNumber() {
        return needleTotalNumber;
    }

    /**
     * 设置累计针数
     *
     * @param needleTotalNumber 累计针数
     */
    public void setNeedleTotalNumber(String needleTotalNumber) {
        this.needleTotalNumber = needleTotalNumber;
    }

    //该机器记录的首次创建时间
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 该机器记录的更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    //该机器记录信息的创建账号
    private String user;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}