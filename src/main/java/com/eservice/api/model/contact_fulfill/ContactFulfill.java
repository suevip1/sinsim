package com.eservice.api.model.contact_fulfill;

import java.util.Date;
import javax.persistence.*;

@Table(name = "contact_fulfill")
public class ContactFulfill {
    /**
     * 落实单
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 联系单的ID
     */
    @Column(name = "contact_form_id")
    private Integer contactFormId;

    /**
     * 落实单的创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 落实单的更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 希望完成日期
     */
    @Column(name = "hope_date")
    private Date hopeDate;

    /**
     * 指定的落实人
     */
    @Column(name = "fulfill_man")
    private String fulfillMan;

    /**
     * 意见建议信息
     */
    private String message;

    /**
     * 落实人的反馈信息
     */
    private String feedback;

    /**
     * 落实状态: 初始化,未指定落实人员，落实进行中，落实完成
     */
    private String status;

    /**
     * 获取落实单
     *
     * @return id - 落实单
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置落实单
     *
     * @param id 落实单
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取联系单的ID
     *
     * @return contact_form_id - 联系单的ID
     */
    public Integer getContactFormId() {
        return contactFormId;
    }

    /**
     * 设置联系单的ID
     *
     * @param contactFormId 联系单的ID
     */
    public void setContactFormId(Integer contactFormId) {
        this.contactFormId = contactFormId;
    }

    /**
     * 获取落实单的创建时间
     *
     * @return create_date - 落实单的创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置落实单的创建时间
     *
     * @param createDate 落实单的创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取落实单的更新时间
     *
     * @return update_date - 落实单的更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置落实单的更新时间
     *
     * @param updateDate 落实单的更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取希望完成日期
     *
     * @return hope_date - 希望完成日期
     */
    public Date getHopeDate() {
        return hopeDate;
    }

    /**
     * 设置希望完成日期
     *
     * @param hopeDate 希望完成日期
     */
    public void setHopeDate(Date hopeDate) {
        this.hopeDate = hopeDate;
    }

    /**
     * 获取指定的落实人
     *
     * @return fulfill_man - 指定的落实人
     */
    public String getFulfillMan() {
        return fulfillMan;
    }

    /**
     * 设置指定的落实人
     *
     * @param fulfillMan 指定的落实人
     */
    public void setFulfillMan(String fulfillMan) {
        this.fulfillMan = fulfillMan;
    }

    /**
     * 获取意见建议信息
     *
     * @return message - 意见建议信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置意见建议信息
     *
     * @param message 意见建议信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取落实人的反馈信息
     *
     * @return feedback - 落实人的反馈信息
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * 设置落实人的反馈信息
     *
     * @param feedback 落实人的反馈信息
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * 获取落实状态: 初始化,未指定落实人员，落实进行中，落实完成
     *
     * @return status - 落实状态: 初始化,未指定落实人员，落实进行中，落实完成
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置落实状态: 初始化,未指定落实人员，落实进行中，落实完成
     *
     * @param status 落实状态: 初始化,未指定落实人员，落实进行中，落实完成
     */
    public void setStatus(String status) {
        this.status = status;
    }

    //0: 未成功； 1：成功
    @Column(name = "filfull_success")
    private boolean filfullSuccess;

    public boolean isFilfullSuccess() {
        return filfullSuccess;
    }

    public void setFilfullSuccess(boolean filfullSuccess) {
        this.filfullSuccess = filfullSuccess;
    }
}