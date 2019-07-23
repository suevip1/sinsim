package com.eservice.api.model.install_plan_actual;

import java.util.Date;
import javax.persistence.*;

@Table(name = "install_plan_actual")
public class InstallPlanActual {
    /**
     * 排产的实际反馈(总装，部装)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     */
    @Column(name = "install_plan_id")
    private Integer installPlanId;

    /**
     * 这次（今天）完成的头数
     */
    @Column(name = "head_count_done")
    private Integer headCountDone;

    /**
     * 这次（今天）安装组长反馈的备注
     */
    @Column(name = "cmt_feedback")
    private String cmtFeedback;

    /**
     * 电脑线， 这些焊线，都用varchar（10），方便处理，占用也不大
     */
    @Column(name = "pc_wire_num")
    private String pcWireNum;

    /**
     * 扣线
     */
    @Column(name = "kouxian_num")
    private String kouxianNum;

    /**
     * 灯线
     */
    @Column(name = "light_wire_num")
    private String lightWireNum;

    /**
     * 报警信号
     */
    @Column(name = "warn_signal_num")
    private String warnSignalNum;

    /**
     * 整装信号
     */
    @Column(name = "device_signal_num")
    private String deviceSignalNum;

    /**
     * 报警电源
     */
    @Column(name = "warn_power_num")
    private String warnPowerNum;

    /**
     * 装置电源
     */
    @Column(name = "device_power_num")
    private String devicePowerNum;

    /**
     * 装置补秀
     */
    @Column(name = "device_buxiu_num")
    private String deviceBuxiuNum;

    /**
     * 装置开关
     */
    @Column(name = "device_switch_num")
    private String deviceSwitchNum;

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
     * 获取排产的实际反馈(总装，部装)
     *
     * @return id - 排产的实际反馈(总装，部装)
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置排产的实际反馈(总装，部装)
     *
     * @param id 排产的实际反馈(总装，部装)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     *
     * @return install_plan_id - 1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     */
    public Integer getInstallPlanId() {
        return installPlanId;
    }

    /**
     * 设置1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     *
     * @param installPlanId 1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     */
    public void setInstallPlanId(Integer installPlanId) {
        this.installPlanId = installPlanId;
    }

    /**
     * 获取这次（今天）完成的头数
     *
     * @return head_count_done - 这次（今天）完成的头数
     */
    public Integer getHeadCountDone() {
        return headCountDone;
    }

    /**
     * 设置这次（今天）完成的头数
     *
     * @param headCountDone 这次（今天）完成的头数
     */
    public void setHeadCountDone(Integer headCountDone) {
        this.headCountDone = headCountDone;
    }

    /**
     * 获取这次（今天）安装组长反馈的备注
     *
     * @return cmt_feedback - 这次（今天）安装组长反馈的备注
     */
    public String getCmtFeedback() {
        return cmtFeedback;
    }

    /**
     * 设置这次（今天）安装组长反馈的备注
     *
     * @param cmtFeedback 这次（今天）安装组长反馈的备注
     */
    public void setCmtFeedback(String cmtFeedback) {
        this.cmtFeedback = cmtFeedback;
    }

    /**
     * 获取电脑线， 这些焊线，都用varchar（10），方便处理，占用也不大
     *
     * @return pc_wire_num - 电脑线， 这些焊线，都用varchar（10），方便处理，占用也不大
     */
    public String getPcWireNum() {
        return pcWireNum;
    }

    /**
     * 设置电脑线， 这些焊线，都用varchar（10），方便处理，占用也不大
     *
     * @param pcWireNum 电脑线， 这些焊线，都用varchar（10），方便处理，占用也不大
     */
    public void setPcWireNum(String pcWireNum) {
        this.pcWireNum = pcWireNum;
    }

    /**
     * 获取扣线
     *
     * @return kouxian_num - 扣线
     */
    public String getKouxianNum() {
        return kouxianNum;
    }

    /**
     * 设置扣线
     *
     * @param kouxianNum 扣线
     */
    public void setKouxianNum(String kouxianNum) {
        this.kouxianNum = kouxianNum;
    }

    /**
     * 获取灯线
     *
     * @return light_wire_num - 灯线
     */
    public String getLightWireNum() {
        return lightWireNum;
    }

    /**
     * 设置灯线
     *
     * @param lightWireNum 灯线
     */
    public void setLightWireNum(String lightWireNum) {
        this.lightWireNum = lightWireNum;
    }

    /**
     * 获取报警信号
     *
     * @return warn_signal_num - 报警信号
     */
    public String getWarnSignalNum() {
        return warnSignalNum;
    }

    /**
     * 设置报警信号
     *
     * @param warnSignalNum 报警信号
     */
    public void setWarnSignalNum(String warnSignalNum) {
        this.warnSignalNum = warnSignalNum;
    }

    /**
     * 获取整装信号
     *
     * @return device_signal_num - 整装信号
     */
    public String getDeviceSignalNum() {
        return deviceSignalNum;
    }

    /**
     * 设置整装信号
     *
     * @param deviceSignalNum 整装信号
     */
    public void setDeviceSignalNum(String deviceSignalNum) {
        this.deviceSignalNum = deviceSignalNum;
    }

    /**
     * 获取报警电源
     *
     * @return warn_power_num - 报警电源
     */
    public String getWarnPowerNum() {
        return warnPowerNum;
    }

    /**
     * 设置报警电源
     *
     * @param warnPowerNum 报警电源
     */
    public void setWarnPowerNum(String warnPowerNum) {
        this.warnPowerNum = warnPowerNum;
    }

    /**
     * 获取装置电源
     *
     * @return device_power_num - 装置电源
     */
    public String getDevicePowerNum() {
        return devicePowerNum;
    }

    /**
     * 设置装置电源
     *
     * @param devicePowerNum 装置电源
     */
    public void setDevicePowerNum(String devicePowerNum) {
        this.devicePowerNum = devicePowerNum;
    }

    /**
     * 获取装置补秀
     *
     * @return device_buxiu_num - 装置补秀
     */
    public String getDeviceBuxiuNum() {
        return deviceBuxiuNum;
    }

    /**
     * 设置装置补秀
     *
     * @param deviceBuxiuNum 装置补秀
     */
    public void setDeviceBuxiuNum(String deviceBuxiuNum) {
        this.deviceBuxiuNum = deviceBuxiuNum;
    }

    /**
     * 获取装置开关
     *
     * @return device_switch_num - 装置开关
     */
    public String getDeviceSwitchNum() {
        return deviceSwitchNum;
    }

    /**
     * 设置装置开关
     *
     * @param deviceSwitchNum 装置开关
     */
    public void setDeviceSwitchNum(String deviceSwitchNum) {
        this.deviceSwitchNum = deviceSwitchNum;
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