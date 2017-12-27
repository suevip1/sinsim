package com.eservice.api.model.abnormal_record;

import java.util.Date;
import javax.persistence.*;

@Table(name = "abnormal_record")
public class AbnormalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 异常类型
     */
    @Column(name = "abnormal_type")
    private Byte abnormalType;

    /**
     * 作业工序
     */
    @Column(name = "task_record_id")
    private Integer taskRecordId;

    /**
     * 提交异常的用户ID
     */
    @Column(name = "submit_user")
    private Integer submitUser;

    /**
     * 解决问题的用户对应的ID
     */
    @Column(name = "solution_user")
    private Integer solutionUser;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "solve_time")
    private Date solveTime;

    /**
     * 异常备注
     */
    private String comment;

    /**
     * 解决办法
     */
    private String solution;

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
     * 获取异常类型
     *
     * @return abnormal_type - 异常类型
     */
    public Byte getAbnormalType() {
        return abnormalType;
    }

    /**
     * 设置异常类型
     *
     * @param abnormalType 异常类型
     */
    public void setAbnormalType(Byte abnormalType) {
        this.abnormalType = abnormalType;
    }

    /**
     * 获取作业工序
     *
     * @return task_record_id - 作业工序
     */
    public Integer getTaskRecordId() {
        return taskRecordId;
    }

    /**
     * 设置作业工序
     *
     * @param taskRecordId 作业工序
     */
    public void setTaskRecordId(Integer taskRecordId) {
        this.taskRecordId = taskRecordId;
    }

    /**
     * 获取提交异常的用户ID
     *
     * @return submit_user - 提交异常的用户ID
     */
    public Integer getSubmitUser() {
        return submitUser;
    }

    /**
     * 设置提交异常的用户ID
     *
     * @param submitUser 提交异常的用户ID
     */
    public void setSubmitUser(Integer submitUser) {
        this.submitUser = submitUser;
    }

    /**
     * 获取解决问题的用户对应的ID
     *
     * @return solution_user - 解决问题的用户对应的ID
     */
    public Integer getSolutionUser() {
        return solutionUser;
    }

    /**
     * 设置解决问题的用户对应的ID
     *
     * @param solutionUser 解决问题的用户对应的ID
     */
    public void setSolutionUser(Integer solutionUser) {
        this.solutionUser = solutionUser;
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
     * @return solve_time
     */
    public Date getSolveTime() {
        return solveTime;
    }

    /**
     * @param solveTime
     */
    public void setSolveTime(Date solveTime) {
        this.solveTime = solveTime;
    }

    /**
     * 获取异常备注
     *
     * @return comment - 异常备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置异常备注
     *
     * @param comment 异常备注
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取解决办法
     *
     * @return solution - 解决办法
     */
    public String getSolution() {
        return solution;
    }

    /**
     * 设置解决办法
     *
     * @param solution 解决办法
     */
    public void setSolution(String solution) {
        this.solution = solution;
    }
}