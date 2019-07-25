package com.eservice.api.model.attendance;

import java.util.Date;
import javax.persistence.*;

public class Attendance {
    /**
     * 用于记录组长每天提交的考勤情况
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 谁上传
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 安装组
     */
    @Column(name = "install_group_id")
    private Integer installGroupId;

    /**
     * 记录的生成时间
     */
    private Date date;

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
     * 获取谁上传
     *
     * @return user_id - 谁上传
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置谁上传
     *
     * @param userId 谁上传
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取安装组
     *
     * @return install_group_id - 安装组
     */
    public Integer getInstallGroupId() {
        return installGroupId;
    }

    /**
     * 设置安装组
     *
     * @param installGroupId 安装组
     */
    public void setInstallGroupId(Integer installGroupId) {
        this.installGroupId = installGroupId;
    }

    /**
     * 获取记录的生成时间
     *
     * @return date - 记录的生成时间
     */
    public Date getDate() {
        return date;
    }

    /**
     * 设置记录的生成时间
     *
     * @param date 记录的生成时间
     */
    public void setDate(Date date) {
        this.date = date;
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