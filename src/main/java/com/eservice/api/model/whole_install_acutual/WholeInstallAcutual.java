package com.eservice.api.model.whole_install_acutual;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whole_install_acutual")
public class WholeInstallAcutual {
    /**
     * 整装排产的实际反馈
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     */
    @Column(name = "whole_install_plan_id")
    private Integer wholeInstallPlanId;

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
     * 今日上班人数，这些用varchar, varchar方便处理，而且可以考虑以后改为写名字
     */
    @Column(name = "attendance_member")
    private String attendanceMember;

    /**
     * 今日加班人数
     */
    @Column(name = "overtime_member")
    private String overtimeMember;

    /**
     * 请假人数
     */
    @Column(name = "absence_member")
    private String absenceMember;

    @Column(name = "attendance_tomorrow")
    private String attendanceTomorrow;

    /**
     * 获取整装排产的实际反馈
     *
     * @return id - 整装排产的实际反馈
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置整装排产的实际反馈
     *
     * @param id 整装排产的实际反馈
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     *
     * @return whole_install_plan_id - 1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     */
    public Integer getWholeInstallPlanId() {
        return wholeInstallPlanId;
    }

    /**
     * 设置1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     *
     * @param wholeInstallPlanId 1个排产计划，可以有多次反馈，比如第一天没完成，第二天继续，每天都要反馈
     */
    public void setWholeInstallPlanId(Integer wholeInstallPlanId) {
        this.wholeInstallPlanId = wholeInstallPlanId;
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
     * 获取今日上班人数，这些用varchar, varchar方便处理，而且可以考虑以后改为写名字
     *
     * @return attendance_member - 今日上班人数，这些用varchar, varchar方便处理，而且可以考虑以后改为写名字
     */
    public String getAttendanceMember() {
        return attendanceMember;
    }

    /**
     * 设置今日上班人数，这些用varchar, varchar方便处理，而且可以考虑以后改为写名字
     *
     * @param attendanceMember 今日上班人数，这些用varchar, varchar方便处理，而且可以考虑以后改为写名字
     */
    public void setAttendanceMember(String attendanceMember) {
        this.attendanceMember = attendanceMember;
    }

    /**
     * 获取今日加班人数
     *
     * @return overtime_member - 今日加班人数
     */
    public String getOvertimeMember() {
        return overtimeMember;
    }

    /**
     * 设置今日加班人数
     *
     * @param overtimeMember 今日加班人数
     */
    public void setOvertimeMember(String overtimeMember) {
        this.overtimeMember = overtimeMember;
    }

    /**
     * 获取请假人数
     *
     * @return absence_member - 请假人数
     */
    public String getAbsenceMember() {
        return absenceMember;
    }

    /**
     * 设置请假人数
     *
     * @param absenceMember 请假人数
     */
    public void setAbsenceMember(String absenceMember) {
        this.absenceMember = absenceMember;
    }

    /**
     * @return attendance_tomorrow
     */
    public String getAttendanceTomorrow() {
        return attendanceTomorrow;
    }

    /**
     * @param attendanceTomorrow
     */
    public void setAttendanceTomorrow(String attendanceTomorrow) {
        this.attendanceTomorrow = attendanceTomorrow;
    }
}