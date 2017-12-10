package com.eservice.api.model.task_quality_record;

import com.eservice.api.model.quality_record_image.QualityRecordImage;
import com.eservice.api.model.task_record.TaskRecord;

import javax.persistence.*;
import java.util.Date;

@Table(name = "task_quality_record")
public class TaskQualityRecordDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 对应安装项ID
     */
    @Column(name = "task_record_id")
    private Integer taskRecordId;

    /**
     * 质检员名字
     */
    private String name;

    /**
     * 质检结果: "1"==>通过； “0”==>不通过
     */
    private Byte status;

    /**
     * 添加质检结果的时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 质检备注
     */
    private String comment;


    /**
     * quality_record_image,task_record作为 TaskQualityRecordDetail的构成
     * 在根据 taskRecord的ID查询时一并返回
     */
    private QualityRecordImage qualityRecordImage;
    private TaskRecord taskRecord;

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
     * 获取对应安装项ID
     *
     * @return task_record_id - 对应安装项ID
     */
    public Integer getTaskRecordId() {
        return taskRecordId;
    }

    /**
     * 设置对应安装项ID
     *
     * @param taskRecordId 对应安装项ID
     */
    public void setTaskRecordId(Integer taskRecordId) {
        this.taskRecordId = taskRecordId;
    }

    /**
     * 获取质检员名字
     *
     * @return name - 质检员名字
     */
    public String getName() {
        return name;
    }

    /**
     * 设置质检员名字
     *
     * @param name 质检员名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取质检结果: "1"==>通过； “0”==>不通过
     *
     * @return status - 质检结果: "1"==>通过； “0”==>不通过
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置质检结果: "1"==>通过； “0”==>不通过
     *
     * @param status 质检结果: "1"==>通过； “0”==>不通过
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取添加质检结果的时间
     *
     * @return create_time - 添加质检结果的时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加质检结果的时间
     *
     * @param createTime 添加质检结果的时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取质检备注
     *
     * @return comment - 质检备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置质检备注
     *
     * @param comment 质检备注
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    public QualityRecordImage getQualityRecordImage() { return  this.qualityRecordImage; }
    public void setQualityRecordImage(QualityRecordImage qualityRecordImage) {this.qualityRecordImage = qualityRecordImage; }
    public TaskRecord getTaskRecord() { return this.taskRecord; }
    public void setTaskRecord(TaskRecord taskRecord) { this.taskRecord = taskRecord; }
}