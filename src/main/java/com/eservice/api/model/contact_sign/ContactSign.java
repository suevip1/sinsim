package com.eservice.api.model.contact_sign;

import java.util.Date;
import javax.persistence.*;

@Table(name = "contact_sign")
public class ContactSign {
    /**
     * 变更联系单的签核
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
     * 联系单的当前签核步骤
     */
    @Column(name = "current_step")
    private String currentStep;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     *  签核内容，以json格式的数组形式存放.
     */
    @Column(name = "sign_content")
    private String signContent;

    /**
     * 获取变更联系单的签核
     *
     * @return id - 变更联系单的签核
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置变更联系单的签核
     *
     * @param id 变更联系单的签核
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
     * 获取联系单的当前签核步骤
     *
     * @return current_step - 联系单的当前签核步骤
     */
    public String getCurrentStep() {
        return currentStep;
    }

    /**
     * 设置联系单的当前签核步骤
     *
     * @param currentStep 联系单的当前签核步骤
     */
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
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
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 签核内容，以json格式的数组形式存放.
     *
     * @return sign_content -  签核内容，以json格式的数组形式存放.
     */
    public String getSignContent() {
        return signContent;
    }

    /**
     * 设置 签核内容，以json格式的数组形式存放.
     *
     * @param signContent  签核内容，以json格式的数组形式存放.
     */
    public void setSignContent(String signContent) {
        this.signContent = signContent;
    }
}